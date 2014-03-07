package cs2340.woms.android.model;

import java.util.ArrayList;
import java.util.List;

import cs2340.woms.model.DataSetObserver;
import cs2340.woms.model.FinanceAccount;
import cs2340.woms.model.LocalStorageModel;
import cs2340.woms.model.LoginAccount;
import cs2340.woms.model.Transaction;

/**
 * The android implementation of {@link cs2340.woms.model.LocalStorageModel}.
 * This model stores and retrieves all data from a local android SQLite
 * database.
 */
public class AndroidLocalStorageModel implements LocalStorageModel {

    private LoginAccount currentUser;

    private List<DataSetObserver<FinanceAccount>> accountObservers = new ArrayList<DataSetObserver<FinanceAccount>>();
    private List<DataSetObserver<Transaction>> transactionObservers = new ArrayList<DataSetObserver<Transaction>>();

    @Override
    public boolean login(String username, String password) {
        LoginAccount user = new LoginAccount(username, password);

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
    public boolean register(String username, String password) {
        return AndroidLocalDatabase.getLocalDatabase().addUser(username, password);
    }

    @Override
    public void addAccount(FinanceAccount account) {
        if (currentUser == null) {
            return;
        }

        AndroidLocalDatabase.getLocalDatabase().addAccount(currentUser, account);

        List<FinanceAccount> accounts = AndroidLocalDatabase.getLocalDatabase().getAccounts(currentUser);
        for (DataSetObserver<FinanceAccount> observer: accountObservers) {
            observer.update(accounts);
        }
    }

    @Override
    public void registerAccountsObserver(DataSetObserver<FinanceAccount> observer) {
        accountObservers.add(observer);
        observer.update(AndroidLocalDatabase.getLocalDatabase().getAccounts(currentUser));
    }

    @Override
    public void addTransaction(FinanceAccount account, Transaction transaction) {
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
        List<FinanceAccount> accounts = AndroidLocalDatabase.getLocalDatabase().getAccounts(currentUser);
        for (DataSetObserver<FinanceAccount> observer: accountObservers) {
            observer.update(accounts);
        }
    }

    @Override
    public void registerTransactionsObserver(FinanceAccount account, DataSetObserver<Transaction> observer) {
        transactionObservers.add(observer);
        observer.update(AndroidLocalDatabase.getLocalDatabase().getTransactions(currentUser, account));
    }
}
