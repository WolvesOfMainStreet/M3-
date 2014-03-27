package org.homenet.dnoved1.woms.android.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.homenet.dnoved1.woms.model.Account;
import org.homenet.dnoved1.woms.model.DatabaseConnection;
import org.homenet.dnoved1.woms.model.Deposit;
import org.homenet.dnoved1.woms.model.ExpenseCategory;
import org.homenet.dnoved1.woms.model.Transaction;
import org.homenet.dnoved1.woms.model.User;
import org.homenet.dnoved1.woms.model.Withdrawal;
import org.homenet.dnoved1.woms.model.report.Report;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A database connection to an android phone's local SQLite database.
 */
public class AndroidLocalSQLConnection implements DatabaseConnection {

    /**The current local sqlite database version.*/
    private static final int DATABASE_VERSION = 5;
    /**The name of the local sqlite database.*/
    private static final String DATABASE_NAME = "womsMainDB";

    //-----SQL Names------------------------------------------------------------

    /**A table for storing {@link User} objects.*/
    private static final String TABLE_USER = "users";
    /**A table for storing {@link Account} objects.*/
    private static final String TABLE_ACCOUNT = "accounts";
    /**A table for storing {@link TRANSACTION} objects.*/
    private static final String TABLE_TRANSACTION = "transactions";

    /**The primary key for {@link #TABLE_USER}.*/
    private static final String KEY_USER_PK = "user_id";
    /**A key for {@link #TABLE_USER} for the username of the user.*/
    private static final String KEY_USER_USERNAME = "username";
    /**A key for {@link #TABLE_USER} for the password of the user.*/
    private static final String KEY_USER_PASSWORD = "password";

    /**The primary key for {@link #TABLE_ACCOUNT}.*/
    private static final String KEY_ACCOUNT_PK = "account_id";
    /**A key for {@link #TABLE_ACCOUNT} for the name of the account.*/
    private static final String KEY_ACCOUNT_NAME = "name";
    /**A key for {@link #TABLE_ACCOUNT} for the balance of the account.*/
    private static final String KEY_ACCOUNT_BALANCE = "balance";

    /**The primary key for {@link #TABLE_TRANSACTION}.*/
    private static final String KEY_TRANSACTION_PK = "transaction_id";
    /**A key for {@link #TABLE_TRANSACTION} for the amount of the transaction.*/
    private static final String KEY_TRANSACTION_AMOUNT = "amount";
    /**A key for {@link #TABLE_TRANSACTION} for the time the transaction was entered.*/
    private static final String KEY_TRANSACTION_TIME_ENTERED = "time_entered";
    /**A key for {@link #TABLE_TRANSACTION} for the time the transaction becomes effective.*/
    private static final String KEY_TRANSACTION_TIME_EFFECTIVE = "time_effective";
    /**A key for {@link #TABLE_TRANSACTION} for the type of transaction.*/
    private static final String KEY_TRANSACTION_TYPE = "type";
    /**A key for deposits in {@link #TABLE_TRANSACTION} for the source of the deposit.*/
    private static final String KEY_TRANSACTION_DEPOSIT_SOURCE = "deposit_source";
    /**A key for withdrawals in {@link #TABLE_TRANSACTION} for the reason for the withdrawal.*/
    private static final String KEY_TRANSACTION_WITHDRAWAL_REASON = "withdrawal_reason";
    /**A key for withdrawals in {@link #TABLE_TRANSACTION} for the category of the withdrawal.*/
    private static final String KEY_TRANSACTION_WITHDRAWAL_CATEGORY = "withdrawal_category";

    /**The deposit transaction type. See {@link #KEY_TRANSACTION_TYPE}.*/
    private static final String TRANSACTION_TYPE_DEPOSIT = "type_deposit";
    /**The withdrawal transaction type. See {@link #KEY_TRANSACTION_TYPE}.*/
    private static final String TRANSACTION_TYPE_WITHDRAWAL = "type_withdrawal";

