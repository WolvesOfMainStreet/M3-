package cs2340.woms.model;

import cs2340.woms.model.report.Report;

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
    void addAccount(Account account);

    /**
     * Registers an observer on the set of accounts owned by the current user.
     * If no user is logged in this does nothing.
     *
     * @param observer the observer to register.
     */
    void registerAccountsObserver(DataSetObserver<Account> observer);

    /**
     * Adds the given transaction to the given account owned by the current
     * user. If no user is logged in or the given account is not owned by the
     * current user this does nothing.
     *
     * @param account the account to add the transaction to.
     * @param transaction the transaction to add.
     */
    void addTransaction(Account account, Transaction transaction);

    /**
     * Registers an observer on the set of transactions in the given account
     * owned by the current user. If no user is logged in or the given account
     * is not owned by the current user this does nothing.
     *
     * @param account the account whose transactions should be observed.
     * @param observer the observer to register.
     */
    void registerTransactionsObserver(Account account, DataSetObserver<Transaction> observer);

    /**
     * Makes the given report visit every element of this model.
     *
     * @param report the report that will visit every element of this model.
     */
    void visit(Report report);

    // TODO: add method for selectively visiting elements. Not needed now, as
    // the database is small, however if it grows to any decent size this will
    // become unreasonable.
}
