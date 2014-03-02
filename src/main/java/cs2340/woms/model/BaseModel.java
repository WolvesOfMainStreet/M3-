package cs2340.woms.model;

import cs2340.woms.account.FinanceAccount;
import cs2340.woms.account.Transaction;

/**
 * The base model interface. All models should implement this.
 */
public interface BaseModel {

    /**
     * Returns whether authentication using the given credentials was
     * successful.
     *
     * @param username the username of the account to authenticate.
     * @param password the password of the account to authenticate.
     * @return true if authentication was successful, false if not.
     */
    boolean login(String username, String password);

    /**
     * Logs out the currently logged in user, if any.
     */
    void logout();

    /**
     * Attempts to register a new account, and returns whether this was
     * successful or not.
     *
     * @param username the username of the new account.
     * @param password the password of the new account.
     * @return true if the account was successfully registered, false if not.
     */
    boolean register(String username, String password);

    /**
     * Adds the given account to the currently logged in user. If no user is
     * logged in this does nothing.
     *
     * @param account the account to add.
     */
    void addAccount(FinanceAccount account);

    /**
     * Registers an observer on the set of accounts owned by the current user.
     * If no user is logged in this does nothing.
     *
     * @param observer the observer to register.
     */
    void registerAccountsObserver(DataSetObserver<FinanceAccount> observer);

    /**
     * Adds the given transaction to the given account owned by the current
     * user. If no user is logged in or the given account is not owned by the
     * current user this does nothing.
     *
     * @param account the account to add the transaction to.
     * @param transaction the transaction to add.
     */
    void AddTransaction(FinanceAccount account, Transaction transaction);

    /**
     * Registers an observer on the set of transactions in the given account
     * owned by the current user. If no user is logged in or the given account
     * is not owned by the current user this does nothing.
     *
     * @param account the account whose transactions should be observed.
     * @param observer the observer to register.
     */
    void registerTransactionsObserver(FinanceAccount account, DataSetObserver<Transaction> observer);
}