    //-----SQL Table Structure--------------------------------------------------
    // CHECKSTYLE:OFF Turn off checkstyle for SQL commands due to MultipleStringLiterals

    /**A sql command for creating the user table.*/
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + '('
                + KEY_USER_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_USERNAME + " TEXT UNIQUE NOT NULL,"
                + KEY_USER_PASSWORD + " TEXT NOT NULL"
            + ')';

    /**A sql command for creating the account table.*/
    private static final String CREATE_TABLE_ACCOUNT =
            "CREATE TABLE " + TABLE_ACCOUNT + '('
                + KEY_ACCOUNT_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_PK + " INTEGER,"
                + KEY_ACCOUNT_NAME + " TEXT NOT NULL,"
                + KEY_ACCOUNT_BALANCE + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + KEY_USER_PK + ") REFERENCES " + TABLE_USER + '(' + KEY_USER_PK + ')'
            + ')';

    /**A sql command for creating the transaction table.*/
    private static final String CREATE_TABLE_TRANSACTION =
            "CREATE TABLE " + TABLE_TRANSACTION + '('
                + KEY_TRANSACTION_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ACCOUNT_PK + " INTEGER,"
                + KEY_TRANSACTION_AMOUNT + " TEXT NOT NULL,"
                + KEY_TRANSACTION_TIME_ENTERED + " INTEGER NOT NULL,"
                + KEY_TRANSACTION_TIME_EFFECTIVE + " INTEGER NOT NULL,"
                + KEY_TRANSACTION_TYPE + " TEXT NOT NULL,"
                + KEY_TRANSACTION_DEPOSIT_SOURCE + " TEXT,"
                + KEY_TRANSACTION_WITHDRAWAL_REASON + " TEXT,"
                + KEY_TRANSACTION_WITHDRAWAL_CATEGORY + " TEXT,"
                + "FOREIGN KEY (" + KEY_ACCOUNT_PK + ") REFERENCES " + TABLE_ACCOUNT + '(' + KEY_ACCOUNT_PK + ')'
            + ')';

    //-----SQL Commands---------------------------------------------------------

    /**A format string for a where clause for a single integer column.*/
    private static final String WHERE_INT = "%s=%d";
    /**A format string for a where clause for a single string column.*/
    private static final String WHERE_STRING = "%s='%s'";
    /**A format string for a where clause for two integer columns.*/
    private static final String WHERE_DOUBLE_INT = "%s=%d AND %s=%d";
    /**A format string for a where clause for two string columns.*/
    private static final String WHERE_DOUBLE_STRING = "%s='%s' AND %s='%s'";
    /**A format string for a where clause for one integer column and one string column.*/
    private static final String WHERE_INT_STRING = "%s=%d AND %s='%s'";
    /**A format string for a drop table command.*/
    private static final String DROP_TABLE = "DROP TABLE %s";

    //CHECKSTYLE:ON End of SQL Commands

    /**The instance of the sqlite database helper object.*/
    private LocalSQLiteDatabase sqlitedb;

    /**The currently logged in user.*/
    private User currentUser;

    /**
     * Creates a new local sqlite database connection.
     *
     * @param context the application context.
     */
    public AndroidLocalSQLConnection(Context context) {
        sqlitedb = new LocalSQLiteDatabase(context);
    }

    @Override
    public boolean login(User user) {
        if (currentUser != null) {
            logout();
        }

        // Select any users whose username and password exactly match the given
        // user's username and password.
        Object[] whereArgs = new Object[] {
            KEY_USER_USERNAME, user.getUsername(),
            KEY_USER_PASSWORD, user.getPassword()
        };

        SQLiteDatabase db = sqlitedb.getReadableDatabase();
        Cursor c = db.query(TABLE_USER, new String[] {KEY_USER_PK}, String.format(WHERE_DOUBLE_STRING, whereArgs), null, null, null, null);
        boolean loggedIn = c.getCount() > 0;

        if (loggedIn) {
            currentUser = user;
        }

        return loggedIn;
    }

