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
import cs2340.woms.model.FinanceAccount;
import cs2340.woms.model.LoginAccount;
import cs2340.woms.model.Transaction;

/**
 * The android implementation of {@link cs2340.woms.model.BaseModel}. This
 * model stores all data only during runtime, meaning no saving or syncing
 * occurs.
 */
public class AndroidBaseModel implements BaseModel {

    private LoginAccount currentUser;
    private Set<LoginAccount> registeredUsers = new HashSet<LoginAccount>();

    private Map<LoginAccount, Set<FinanceAccount>> accounts = new HashMap<LoginAccount, Set<FinanceAccount>>();
    private Map<FinanceAccount, Set<Transaction>> transactions = new HashMap<FinanceAccount, Set<Transaction>>();

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

        Set<FinanceAccount> userAccounts = accounts.get(currentUser);
        if (userAccounts == null) {
            userAccounts = new HashSet<FinanceAccount>();
            accounts.put(currentUser, userAccounts);
        }

        userAccounts.add(account);

        for (DataSetObserver<FinanceAccount> observer: accountObservers) {
            observer.update(userAccounts);
        }
    }

    @Override
    public void registerAccountsObserver(DataSetObserver<FinanceAccount> observer) {
        if (currentUser == null) {
            return;
        }

        accountObservers.add(observer);

        Set<FinanceAccount> userAccounts = accounts.get(currentUser);
        if (userAccounts == null) {
            userAccounts = Collections.emptySet();
        }

        observer.update(userAccounts);
    }

    @Override
    public void addTransaction(FinanceAccount account, Transaction transaction) {
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
        account.adjustBalance(transaction.getAmount());
        for (DataSetObserver<FinanceAccount> observer: accountObservers) {
            observer.update(accounts.get(currentUser));
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

        Set<Transaction> accountTransactions = transactions.get(account);
        if (accountTransactions == null) {
            accountTransactions = Collections.emptySet();
        }

        observer.update(accountTransactions);
    }
}
