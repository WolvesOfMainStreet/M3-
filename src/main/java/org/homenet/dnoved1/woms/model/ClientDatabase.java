package org.homenet.dnoved1.woms.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.homenet.dnoved1.woms.Consumer;
import org.homenet.dnoved1.woms.ImmediateFuture;
import org.homenet.dnoved1.woms.Pair;
import org.homenet.dnoved1.woms.model.report.Report;

/**
 * The client database for applications. Depends on some other database
 * connection for getting the actual data, but will keep a cache of accounts
 * and transactions for the currently logged in user.
 */
public final class ClientDatabase implements DatabaseConnection {

    // TODO: make sure synchronized access to accounts, transactions, etc is
    //       correct.
    // TODO: do something with all the possible exceptions that occur inside
    //       Runnables and Consumers.

    /**The current instance of the client database.*/
    private static ClientDatabase instance;

    /**
     * The backing connection that the client database uses to get its data.
     */
    private DatabaseConnection backingConnection;

    /**
     * A separate thread that will constantly check if requested futures are
     * done, caching the result when they are.
     *
     * @see #futureConsumers
     * @see ConsumerFeeder
     */
    private Thread futureUnwrapper;

    /**
     * A list of requested futures and consumers of those futures' results
     * created by this class. A list of pairs is used instead of a map so
     * that access can be made without synchronizing. This is possible by
     * iterating over a snapshot of the list and only removing during that
     * iteration.
     *
     * @see #futureUnwrapper
     * @see ConsumerFeeder
     */
    private List<Pair<Future<?>, Consumer<?>>> futureConsumers;

    /**The currently logged in user.*/
    private User currentUser;
    /**The currently selected account.*/
    private Account currentAccount;
    /**The currently selected transaction.*/
    private Transaction currentTransaction;

    /**All of the observers on the current user's accounts.*/
    private Collection<DataSetObserver<Account>> accountObservers;
    /**A map from accounts to observers on that account's transactions.*/
    private Map<Account, Collection<DataSetObserver<Transaction>>> transactionObservers;

    /**
     * A cache of the currently logged in user's accounts. May be null until
     * they are requested.
     */
    private Collection<Account> accounts;

    /**A lock for {@link #accounts}, since it is null when it is empty.*/
    private Object accountLock = new Object();

    /**
     * A map of the currently logged in user's accounts to a cache of its
     * transactions. Each account's transaction cache may be null until they
     * are requested.
     */
    private Map<Account, Collection<Transaction>> transactions;

    /**
     * Create a new Client database with the given backing connection.
     *
     * @param connection the backing connection for the client database,
     */
    private ClientDatabase(DatabaseConnection connection) {
        this.backingConnection = connection;
        this.transactions = new HashMap<Account, Collection<Transaction>>();
        this.accountObservers = new ArrayList<DataSetObserver<Account>>();
        this.transactionObservers = new HashMap<Account, Collection<DataSetObserver<Transaction>>>();

        // Use a linked list for easy removals in the middle of the list.
        this.futureConsumers = new LinkedList<Pair<Future<?>, Consumer<?>>>();
        this.futureUnwrapper = new Thread(new ConsumerFeeder());
        // Daemon so that it will stop when the application stops.
        this.futureUnwrapper.setDaemon(true);
        this.futureUnwrapper.start();
    }

    /**
     * Creates the client database and connects it to the given backing
     * connection.
     *
     * @param connection the backing connection the client database will use to
     * get its data from.
     * @throws IllegalStateException if the client database has previously been
     * created.
     */
    public static synchronized void create(DatabaseConnection connection) throws IllegalStateException {
        if (instance != null) {
            throw new IllegalStateException("Client has already been created.");
        }
        instance = new ClientDatabase(connection);
    }

    /**
     * Returns the instance of the client database.
     *
     * @return the client database instance.
     * @throws IllegalStateException if the client database has not been
     * created by calling {@link #create(DatabaseConnection)} yet.
     */
    public static ClientDatabase get() throws IllegalStateException {
        if (instance == null) {
            throw new IllegalStateException("Client has not been created.");
        }
        return instance;
    }

    /**
     * Returns the currently logged in user.
     *
     * @return the currently logged in user.
     * @throws IllegalStateException if no user is logged in.
     */
    public User getCurrentUser() throws IllegalStateException {
        if (currentUser == null) {
            throw new IllegalStateException("No current user.");
        }
        return currentUser;
    }

