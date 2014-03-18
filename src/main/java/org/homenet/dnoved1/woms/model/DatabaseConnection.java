package org.homenet.dnoved1.woms.model;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Future;

import org.homenet.dnoved1.woms.model.report.Report;

/**
 * A connection to a database. Contains methods for adding, updating, and
 * removing certain elements of the database.
 */
public interface DatabaseConnection {

    /**
     * Attempts to login the given user.
     *
     * @param user the user to login.
     * @return a future that returns true if the user was successfully logged
     * in and false if they could not be.
     * @throws IOException if some error occurs while while accessing the
     * database.
     */
    Future<Boolean> login(User user)
        throws IOException;

    /**
     * Logs out the currently logged in user, if any.
     *
     * @return a future that can be checked to determine when a user has been
     * successfully logged out.
     * @throws IOException if some error occurs while while accessing the
     * database.
     */
    Future<Void> logout()
        throws IOException;

    /**
     * Attempts to register the given user.
     *
     * @param user the user to register.
     * @return a future that returns true if the user was successfully
     * registered and false if they were not.
     * @throws IOException if some error occurs while while accessing the
     * database.
     */
    Future<Boolean> register(User user)
        throws IOException;

    /**
     * Removes the currently logged in user from the database.
     *
     * @return a future that can be checked to determine when a user has been
     * successfully removed.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in.
     */
    Future<Void> removeUser()
        throws IOException, IllegalStateException;

    /**
     * Returns all of the currently logged in user's accounts.
     *
     * @return a future that returns the currently logged in user's accounts.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in.
     */
    Future<Collection<Account>> getAllAccounts()
        throws IOException, IllegalStateException;

    /**
     * Returns all of the currently logged in user's transactions for the given
     * account.
     *
     * @param account the account to get transactions for.
     * @return a future that returns all of the given account's transactions.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in or the
     * logged in user does not own the given account.
     */
    Future<Collection<Transaction>> getAllTransactions(Account account)
        throws IOException, IllegalStateException;

    /**
     * Adds the given account to the currently logged in user's accounts.
     *
     * @param account the account to add.
     * @return a future that can be checked to determine when the account has
     * been successfully added.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in.
     */
    Future<Void> addAccount(Account account)
        throws IOException, IllegalStateException;

    /**
     * Adds the given transaction to the given account belonging to the
     * currently logged in user.
     *
     * @param account the account to add the transaction to.
     * @param transaction the transaction to add.
     * @return a future that can be checked to determine when the transaction
     * has been successfully added.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in or the
     * logged in user does not own the given account.
     */
    Future<Void> addTransaction(Account account, Transaction transaction)
        throws IOException, IllegalStateException;

    /**
     * Removes the given account from the currently logged in user's accounts.
     *
     * @param account the account to remove.
     * @return a future that can be checked to determine when the account has
     * been successfully removed.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in or the
     * logged in user does not own the given account.
     */
    Future<Void> removeAccount(Account account)
        throws IOException, IllegalStateException;

    /**
     * Removes the given transaction from the given account belonging to the
     * currently logged in user.
     *
     * @param account the account to remove the transaction from.
     * @param transaction the transaction to add.
     * @return a future that can be checked to determine when the transaction
     * has been successfully removed.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in, the
     * logged in user does not own the given account, or the given account does
     * not have the given transaction.
     */
    Future<Void> removeTransaction(Account account, Transaction transaction)
        throws IOException, IllegalStateException;

    /**
     * Updates the currently logged in user's information to match the given
     * user.
     *
     * @param nuw a user object containing the information the current user
     * should match.
     * @return a future that can be checked to determine when the user has
     * been successfully updated.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in.
     */
    Future<Void> updateUser(User nuw)
        throws IOException, IllegalStateException;

    /**
     * Updates the old account's information to match the new account's
     * information. The old account must belong to the currently logged in
     * user.
     *
     * @param old the old account to update.
     * @param nuw an account object containing the new information for the
     * account.
     * @return a future that can be checked to determine when the account has
     * been successfully updated.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in or the
     * logged in user does not own the given account.
     */
    Future<Void> updateAccount(Account old, Account nuw)
        throws IOException, IllegalStateException;

    /**
     * Updates the old transactions' information to match the new transactions'
     * information. The old transaction must belong to the given account, which
     * also must belong to the currently logged in user.
     *
     * @param account the account containing the old transaction.
     * @param old the old transaction to update.
     * @param nuw a transaction object containing the new information for the
     * transaction.
     * @return a future that can be checked to determine when the transaction
     * has been successfully updated.
     * @throws IOException if some error occurs while while accessing the
     * database.
     * @throws IllegalStateException if no user is currently logged in, the
     * logged in user does not own the given account, or the given account does
     * not have the given transaction.
     */
    Future<Void> updateTransaction(Account account, Transaction old, Transaction nuw)
        throws IOException, IllegalStateException;

    /**
     * Makes the given report visit every element of this database.
     *
     * @param report the report to accept.
     * @return a future that returns the report after it has visited every
     * element.
     * @throws IOException if some error occurs while while accessing the
     * database.
     */
    Future<Report> accept(Report report)
        throws IOException;
}
