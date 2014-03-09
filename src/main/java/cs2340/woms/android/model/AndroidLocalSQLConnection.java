package cs2340.woms.android.model;

import java.util.Collection;

import cs2340.woms.model.Account;
import cs2340.woms.model.DatabaseConnection;
import cs2340.woms.model.Transaction;
import cs2340.woms.model.User;
import cs2340.woms.model.report.Report;

/**
 * A database connection to an android phone's local SQLite database.
 */
public class AndroidLocalSQLConnection implements DatabaseConnection {

    /**The currently logged in user.*/
    private User currentUser;

    @Override
    public boolean login(User user) {
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
    }

    @Override
    public boolean register(User user) {
        return AndroidLocalDatabase.getLocalDatabase().addUser(user);
    }

    @Override
    public void removeUser() throws IllegalStateException {
        checkUserLoggedIn();
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Account> getAllAccounts() throws IllegalStateException {
        checkUserLoggedIn();
        return AndroidLocalDatabase.getLocalDatabase().getAccounts(currentUser);
    }

    @Override
    public Collection<Transaction> getAllTransactions(Account account) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        // TODO check ownership of account
        return AndroidLocalDatabase.getLocalDatabase().getTransactions(currentUser, account);
    }

    @Override
    public void addAccount(Account account) throws IllegalStateException {
        checkUserLoggedIn();
        AndroidLocalDatabase.getLocalDatabase().addAccount(currentUser, account);
    }

    @Override
    public void addTransaction(Account account, Transaction transaction) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        // TODO check ownership of account
        AndroidLocalDatabase.getLocalDatabase().addTransaction(currentUser, account, transaction);
    }

    @Override
    public void removeAccount(Account account) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        // TODO check ownership of account
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeTransaction(Account account, Transaction transaction) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        // TODO check ownership of account and transaction
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateUser(User nuw) throws IllegalStateException {
        checkUserLoggedIn();
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateAccount(Account old, Account nuw) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        // TODO check ownership of account
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateTransaction(Account account, Transaction old, Transaction nuw) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();
        // TODO check ownership of account and transaction
        // TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public Report accept(Report report) {
        AndroidLocalDatabase db = AndroidLocalDatabase.getLocalDatabase();

        for (User user: db.getUsers()) {
            report.visit(user);

            for (Account account: db.getAccounts(user)) {
                report.visit(account);

                for (Transaction transaction: db.getTransactions(user, account)) {
                    report.visit(transaction);
                }
            }
        }

        return report;
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
}