    /**
     * Sets the currently selected account to the given account.
     *
     * @param account the account that is currently selected.
     * @throws IllegalArgumentException if the selected account is null.
     */
    public void setCurrentAccount(Account account) throws IllegalArgumentException {
        if (account == null) {
            throw new IllegalArgumentException("Cannot set current account as null.");
        }
        currentAccount = account;
    }

    /**
     * Clears the currently selected account.
     */
    public void clearCurrentAccount() {
        currentAccount = null;
        currentTransaction = null;
        transactionObservers.clear();
    }

    /**
     * Gets the currently selected account.
     *
     * @return the currently selected account.
     * @throws IllegalStateException if no account is currently selected.
     */
    public Account getCurrentAccount() throws IllegalStateException {
        if (currentAccount == null) {
            throw new IllegalStateException("No current account.");
        }
        return currentAccount;
    }

    /**
     * Sets the currently selected transaction to the given transaction.
     *
     * @param transaction the currently selected transaction.
     * @throws IllegalStateException if the selected transaction is null.
     */
    public void setCurrentTransaction(Transaction transaction) throws IllegalStateException {
        if (transaction == null) {
            throw new IllegalArgumentException("Cannot set current transaction as null.");
        }
        currentTransaction = transaction;
    }

    /**
     * Clears the currently selected transaction.
     */
    public void clearCurrentTransaction() {
        currentTransaction = null;
    }

    /**
     * Gets the currently selected transaction.
     *
     * @return the currently selected transaction.
     * @throws IllegalStateException if no transaction is currently selected.
     */
    public Transaction getCurrentTransaction() throws IllegalStateException {
        if (currentTransaction == null) {
            throw new IllegalStateException("No current transaction.");
        }
        return currentTransaction;
    }

