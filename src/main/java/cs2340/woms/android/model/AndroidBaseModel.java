package cs2340.woms.android.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cs2340.woms.account.FinanceAccount;
import cs2340.woms.account.LoginAccount;
import cs2340.woms.account.Transaction;
import cs2340.woms.model.BaseModel;
import cs2340.woms.model.DataSetObserver;

/**
 * The android implementation of {@link cs2340.woms.model.BaseModel}.
 */
public class AndroidBaseModel implements BaseModel {

    private LoginAccount currentUser;
    private Set<LoginAccount> registeredUsers = new HashSet<LoginAccount>();

    private List<DataSetObserver<FinanceAccount>> accountObservers = new ArrayList<DataSetObserver<FinanceAccount>>();
    private List<DataSetObserver<Transaction>> transactionObservers = new ArrayList<DataSetObserver<Transaction>>();

    @Override
    public boolean login(String username, String password) {
        if (currentUser != null) {
            logout();
        }

        for (LoginAccount user: registeredUsers) {
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
        return registeredUsers.add(new LoginAccount(username, password));
    }

    @Override
    public void addAccount(FinanceAccount account) {
        if (currentUser == null) {
            return;
        }

        currentUser.addAccount(account);

        for (DataSetObserver<FinanceAccount> observer: accountObservers) {
            observer.update(currentUser.getAccounts());
        }
    }

    @Override
    public void registerAccountsObserver(DataSetObserver<FinanceAccount> observer) {
        if (currentUser == null) {
            return;
        }

        accountObservers.add(observer);
    }

    @Override
    public void addTransaction(FinanceAccount account, Transaction transaction) {
        if (currentUser == null) {
            return;
        }

        account.addTransaction(transaction);

        for (DataSetObserver<Transaction> observer: transactionObservers) {
            observer.update(account.getTransactions());
        }
    }

    @Override
    public void registerTransactionsObserver(FinanceAccount account, DataSetObserver<Transaction> observer) {
        if (currentUser == null) {
            return;
        }

        // TODO: this implementation is incorrect. It notifies all transaction
        //observers, regardless of which account they belong to.
        transactionObservers.add(observer);
    }
}
