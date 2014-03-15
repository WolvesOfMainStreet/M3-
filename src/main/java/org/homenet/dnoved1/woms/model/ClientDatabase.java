package org.homenet.dnoved1.woms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.homenet.dnoved1.woms.model.report.Report;

/**
 * The client database for applications.
 */
public final class ClientDatabase implements DatabaseConnection {

    /**The current instance of the client database.*/
    private static ClientDatabase instance;

    /**
     * The backing connection that the client database uses to get its data.
     */
    private DatabaseConnection backingConnection;

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
    }

    /**
     * Creates the client database and connects it to the given backing
     * connection. The client database can be recreated any number of times.
     *
     * @param connection the backing connection the client database will use to
     * get its data from.
     */
    public static void create(DatabaseConnection connection) {
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
    public void registerAccountsObserver(DataSetObserver<Account> observer) {
        accountObservers.add(observer);
        observer.update(getAllAccounts());
    }

    /**
     * Registers an observer on the set of transactions in the given account
     * owned by the current user. If no user is logged in or the given account
     * is not owned by the current user this does nothing.
     *
     * @param account the account whose transactions should be observed.
     * @param observer the observer to register.
     */
    public void registerTransactionsObserver(Account account, DataSetObserver<Transaction> observer) {
        Collection<DataSetObserver<Transaction>> observers = transactionObservers.get(account);
        if (observers == null) {
            observers = new ArrayList<DataSetObserver<Transaction>>();
            transactionObservers.put(account, observers);
        }
        observers.add(observer);
        observer.update(getAllTransactions(account));
    }

    @Override
    public boolean login(User user) {
        if (currentUser != null) {
            logout();
        }

        boolean loggedIn = backingConnection.login(user);
        if (loggedIn) {
            currentUser = user;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void logout() {
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

        backingConnection.logout();
    }

    @Override
    public boolean register(User user) {
        return backingConnection.register(user);
    }

    @Override
    public void removeUser() throws IllegalStateException {
        checkUserLoggedIn();
        backingConnection.removeUser();
    }

    @Override
    public Collection<Account> getAllAccounts() throws IllegalStateException {
        checkUserLoggedIn();
        if (accounts == null) {
            accounts = backingConnection.getAllAccounts();
        }
        return accounts;
    }

    @Override
    public Collection<Transaction> getAllTransactions(Account account) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        checkUserOwnsAccount(account);

        Collection<Transaction> accountTransactions = transactions.get(account);
        if (accountTransactions == null) {
            accountTransactions = backingConnection.getAllTransactions(account);
            transactions.put(account, accountTransactions);
        }
        return accountTransactions;
    }

    @Override
    public void addAccount(Account account) throws IllegalStateException {
        checkUserLoggedIn();
        accounts.add(account);
        updateAccountObservers();
        backingConnection.addAccount(account);
    }

    @Override
    public void addTransaction(Account account, Transaction transaction) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        checkUserOwnsAccount(account);

        Collection<Transaction> accountTransactions = transactions.get(account);
        if (accountTransactions == null) {
            accountTransactions = new ArrayList<Transaction>();
            transactions.put(account, accountTransactions);
        }
        accountTransactions.add(transaction);
        updateTransactionObservers(account);
        transaction.applyToAccount(account);
        updateAccount(account, account);
        updateAccountObservers();
        backingConnection.addTransaction(account, transaction);
    }

    @Override
    public void removeAccount(Account account) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        checkUserOwnsAccount(account);

        accounts.remove(account);
        transactions.remove(account);
        updateAccountObservers();
        backingConnection.removeAccount(account);
    }

    @Override
    public void removeTransaction(Account account, Transaction transaction) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        checkUserOwnsAccount(account);
        checkAccountContainsTransaction(account, transaction);

        Collection<Transaction> accountTransactions = transactions.get(account);
        if (accountTransactions != null) {
            accountTransactions.remove(transaction);
        }
        updateTransactionObservers(account);
        backingConnection.removeTransaction(account, transaction);
    }

    @Override
    public void updateUser(User nuw) throws IllegalStateException {
        checkUserLoggedIn();
        currentUser = nuw;
        backingConnection.updateUser(nuw);
    }

    @Override
    public void updateAccount(Account old, Account nuw) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        checkUserOwnsAccount(old);

        accounts.remove(old);
        accounts.add(nuw);
        updateAccountObservers();
        backingConnection.updateAccount(old, nuw);
    }

    @Override
    public void updateTransaction(Account account, Transaction old, Transaction nuw) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        checkUserOwnsAccount(account);
        checkAccountContainsTransaction(account, old);

        Collection<Transaction> accountTransactions = transactions.get(account);
        if (accountTransactions != null) {
            accountTransactions.remove(old);
            accountTransactions.add(nuw);
        }
        updateTransactionObservers(account);
        backingConnection.updateTransaction(account, old, nuw);
    }

    @Override
    public Report accept(Report report) {
        return backingConnection.accept(report);
    }

    /**
     * Checks that a user is currently logged in.
     *
     * @throws IllegalStateException if no user is currently logged in.
     */
    private void checkUserLoggedIn() throws IllegalStateException {
        if (currentUser == null) {
            throw new IllegalStateException("No user currently logged in.");
        }
    }

    /**
     * Checks that the currently logged in user owns the given account.
     *
     * @param account the account to check for ownership of.
     * @throws IllegalArgumentException if the current user does not own the
     * given account.
     */
    private void checkUserOwnsAccount(Account account) throws IllegalArgumentException {
        if (!getAllAccounts().contains(account)) {
            throw new IllegalArgumentException("Currently logged in user does not own account " + account + '.');
        }
    }

    /**
     * Checks that the given account contains the given transaction.
     *
     * @param account the account to that is being checked for ownership of the
     * given transaction.
     * @param transaction the transaction to check for ownership of.
     * @throws IllegalArgumentException if the given account does not contain
     * the given transaction.
     */
    private void checkAccountContainsTransaction(Account account, Transaction transaction) throws IllegalArgumentException {
        if (!getAllTransactions(account).contains(transaction)) {
            throw new IllegalArgumentException("Account " + account + " does not contain transaction " + transaction + '.');
        }
    }

    /**
     * Updates all account observers with the current set of accounts.
     */
    private void updateAccountObservers() {
        for (DataSetObserver<Account> observer: accountObservers) {
            observer.update(getAllAccounts());
        }
    }

    /**
     * Updates all transaction observers on the given account with that
     * account's transactions.
     *
     * @param account the account whose transactions are being observed.
     */
    private void updateTransactionObservers(Account account) {
        Collection<DataSetObserver<Transaction>> observers = transactionObservers.get(account);
        if (observers != null) {
            for (DataSetObserver<Transaction> observer: observers) {
                observer.update(getAllTransactions(account));
            }
        }
    }
}
