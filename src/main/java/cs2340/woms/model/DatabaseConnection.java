package cs2340.woms.model;

import java.util.Collection;

import cs2340.woms.model.report.Report;

/**
 * A connection to a database. Contains methods for adding, updating, and
 * removing certain elements of the database.
 */
public interface DatabaseConnection {

    /**
     * Attempts to login the given user, returning whether this was successful.
     *
     * @param user the user to login.
     * @return true if the user was successfully logged in, false if they could
     * not be.
     */
    boolean login(User user);

    /**
     * Logs out the currently logged in user, if any.
     */
    void logout();

    /**
     * Attempts to register the given user. Returns whether this was
     * successful.
     *
     * @param user the user to register.
     * @return true if the user was successfully registered, false if not.
     */
    boolean register(User user);

    /**
     * Removes the currently logged in user from the database.
     *
     * @throws IllegalStateException if no user is currently logged in.
     */
    void removeUser()
            throws IllegalStateException;

    /**
     * Returns all of the currently logged in user's accounts.
     *
     * @return the currently logged in user's accounts.
     * @throws IllegalStateException if no user is currently logged in.
     */
    Collection<Account> getAllAccounts()
            throws IllegalStateException;

    /**
     * Returns all of the currently logged in user's transactions for the given
     * account.
     *
     * @param account the account to get transactions for.
     * @return all of the given account's transactions.
     * @throws IllegalStateException if no user is currently logged in.
     * @throws IllegalArgumentException if the current user does not own the
     * given account.
     */
    Collection<Transaction> getAllTransactions(Account account)
            throws IllegalStateException, IllegalArgumentException;

    /**
     * Adds the given account to the currently logged in user's accounts.
     *
     * @param account the account to add.
     * @throws IllegalStateException if no user is currently logged in.
     */
    void addAccount(Account account)
            throws IllegalStateException;

    /**
     * Adds the given transaction to the given account belonging to the
     * currently logged in user.
     *
     * @param account the account to add the transaction to.
     * @param transaction the transaction to add.
     * @throws IllegalStateException if no user is currently logged in.
     * @throws IllegalArgumentException if the current user does not own the
     * given account.
     */
    void addTransaction(Account account, Transaction transaction)
            throws IllegalStateException, IllegalArgumentException;

    /**
     * Removes the given account from the currently logged in user's accounts.
     *
     * @param account the account to remove.
     * @throws IllegalStateException if no user is currently logged in.
     * @throws IllegalArgumentException if the current user does not own the
     * given account.
     */
    void removeAccount(Account account)
            throws IllegalStateException, IllegalArgumentException;

    /**
     * Removes the given transaction from the given account belonging to the
     * currently logged in user.
     *
     * @param account the account to remove the transaction from.
     * @param transaction the transaction to add.
     * @throws IllegalStateException if no user is currently logged in.
     * @throws IllegalArgumentException if the current user does not own the
     * given account or the given account does not have the given transaction.
     */
    void removeTransaction(Account account, Transaction transaction)
            throws IllegalStateException, IllegalArgumentException;

    /**
     * Updates the currently logged in user's information to match the given
     * user.
     *
     * @param nuw a user object containing the information the current user
     * should match.
     * @throws IllegalStateException if no user is currently logged in.
     */
    void updateUser(User nuw)
            throws IllegalStateException;

    /**
     * Updates the old account's information to match the new account's
     * information. The old account must belong to the currently logged in
     * user.
     *
     * @param old the old account to update.
     * @param nuw an account object containing the new information for the
     * account.
     * @throws IllegalStateException if no user is currently logged in.
     * @throws IllegalArgumentException if the current user does not own the
     * given account.
     */
    void updateAccount(Account old, Account nuw)
            throws IllegalStateException, IllegalArgumentException;

    /**
     * Updates the old transactions' information to match the new transactions'
     * information. The old transaction must belong to the given account, which
     * also must belong to the currently logged in user.
     *
     * @param account the account containing the old transaction.
     * @param old the old transaction to update.
     * @param nuw a transaction object containing the new information for the
     * transaction.
     * @throws IllegalStateException if no user is currently logged in.
     * @throws IllegalArgumentException if the current user does not own the
     * given account or the given account does not have the given transaction.
     */
    void updateTransaction(Account account, Transaction old, Transaction nuw)
            throws IllegalStateException, IllegalArgumentException;

    /**
     * Makes the given report visit every element of this database.
     *
     * @param report the report to accept.
     * @return the report after it has visited every element.
     */
    Report accept(Report report);
}
