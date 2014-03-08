package cs2340.woms.android.model;

import java.util.ArrayList;
import java.util.List;

import cs2340.woms.model.Account;
import cs2340.woms.model.DataSetObserver;
import cs2340.woms.model.LocalStorageModel;
import cs2340.woms.model.Transaction;
import cs2340.woms.model.User;
import cs2340.woms.model.report.Report;

/**
 * The android implementation of {@link cs2340.woms.model.LocalStorageModel}.
 * This model stores and retrieves all data from a local android SQLite
 * database.
 */
public class AndroidLocalStorageModel implements LocalStorageModel {

    private User currentUser;

    private List<DataSetObserver<Account>> accountObservers = new ArrayList<DataSetObserver<Account>>();
    private List<DataSetObserver<Transaction>> transactionObservers = new ArrayList<DataSetObserver<Transaction>>();

    @Override
    public boolean login(String username, String password) {
        User user = new User(username, password);

        if (currentUser != null) {
            logout();
        }

        if (AndroidLocalDatabase.getLocalDatabase().doesUserExist(user)) {
            currentUser = user;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void logout() {
        currentUser = null;
        accountObservers.clear();
        transactionObservers.clear();
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public boolean register(String username, String password) {
        return AndroidLocalDatabase.getLocalDatabase().addUser(new User(username, password));
    }

    @Override
    public void addAccount(Account account) {
        if (currentUser == null) {
            return;
        }

        AndroidLocalDatabase.getLocalDatabase().addAccount(currentUser, account);

        List<Account> accounts = AndroidLocalDatabase.getLocalDatabase().getAccounts(currentUser);
        for (DataSetObserver<Account> observer: accountObservers) {
            observer.update(accounts);
        }
    }

    @Override
    public void registerAccountsObserver(DataSetObserver<Account> observer) {
        accountObservers.add(observer);
        observer.update(AndroidLocalDatabase.getLocalDatabase().getAccounts(currentUser));
    }

    @Override
    public void addTransaction(Account account, Transaction transaction) {
        if (currentUser == null) {
            return;
        }

        AndroidLocalDatabase.getLocalDatabase().addTransaction(currentUser, account, transaction);

        List<Transaction> transactions = AndroidLocalDatabase.getLocalDatabase().getTransactions(currentUser, account);
        for (DataSetObserver<Transaction> observer: transactionObservers) {
            observer.update(transactions);
        }

        transaction.applyToAccount(account);
        AndroidLocalDatabase.getLocalDatabase().updateAccount(currentUser, account);
        List<Account> accounts = AndroidLocalDatabase.getLocalDatabase().getAccounts(currentUser);
        for (DataSetObserver<Account> observer: accountObservers) {
            observer.update(accounts);
        }
    }

    @Override
    public void registerTransactionsObserver(Account account, DataSetObserver<Transaction> observer) {
        transactionObservers.add(observer);
        observer.update(AndroidLocalDatabase.getLocalDatabase().getTransactions(currentUser, account));
    }

    @Override
    public void visit(Report report) {
        AndroidLocalDatabase database = AndroidLocalDatabase.getLocalDatabase();

        for (User user: database.getUsers()) {
            report.accept(user);

            for (Account account: database.getAccounts(user)) {
                report.accept(account);

                for (Transaction transaction: database.getTransactions(user, account)) {
                    report.accept(transaction);
                }
            }
        }
    }
}