    @Override
    public void logout() {
        currentUser = null;
    }

    @Override
    public boolean register(User user) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_USERNAME, user.getUsername());
        cv.put(KEY_USER_PASSWORD, user.getPassword());

        SQLiteDatabase db = sqlitedb.getWritableDatabase();
        long rowID = db.insert(TABLE_USER, null, cv);
        return rowID != -1;
    }

    @Override
    public void removeUser() throws IllegalStateException {
        checkUserLoggedIn();

        // First delete all the user's accounts
        for (Account account: getAllAccounts()) {
            removeAccount(account);
        }

        Object[] whereArgs = new Object[] {
            KEY_USER_PK, currentUser.getUsername()
        };

        SQLiteDatabase db = sqlitedb.getWritableDatabase();
        db.delete(TABLE_USER, String.format(WHERE_STRING, whereArgs), null);
        logout();
    }

    @Override
    public Collection<Account> getAllAccounts() throws IllegalStateException {
        checkUserLoggedIn();

        int userID = getUserID(currentUser);
        if (userID == -1) {
            return Collections.emptyList();
        }

        Object[] whereArgs = new Object[] {
            KEY_USER_PK, userID
        };

        SQLiteDatabase db = sqlitedb.getReadableDatabase();
        Cursor c = db.query(TABLE_ACCOUNT, null, String.format(WHERE_INT, whereArgs), null, null, null, null);

        Collection<Account> accounts = new ArrayList<Account>();
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();

            try {
                String name = c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME));
                BigDecimal balance = new BigDecimal(c.getString(c.getColumnIndex(KEY_ACCOUNT_BALANCE)));

                accounts.add(new Account(name, balance));
            } catch (NumberFormatException e) { // From de-serializing balance
                e.printStackTrace();
            }
        }

        return accounts;
    }

    @Override
    public Collection<Transaction> getAllTransactions(Account account) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();

        int accountID = getAccountID(currentUser, account);
        if (accountID == -1) {
            return Collections.emptyList();
        }

        Object[] selectionArgs = new Object[] {
            KEY_ACCOUNT_PK, accountID
        };

        SQLiteDatabase db = sqlitedb.getReadableDatabase();
        Cursor c = db.query(TABLE_TRANSACTION, null, String.format(WHERE_INT, selectionArgs), null, null, null, null);

        Collection<Transaction> transactions = new ArrayList<Transaction>();
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();
            Transaction transaction;

            try {
                BigDecimal amount = new BigDecimal(c.getString(c.getColumnIndex(KEY_TRANSACTION_AMOUNT)));
                Date timeEntered = new Date(c.getLong(c.getColumnIndex(KEY_TRANSACTION_TIME_ENTERED)));
                Date timeEffective = new Date(c.getLong(c.getColumnIndex(KEY_TRANSACTION_TIME_EFFECTIVE)));
                String type = c.getString(c.getColumnIndex(KEY_TRANSACTION_TYPE));

                if (type.equals(TRANSACTION_TYPE_DEPOSIT)) {
                    String source = c.getString(c.getColumnIndex(KEY_TRANSACTION_DEPOSIT_SOURCE));

                    transaction = new Deposit(source, amount, timeEntered, timeEffective);
                } else if (type.equals(TRANSACTION_TYPE_WITHDRAWAL)) {
                    String reason = c.getString(c.getColumnIndex(KEY_TRANSACTION_WITHDRAWAL_REASON));
                    ExpenseCategory category = ExpenseCategory.valueOf(c.getString(c.getColumnIndex(KEY_TRANSACTION_WITHDRAWAL_CATEGORY)));

                    transaction = new Withdrawal(category, reason, amount, timeEntered, timeEffective);
                } else {
                    System.err.println("Unknown transaction type " + type + '.');
                    transaction = null;
                }
            } catch (NumberFormatException e) { // When de-serializing amount
                e.printStackTrace();
                transaction = null;
            } catch (IllegalArgumentException e) { // When de-serializing expense category
                e.printStackTrace();
                transaction = null;
            }

            if (transaction != null) {
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    @Override
    public void addAccount(Account account) throws IllegalStateException {
        checkUserLoggedIn();

        int userID = getUserID(currentUser);
        if (userID == -1) {
            return;
        }

        String name = account.getName();
        String balance = account.getBalance().toPlainString();

        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_PK, userID);
        cv.put(KEY_ACCOUNT_NAME, name);
        cv.put(KEY_ACCOUNT_BALANCE, balance);

        SQLiteDatabase db = sqlitedb.getWritableDatabase();
        db.insert(TABLE_ACCOUNT, null, cv);
    }

    @Override
    public void addTransaction(Account account, Transaction transaction) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();

        int accountID = getAccountID(currentUser, account);
        if (accountID == -1) {
            throw new IllegalArgumentException();
        }

        // Get the serializable data for the transaction
        String amount = transaction.getAmount().toPlainString();
        long timeEntered = transaction.getTimeEntered().getTime();
        long timeEffective = transaction.getTimeEffective().getTime();

        //-----Write all data to sql tables-------------------------------------
        ContentValues cv = new ContentValues();
        cv.put(KEY_ACCOUNT_PK, accountID);
        cv.put(KEY_TRANSACTION_AMOUNT, amount);
        cv.put(KEY_TRANSACTION_TIME_ENTERED, timeEntered);
        cv.put(KEY_TRANSACTION_TIME_EFFECTIVE, timeEffective);

        boolean shouldInsert = true;
        if (transaction instanceof Deposit) {
            Deposit deposit = (Deposit) transaction;

            String type = TRANSACTION_TYPE_DEPOSIT;
            String source = deposit.getSource();

            cv.put(KEY_TRANSACTION_TYPE, type);
            cv.put(KEY_TRANSACTION_DEPOSIT_SOURCE, source);
        } else if (transaction instanceof Withdrawal) {
            Withdrawal withdrawal = (Withdrawal) transaction;

            String type = TRANSACTION_TYPE_WITHDRAWAL;
            String reason = withdrawal.getReason();
            String category = withdrawal.getExpenseCategory().name();

            cv.put(KEY_TRANSACTION_TYPE, type);
            cv.put(KEY_TRANSACTION_WITHDRAWAL_REASON, reason);
            cv.put(KEY_TRANSACTION_WITHDRAWAL_CATEGORY, category);
        } else {
            shouldInsert = false;
            System.err.println("Unknown transaction class " + transaction.getClass().getName() + '.');
        }

        if (shouldInsert) {
            SQLiteDatabase db = sqlitedb.getWritableDatabase();
            db.insert(TABLE_TRANSACTION, null, cv);
        }
    }

    @Override
    public void removeAccount(Account account) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();

        int userID = getUserID(currentUser);
        if (userID == -1) {
            throw new IllegalArgumentException();
        }

        // First delete all transactions
        for (Transaction transaction: getAllTransactions(account)) {
            removeTransaction(account, transaction);
        }

        Object[] whereArgs = new Object[] {
            KEY_USER_PK, userID,
            KEY_ACCOUNT_NAME, account.getName()};

        SQLiteDatabase db = sqlitedb.getWritableDatabase();
        db.delete(TABLE_ACCOUNT, String.format(WHERE_INT_STRING, whereArgs), null);
    }

    @Override
    public void removeTransaction(Account account, Transaction transaction) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();

        int accountID = getAccountID(currentUser, account);
        if (accountID == -1) {
            throw new IllegalArgumentException();
        }

        Object[] whereArgs = new Object[] {
            KEY_ACCOUNT_PK, accountID,
            KEY_TRANSACTION_TIME_ENTERED, transaction.getTimeEntered().getTime()
        };

        SQLiteDatabase db = sqlitedb.getWritableDatabase();
        db.delete(TABLE_TRANSACTION, String.format(WHERE_DOUBLE_INT, whereArgs), null);
    }

    @Override
    public void updateUser(User nuw) throws IllegalStateException {
        checkUserLoggedIn();

        int userID = getUserID(currentUser);
        if (userID == -1) {
            throw new IllegalArgumentException();
        }

        Object[] whereArgs = new Object[] {
            KEY_USER_PK, userID
        };

        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_USERNAME, nuw.getUsername());
        cv.put(KEY_USER_PASSWORD, nuw.getPassword());

        SQLiteDatabase db = sqlitedb.getWritableDatabase();
        db.update(TABLE_USER, cv, String.format(WHERE_INT, whereArgs), null);
    }

    @Override
    public void updateAccount(Account old, Account nuw) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();

        int accountID = getAccountID(currentUser, old);
        if (accountID == -1) {
            throw new IllegalArgumentException();
        }

        Object[] whereArgs = new Object[] {
            KEY_ACCOUNT_PK, accountID
        };

        ContentValues cv = new ContentValues();
        cv.put(KEY_ACCOUNT_NAME, nuw.getName());
        cv.put(KEY_ACCOUNT_BALANCE, nuw.getBalance().toPlainString());

        SQLiteDatabase db = sqlitedb.getWritableDatabase();
        db.update(TABLE_ACCOUNT, cv, String.format(WHERE_INT, whereArgs), null);
    }

    @Override
    public void updateTransaction(Account account, Transaction old, Transaction nuw) throws IllegalStateException, IllegalArgumentException {
        checkUserLoggedIn();

        int transactionID = this.getTransactionID(currentUser, account, old);
        if (transactionID == -1) {
            throw new IllegalArgumentException();
        }

        Object[] whereArgs = new Object[] {
            KEY_TRANSACTION_PK, transactionID
        };

        ContentValues cv = new ContentValues();
        cv.put(KEY_TRANSACTION_AMOUNT, nuw.getAmount().toPlainString());
        cv.put(KEY_TRANSACTION_TIME_ENTERED, nuw.getTimeEntered().getTime());
        cv.put(KEY_TRANSACTION_TIME_EFFECTIVE, nuw.getTimeEffective().getTime());

        if (nuw instanceof Deposit) {
            Deposit deposit = (Deposit) nuw;

            cv.put(KEY_TRANSACTION_DEPOSIT_SOURCE, deposit.getSource());
            cv.putNull(KEY_TRANSACTION_WITHDRAWAL_REASON);
            cv.putNull(KEY_TRANSACTION_WITHDRAWAL_CATEGORY);
        } else if (nuw instanceof Withdrawal) {
            Withdrawal withdrawal = (Withdrawal) nuw;

            cv.putNull(KEY_TRANSACTION_DEPOSIT_SOURCE);
            cv.put(KEY_TRANSACTION_WITHDRAWAL_REASON, withdrawal.getReason());
            cv.put(KEY_TRANSACTION_WITHDRAWAL_CATEGORY, withdrawal.getExpenseCategory().name());
        } else {
            System.err.println("Unknown transaction class " + nuw.getClass().getName() + '.');
            return;
        }

        SQLiteDatabase db = sqlitedb.getWritableDatabase();
        db.update(TABLE_TRANSACTION, cv, String.format(WHERE_INT, whereArgs), null);
    }

    @Override
    public Report accept(Report report) {
        for (User user: getAllUsers()) {
            report.visit(user);

            for (Account account: getAllAccounts()) {
                report.visit(account);

                for (Transaction transaction: getAllTransactions(account)) {
                    report.visit(transaction);
                }
            }
        }

        return report;
    }

    /**
     * Returns all users in the sqlite database.
     *
     * @return all local users.
     */
    private Collection<User> getAllUsers() {
        SQLiteDatabase db = sqlitedb.getReadableDatabase();
        Cursor c = db.query(TABLE_USER, null, null, null, null, null, null);

        Collection<User> users = new ArrayList<User>(c.getCount());
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();

            String username = c.getString(c.getColumnIndex(KEY_USER_USERNAME));
            String password = c.getString(c.getColumnIndex(KEY_USER_PASSWORD));

            users.add(new User(username, password));
            // TODO: return username-only users
        }

        return users;
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

    /**
     * Returns the primary key stored for the given user, or -1 if the given
     * user is not stored.
     *
     * @param user the user to find the primary key for.
     * @return the primary key of the user, or -1 if the user is not stored.
     */
    private int getUserID(User user) {
        // Select any users whose username exactly matches the given user's username.
        Object[] whereArgs = new Object[] {
            KEY_USER_USERNAME, user.getUsername()
        };

        SQLiteDatabase db = sqlitedb.getReadableDatabase();
        Cursor c = db.query(TABLE_USER, new String[] {KEY_USER_PK}, String.format(WHERE_STRING, whereArgs), null, null, null, null);

        if (c.getCount() == 0) {
            return -1;
        } else {
            c.moveToFirst();
            return c.getInt(c.getColumnIndex(KEY_USER_PK));
        }
    }

    /**
     * Returns the primary key stored for the given account owned by the given
     * user, or -1 if either the user or the account is not stored.
     *
     * @param user the user who owns the account.
     * @param account the account to find the primary key for.
     * @return the primary key of the account, or -1 if the account is not
     * stored.
     */
    private int getAccountID(User user, Account account) {
        int userID = getUserID(user);
        if (userID == -1) {
            return -1;
        }

        Object[] whereArgs = new Object[] {
            KEY_USER_PK, userID,
            KEY_ACCOUNT_NAME, account.getName()
        };

        SQLiteDatabase db = sqlitedb.getReadableDatabase();
        Cursor c = db.query(TABLE_ACCOUNT, new String[] {KEY_ACCOUNT_PK}, String.format(WHERE_INT_STRING, whereArgs), null, null, null, null);

        if (c.getCount() == 0) {
            return -1;
        } else {
            c.moveToFirst();
            return c.getInt(c.getColumnIndex(KEY_ACCOUNT_PK));
        }
    }

    /**
     * Returns the primary key stored for the given transaction in the given
     * account owned by the given user. Returns -1 if the transaction is not
     * stored.
     *
     * @param user the user which owns the account.
     * @param account the account in which the transaction is contained.
     * @param transaction the transaction to find the primary key for.
     * @return the primary key of the transaction, or -1 if the transaction
     * is not stored.
     */
    private int getTransactionID(User user, Account account, Transaction transaction) {
        int accountID = getAccountID(user, account);
        if (accountID == -1) {
            return -1;
        }

        Object[] whereArgs = new Object[] {
            KEY_ACCOUNT_PK, accountID,
            KEY_TRANSACTION_TIME_ENTERED, transaction.getTimeEntered().getTime()
        };

        SQLiteDatabase db = sqlitedb.getReadableDatabase();
        Cursor c = db.query(TABLE_TRANSACTION, new String[] {KEY_TRANSACTION_PK}, String.format(WHERE_DOUBLE_INT, whereArgs), null, null, null, null);

        if (c.getCount() == 0) {
            return -1;
        } else {
            c.moveToFirst();
            return c.getInt(c.getColumnIndex(KEY_ACCOUNT_PK));
        }
    }

    /**
     * An android sqlite database helper.
     */
    private static class LocalSQLiteDatabase extends SQLiteOpenHelper {

        /**
         * Creates a new sqlite database helper for the given context.
         *
         * @param context the context to create the sqlite database helper for.
         */
        private LocalSQLiteDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_ACCOUNT);
            db.execSQL(CREATE_TABLE_TRANSACTION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(String.format(DROP_TABLE, TABLE_USER));
            db.execSQL(String.format(DROP_TABLE, TABLE_ACCOUNT));
            db.execSQL(String.format(DROP_TABLE, TABLE_TRANSACTION));

            onCreate(db);
        }
    }
}