    /**
     * Registers an observer on the set of accounts owned by the current user.
     * If no user is logged in this does nothing.
     *
     * @param observer the observer to register.
     */
    public void registerAccountsObserver(final DataSetObserver<Account> observer) {
        accountObservers.add(observer);

        try {
            addFutureConsumer(getAllAccounts(), new Consumer<Collection<Account>>() {
                @Override
                public void accept(Collection<Account> t) {
                    observer.update(t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers an observer on the set of transactions in the given account
     * owned by the current user. If no user is logged in or the given account
     * is not owned by the current user this does nothing.
     *
     * @param account the account whose transactions should be observed.
     * @param observer the observer to register.
     */
    public void registerTransactionsObserver(final Account account, final DataSetObserver<Transaction> observer) {
        Collection<DataSetObserver<Transaction>> observers = transactionObservers.get(account);
        if (observers == null) {
            observers = new ArrayList<DataSetObserver<Transaction>>();
            transactionObservers.put(account, observers);
        }
        observers.add(observer);

        try {
            addFutureConsumer(getAllTransactions(account), new Consumer<Collection<Transaction>>() {
                @Override
                public void accept(Collection<Transaction> t) {
                    observer.update(t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Future<Boolean> login(final User user) throws IOException {
        if (currentUser != null) {
            logout();
        }

        Future<Boolean> result = backingConnection.login(user);
        Consumer<Boolean> consumer = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean loggedIn) {
                // TODO: may need synchronization on a current user lock

                // currentUser may not be null at this point if some other
                // login request completed first. If so, don't overwrite.
                if (currentUser == null && loggedIn) {
                    currentUser = user;
                }
            }
        };

        addFutureConsumer(result, consumer);
        return result;
    }

    @Override
    public Future<Void> logout() throws IOException {
        Future<Void> result = backingConnection.logout();

        // Clear the current selections
        clearCurrentTransaction();
        clearCurrentAccount();
        currentUser = null;

        // Clear the cache
        transactions.clear();
        accounts = null;

        // Clear the observers
        transactionObservers.clear();
        accountObservers.clear();

        return result;
    }

    @Override
    public Future<Boolean> register(final User user) throws IOException {
        return backingConnection.register(user);
    }

    @Override
    public Future<Void> removeUser() throws IOException, IllegalStateException {
        Future<Void> result = backingConnection.removeUser();
        logout();
        return result;
    }

    @Override
    public synchronized Future<Collection<Account>> getAllAccounts() throws IOException, IllegalStateException {
        // If we already have a cache of the accounts, return that immediately.
        // Otherwise get the results from the backing connection. When the
        // result is retrieved, add the accounts to the cache.

        synchronized (accountLock) {
            if (accounts != null) {
                return new ImmediateFuture<Collection<Account>>(accounts);
            } else {
                Future<Collection<Account>> result = backingConnection.getAllAccounts();
                Consumer<Collection<Account>> consumer = new Consumer<Collection<Account>>() {
                    @Override
                    public void accept(Collection<Account> t) {
                        // The account cache may no longer be null at this
                        // point if some other request completed first. Most
                        // likely this other request was made after this one,
                        // so don't overwrite, but log it.
                        synchronized (accountLock) {
                            if (accounts != null) {
                                System.err.println(
                                    "Account cache no longer empty. Currently the cache holds\n"
                                    + accounts
                                    + "\nwhile this request contains\n" + t);
                            } else {
                                accounts = new ArrayList<Account>();
                                accounts.addAll(t);
                            }
                        }
                    }
                };

                addFutureConsumer(result, consumer);
                return result;
            }
        }
    }

    @Override
    public Future<Collection<Transaction>> getAllTransactions(final Account account) throws IOException, IllegalStateException {
        // If we already have a cache of the transactions, return that
        // immediately. Otherwise get the results from the backing connection.
        // When the result is retrieved, add the transactions to the cache.

        synchronized (transactions) {
            Collection<Transaction> accountTransactions = transactions.get(account);
            if (accountTransactions != null) {
                return new ImmediateFuture<Collection<Transaction>>(accountTransactions);
            } else {
                Future<Collection<Transaction>> result = backingConnection.getAllTransactions(account);
                Consumer<Collection<Transaction>> consumer = new Consumer<Collection<Transaction>>() {
                    @Override
                    public void accept(Collection<Transaction> t) {
                        // The transaction cache may no longer be null at this
                        // point if some other request completed first. Most
                        // likely this other request was made after this one,
                        // so don't overwrite, but log it.
                        synchronized (transactions) {
                            Collection<Transaction> accountTransactions = transactions.get(account);
                            if (accountTransactions != null) {
                                System.err.println(
                                    "Transaction cache no longer empty. Currently the cache holds\n"
                                    + accountTransactions
                                    + "\nwhile this request contains\n" + t);
                            } else {
                                accountTransactions = new ArrayList<Transaction>();
                                accountTransactions.addAll(t);
                                transactions.put(account, accountTransactions);
                            }
                        }
                    }
                };

                addFutureConsumer(result, consumer);
                return result;
            }
        }
    }

    @Override
    public Future<Void> addAccount(final Account account) throws IOException, IllegalStateException {
        Future<Void> result = backingConnection.addAccount(account);

        Runnable behavior = new Runnable() {
            @Override
            public void run() {
                synchronized (accountLock) {
                    try {
                        accounts.add(account);
                        updateAccountObservers();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        // If the accounts cache is null, get the accounts from the backing
        // connection first. Once that has completed, add the new account.

        synchronized (accountLock) {
            if (accounts == null) {
                addFutureConsumer(getAllAccounts(), new AccountsDependentBehavior(behavior));
            } else {
                behavior.run();
            }
        }

        return result;
    }

    @Override
    public Future<Void> addTransaction(final Account account, final Transaction transaction) throws IOException, IllegalStateException {
        Future<Void> result = backingConnection.addTransaction(account, transaction);

        Runnable behavior = new Runnable() {
            @Override
            public void run() {
                synchronized (transactions) {
                    try {
                        Collection<Transaction> accountTransactions = transactions.get(account);
                        accountTransactions.add(transaction);
                        updateTransactionObservers(account);

                        transaction.applyToAccount(account);
                        updateAccount(account, account);
                        updateAccountObservers();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        // If the transaction cache is null, get the transactions from the
        // backing connection first. Once that has completed, add the new
        // transaction.

        synchronized (transactions) {
            Collection<Transaction> accountTransactions = transactions.get(account);
            if (accountTransactions == null) {
                addFutureConsumer(getAllTransactions(account), new TransactionsDependentBehavior(account, behavior));
            } else {
                behavior.run();
            }
        }

        return result;
    }

    @Override
    public Future<Void> removeAccount(final Account account) throws IOException, IllegalStateException {
        Future<Void> result = backingConnection.removeAccount(account);

        Runnable behavior = new Runnable() {
            @Override
            public void run() {
                synchronized (accountLock) {
                    try {
                        accounts.remove(account);
                        transactions.remove(account);
                        updateAccountObservers();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        // If the accounts cache is null, get the accounts from the backing
        // connection first. Once that has completed, add the new account.

        synchronized (accountLock) {
            if (accounts == null) {
                addFutureConsumer(getAllAccounts(), new AccountsDependentBehavior(behavior));
            } else {
                behavior.run();
            }
        }

        return result;
    }

    @Override
    public Future<Void> removeTransaction(final Account account, final Transaction transaction) throws IOException, IllegalStateException {
        Future<Void> result = backingConnection.removeTransaction(account, transaction);

        Runnable behavior = new Runnable() {
            @Override
            public void run() {
                synchronized (transactions) {
                    try {
                        Collection<Transaction> accountTransactions = transactions.get(account);
                        accountTransactions.remove(transaction);
                        updateTransactionObservers(account);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        // If the transaction cache is null, get the transactions from the
        // backing connection first. Once that has completed, add the new
        // transaction.

        synchronized (transactions) {
            Collection<Transaction> accountTransactions = transactions.get(account);
            if (accountTransactions == null) {
                addFutureConsumer(getAllTransactions(account), new TransactionsDependentBehavior(account, behavior));
            } else {
                behavior.run();
            }
        }

        return result;
    }

    @Override
    public Future<Void> updateUser(final User nuw) throws IOException, IllegalStateException {
        Future<Void> result = backingConnection.updateUser(nuw);
        currentUser = nuw;
        return result;
    }

    @Override
    public Future<Void> updateAccount(final Account old, final Account nuw) throws IOException, IllegalStateException {
        Future<Void> result = backingConnection.updateAccount(old, nuw);

        Runnable behavior = new Runnable() {
            @Override
            public void run() {
                synchronized (accountLock) {
                    try {
                        accounts.remove(old);
                        accounts.add(nuw);
                        updateAccountObservers();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        synchronized (accountLock) {
            if (accounts == null) {
                addFutureConsumer(getAllAccounts(), new AccountsDependentBehavior(behavior));
            } else {
                behavior.run();
            }
        }

        return result;
    }

    @Override
    public Future<Void> updateTransaction(final Account account, final Transaction old, final Transaction nuw) throws IOException, IllegalStateException {
        Future<Void> result = backingConnection.updateTransaction(account, old, nuw);

        Runnable behavior = new Runnable() {
            @Override
            public void run() {
                synchronized (transactions) {
                    try {
                        Collection<Transaction> accountTransactions = transactions.get(account);
                        accountTransactions.remove(old);
                        accountTransactions.add(nuw);
                        updateTransactionObservers(account);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        synchronized (transactions) {
            Collection<Transaction> accountTransactions = transactions.get(account);
            if (accountTransactions == null) {
                addFutureConsumer(getAllTransactions(account), new TransactionsDependentBehavior(account, behavior));
            } else {
                behavior.run();
            }
        }

        return result;
    }

    @Override
    public Future<Report> accept(final Report report) throws IOException {
        return backingConnection.accept(report);
    }

    /**
     * Updates all account observers with the current set of accounts.
     * @throws IOException if some error occurs while accessing the database.
     * @throws IllegalStateException if no user is currently logged in.
     */
    private void updateAccountObservers() throws IllegalStateException, IOException {
        addFutureConsumer(getAllAccounts(), new Consumer<Collection<Account>>() {
            @Override
            public void accept(Collection<Account> t) {
                for (DataSetObserver<Account> observer: accountObservers) {
                    observer.update(t);
                }
            }
        });
    }

    /**
     * Updates all transaction observers on the given account with that
     * account's transactions.
     *
     * @param account the account whose transactions are being observed.
     * @throws IOException if some error occurs while accessing the database.
     * @throws IllegalStateException if no user is currently logged in or the
     * logged in user does not own the given account.
     */
    private void updateTransactionObservers(final Account account) throws IllegalStateException, IOException {
        addFutureConsumer(getAllTransactions(account), new Consumer<Collection<Transaction>>() {
            @Override
            public void accept(Collection<Transaction> t) {
                Collection<DataSetObserver<Transaction>> observers = transactionObservers.get(account);
                if (observers != null) {
                    for (DataSetObserver<Transaction> observer: observers) {
                        observer.update(t);
                    }
                }
            }
        });
    }

    /**
     * Adds a future and its associated consumer to {@link #futureConsumers}.
     *
     * @param future the future who's result the consumer will accept.
     * @param consumer the consumer which will accept the future's result.
     * @param <T> the kind of result the future should return and the parameter
     * the consumer accepts.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> void addFutureConsumer(Future<T> future, Consumer<T> consumer) {
        Pair futureConsumer = new Pair<Future<T>, Consumer<T>>(future, consumer);
        futureConsumers.add(futureConsumer);
    }

    /**
     * A consumer of the set of all accounts for the current user. This
     * consumer will cache these accounts if the cache is empty. Additionally,
     * it will execute some other behavior which is dependent on a cache of the
     * accounts.
     */
    private class AccountsDependentBehavior implements Consumer<Collection<Account>> {

        /**The behavior to execute once the accounts have been retrieved.*/
        private Runnable behavior;

        /**
         * Creates a new account dependent behavior with the given behavior.
         *
         * @param behavior the account dependent behavior. Must not be null.
         */
        private AccountsDependentBehavior(Runnable behavior) {
            this.behavior = behavior;
        }

        @Override
        public void accept(Collection<Account> t) {
            // The account cache should not be empty at this point.
            // If it is, add all the accounts and log the error.
            synchronized (accountLock) {
                if (accounts == null) {
                    System.err.println("Accounts cache null while adding account.");
                    accounts = new ArrayList<Account>();
                    accounts.addAll(t);
                }
            }

            behavior.run();
        }
    }

    /**
     * A consumer of the set of all transactions of an account for the current
     * user. This consumer will cache these transactions if the cache for the
     * account is empty. Additionally, it will execute some other behavior
     * which is dependent on a cache of the account's transactions.
     */
    private class TransactionsDependentBehavior implements Consumer<Collection<Transaction>> {

        /**The account to get transactions for.*/
        private Account account;
        /**The behavior to execute once the transactions have been retrieved.*/
        private Runnable behavior;

        /**
         * Creates a new transaction dependent behavior with the given
         * behavior.
         *
         * @param account the account who to get transactions for.
         * @param behavior the transaction dependent behavior. Must not be
         * null.
         */
        private TransactionsDependentBehavior(Account account, Runnable behavior) {
            this.account = account;
            this.behavior = behavior;
        }

        @Override
        public void accept(Collection<Transaction> t) {
            // The transaction cache should not be empty at this point.
            // If it is, add all the transactions and log the error.

            synchronized (transactions) {
                Collection<Transaction> accountTransactions = transactions.get(account);
                if (accountTransactions == null) {
                    System.err.println("Transactions cache null while adding transaction.");
                    accountTransactions = new ArrayList<Transaction>();
                    transactions.put(account, accountTransactions);
                    accountTransactions.addAll(t);
                }
            }

            behavior.run();
        }
    }

    /**
     * A runnable which feeds futures to their associated consumers. Intended
     * to be run from a separate thread.
     */
    private class ConsumerFeeder implements Runnable {

        /**Determines how long the thread running this should sleep between iterations.*/
        private static final long WAIT_PERIOD = 100;

        @Override
        public void run() {
            while (true) {
                int sizeNow = futureConsumers.size();
                int index = 0;

                while (index < sizeNow) {
                    Pair<Future<?>, Consumer<?>> futureConsumer = futureConsumers.get(index);
                    Future<?> future = futureConsumer.first;

                    if (future.isCancelled()) {
                        futureConsumers.remove(index);
                        sizeNow -= 1;
                    } else if (future.isDone()) {
                        try {
                            feed(futureConsumer);
                        } catch (InterruptedException e) {
                            // Shouldn't happen, as the result is already done.
                            // If it does happen however, just continue.
                        } catch (ExecutionException e) {
                            // TODO: This needs to be handled, but here is not
                            //       the appropriate place.
                            e.printStackTrace();
                        }

                        futureConsumers.remove(index);
                        sizeNow -= 1;
                    } else {
                        // Only increment if the future is not done (if it is
                        // done, it will be removed, so the index should be the same)
                        index += 1;
                    }
                }

                try {
                    Thread.sleep(WAIT_PERIOD);
                } catch (InterruptedException e) {
                    // Just continue with the next iteration.
                }
            }
        }

        /**
         * Feeds the result of a future to a matching consumer.
         *
         * @param futureConsumer a pair matching a future to its consumer.
         * @param <T> a generic parameter to allow casting from ? to Object.
         * @throws InterruptedException see {@link Future#get()}.
         * @throws ExecutionException see {@link Future#get()}.
         */
        @SuppressWarnings("unchecked")
        private <T> void feed(Pair<Future<?>, Consumer<?>> futureConsumer) throws InterruptedException, ExecutionException {
            Future<T> future = (Future<T>) futureConsumer.first;
            Consumer<T> consumer = (Consumer<T>) futureConsumer.second;
            consumer.accept(future.get());
        }
    }
}
