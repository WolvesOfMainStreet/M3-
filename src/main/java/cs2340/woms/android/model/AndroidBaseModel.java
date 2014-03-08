package cs2340.woms.android.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs2340.woms.model.BaseModel;
import cs2340.woms.model.DataSetObserver;
import cs2340.woms.model.Account;
import cs2340.woms.model.User;
import cs2340.woms.model.Transaction;
import cs2340.woms.model.report.Report;

/**
 * The android implementation of {@link cs2340.woms.model.BaseModel}. This
 * model stores all data only during runtime, meaning no saving or syncing
 * occurs.
 */
public class AndroidBaseModel implements BaseModel {

    private User currentUser;
    private Set<User> registeredUsers = new HashSet<User>();

    private Map<User, Set<Account>> accounts = new HashMap<User, Set<Account>>();
    private Map<Account, Set<Transaction>> transactions = new HashMap<Account, Set<Transaction>>();

    private List<DataSetObserver<Account>> accountObservers = new ArrayList<DataSetObserver<Account>>();
    private List<DataSetObserver<Transaction>> transactionObservers = new ArrayList<DataSetObserver<Transaction>>();

    @Override
    public boolean login(String username, String password) {
        if (currentUser != null) {
            logout();
        }

        for (User user: registeredUsers) {
            if (user.matches(username, password)) {
                currentUser = user;
                return true;
            }
        }

        return false;
    }

    @Override
    public void logout() {
        currentUser = null;
        accountObservers.clear();
        transactionObservers.clear();
    }

    @Override
    public boolean register(String username, String password) {
        return registeredUsers.add(new User(username, password));
    }

    @Override
    public void addAccount(Account account) {
        if (currentUser == null) {
            return;
        }

        Set<Account> userAccounts = accounts.get(currentUser);
        if (userAccounts == null) {
            userAccounts = new HashSet<Account>();
            accounts.put(currentUser, userAccounts);
        }

        userAccounts.add(account);

        for (DataSetObserver<Account> observer: accountObservers) {
            observer.update(userAccounts);
        }
    }

    @Override
    public void registerAccountsObserver(DataSetObserver<Account> observer) {
        if (currentUser == null) {
            return;
        }

        accountObservers.add(observer);

        Set<Account> userAccounts = accounts.get(currentUser);
        if (userAccounts == null) {
            userAccounts = Collections.emptySet();
        }

        observer.update(userAccounts);
    }

    @Override
    public void addTransaction(Account account, Transaction transaction) {
        if (currentUser == null) {
            return;
        }

        Set<Transaction> accountTransactions = transactions.get(account);
        if (accountTransactions == null) {
            accountTransactions = new HashSet<Transaction>();
            transactions.put(account, accountTransactions);
        }

        accountTransactions.add(transaction);

        for (DataSetObserver<Transaction> observer: transactionObservers) {
            observer.update(accountTransactions);
        }

        // Also update the account observers, since at least one account has changed
        // TODO: this can be improved by altering the model-account interactions
        transaction.applyToAccount(account);
        for (DataSetObserver<Account> observer: accountObservers) {
            observer.update(accounts.get(currentUser));
        }
    }

    @Override
    public void registerTransactionsObserver(Account account, DataSetObserver<Transaction> observer) {
        if (currentUser == null) {
            return;
        }

        // TODO: this implementation is incorrect. It notifies all transaction
        //observers, regardless of which account they belong to.
        transactionObservers.add(observer);

        Set<Transaction> accountTransactions = transactions.get(account);
        if (accountTransactions == null) {
            accountTransactions = Collections.emptySet();
        }

        observer.update(accountTransactions);
    }

    @Override
    public void visit(Report report) {
        for (User user: registeredUsers) {
            report.accept(user);

            Set<Account> accounts = this.accounts.get(user);
            if (accounts != null) {
                for (Account account: accounts) {
                    report.accept(account);

                    Set<Transaction> transactions = this.transactions.get(account);
                    if (transactions != null) {
                        for (Transaction transaction: transactions) {
                            report.accept(transaction);
                        }
                    }
                }
            }
        }
    }
}
