package org.homenet.dnoved1.woms.android.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.homenet.dnoved1.woms.model.Account;
import org.homenet.dnoved1.woms.model.Transaction;
import org.homenet.dnoved1.woms.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class AndroidLocalDatabase extends SQLiteOpenHelper {

    // TODO: make column creation dynamic based on frequency of key use.
    // I.e if lots of accounts start using key X, make a new column for X
    // rather than saving it under extras.

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "womsMainDB";

    private static AndroidLocalDatabase instance;

    //-----SQL Names------------------------------------------------------------

    private static final String TABLE_USER = "users";
    private static final String TABLE_ACCOUNT = "accounts";
    private static final String TABLE_TRANSACTION = "transactions";

    private static final String KEY_USER_PK = "user_id";
    private static final String KEY_USER_CLASS = "class";
    private static final String KEY_USER_USERNAME = "username";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_EXTRAS = "extras";

    private static final String KEY_ACCOUNT_PK = "account_id";
    private static final String KEY_ACCOUNT_CLASS = "class";
    private static final String KEY_ACCOUNT_NAME = "name";
    private static final String KEY_ACCOUNT_BALANCE = "balance";
    private static final String KEY_ACCOUNT_EXTRAS = "extras";

    private static final String KEY_TRANSACTION_PK = "transaction_id";
    private static final String KEY_TRANSACTION_AMOUNT = "amount";
    private static final String KEY_TRANSACTION_TIME_ENTERED = "time_entered";
    private static final String KEY_TRANSACTION_TIME_EFFECTIVE = "time_effective";
    private static final String KEY_TRANSACTION_CLASS = "class";
    private static final String KEY_TRANSACTION_EXTRAS = "extras";

    //-----SQL Commands---------------------------------------------------------

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                + KEY_USER_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_CLASS + " TEXT NOT NULL,"
                + KEY_USER_USERNAME + " TEXT UNIQUE NOT NULL,"
                + KEY_USER_PASSWORD + " TEXT NOT NULL,"
                + KEY_USER_EXTRAS + " TEXT NOT NULL"
            + ")";

    private static final String CREATE_TABLE_ACCOUNT =
            "CREATE TABLE " + TABLE_ACCOUNT + "("
                + KEY_ACCOUNT_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_PK + " INTEGER,"
                + KEY_ACCOUNT_CLASS + " TEXT NOT NULL,"
                + KEY_ACCOUNT_NAME + " TEXT NOT NULL,"
                + KEY_ACCOUNT_BALANCE + " TEXT NOT NULL,"
                + KEY_ACCOUNT_EXTRAS + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + KEY_USER_PK + ") REFERENCES " + TABLE_USER + "(" + KEY_USER_PK + ")"
            + ")";

    private static final String CREATE_TABLE_TRANSACTION =
            "CREATE TABLE " + TABLE_TRANSACTION + "("
                + KEY_TRANSACTION_PK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ACCOUNT_PK + " INTEGER,"
                + KEY_TRANSACTION_CLASS + " TEXT NOT NULL,"
                + KEY_TRANSACTION_AMOUNT + " TEXT NOT NULL,"
                + KEY_TRANSACTION_TIME_ENTERED + " TEXT NOT NULL,"
                + KEY_TRANSACTION_TIME_EFFECTIVE + " TEXT NOT NULL,"
                + KEY_TRANSACTION_EXTRAS + " TEXT NOT NULL,"
                + "FOREIGN KEY (" + KEY_ACCOUNT_PK + ") REFERENCES " + TABLE_ACCOUNT + "(" + KEY_ACCOUNT_PK + ")"
            + ")";

    //-----End SQL Commands-----------------------------------------------------

    public static void create(Context context) {
        if (instance == null) {
            instance = new AndroidLocalDatabase(context);
        }
    }

    public static AndroidLocalDatabase getLocalDatabase() {
        if (instance == null) {
            throw new IllegalStateException("The database has not been created yet.");
        } else {
            return instance;
        }
    }

    private AndroidLocalDatabase(Context context) {
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
        db.execSQL("DROP TABLE " + TABLE_USER);
        db.execSQL("DROP TABLE " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE " + TABLE_TRANSACTION);

        onCreate(db);
    }

    public boolean doesUserExist(User user) {
        return getUserID(user) != -1;
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        //-----Get the serializable data for the user---------------------------
        Map<String, String> writeData = new HashMap<String, String>();
        writeData = user.write(writeData);

        //-----Get the standard user data, with appropriate defaults------------
        String username = writeData.remove(User.SAVE_KEY_USERNAME);
        if (username == null) {
            System.err.println("Error getting username from saved user data.");
            username = "unknown";
        }

        String password = writeData.remove(User.SAVE_KEY_PASSWORD);
        if (password == null) {
            System.err.println("Error getting password from saved user data.");
            password = "pass123";
        }

        //-----Get the extra user data------------------------------------------
        StringBuilder extras = new StringBuilder();
        for (Entry<String, String> e: writeData.entrySet()) {
            extras.append(e.getKey());
            extras.append(":");
            extras.append(e.getValue());
            extras.append("|");
        }

        //-----Write all data to sql tables-------------------------------------
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_CLASS, user.getClass().getName());
        cv.put(KEY_USER_USERNAME, username);
        cv.put(KEY_USER_PASSWORD, password);
        cv.put(KEY_USER_EXTRAS, extras.toString());

        long rowID = db.insert(TABLE_USER, null, cv);

        return rowID != -1;
    }

    public void addAccount(User user, Account account) {
        int userID = getUserID(user);
        if (userID == -1) {
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        //-----Get the serializable data for the account------------------------
        Map<String, String> writeData = new HashMap<String, String>();
        writeData = account.write(writeData);

        //-----Get the standard account data, with appropriate defaults---------
        String name = writeData.remove(Account.SAVE_KEY_NAME);
        if (name == null) {
            System.err.println("Error getting name from saved account data.");
            name = "Unknown";
        }

        String balance = writeData.remove(Account.SAVE_KEY_BALANCE);
        if (balance == null) {
            System.err.println("Error getting balance from saved account data.");
            balance = "0";
        }

        //-----Get the extra account data---------------------------------------
        StringBuilder extras = new StringBuilder();
        for (Entry<String, String> e: writeData.entrySet()) {
            extras.append(e.getKey());
            extras.append(":");
            extras.append(e.getValue());
            extras.append("|");
        }

        //-----Write all data to sql tables-------------------------------------
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_PK, userID);
        cv.put(KEY_ACCOUNT_CLASS, account.getClass().getName());
        cv.put(KEY_ACCOUNT_NAME, name);
        cv.put(KEY_ACCOUNT_BALANCE, balance);
        cv.put(KEY_ACCOUNT_EXTRAS, extras.toString());

        db.insert(TABLE_ACCOUNT, null, cv);
    }

    /**
     * Adds a new transaction to the SQL database.
     *
     * @param user the user the new transaction belongs to.
     * @param account the account the transaction applies to.
     * @param transaction the transaction to add.
     */
    public void addTransaction(User user, Account account, Transaction transaction) {
        int accountID = getAccountID(user, account);
        if (accountID == -1) {
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // Get the serializable data for the transaction
        Map<String, String> writeData = new HashMap<String, String>();
        writeData = transaction.write(writeData);

        //-----Get the standard transaction data, with appropriate defaults-----
        String amount = writeData.remove(Transaction.SAVE_KEY_AMOUNT);
        if (amount == null) {
            System.err.println("Error getting amount from saved transaction data.");
            amount = "0";
        }

        String timeEntered = writeData.remove(Transaction.SAVE_KEY_TIME_ENTERED);
        if (timeEntered == null) {
            System.err.println("Error getting time entered from saved transaction data.");
            timeEntered = SimpleDateFormat.getDateTimeInstance().format(new Date());
        }

        String timeEffective = writeData.remove(Transaction.SAVE_KEY_TIME_EFFECTIVE);
        if (timeEffective == null) {
            System.err.println("Error getting time effective from saved transaction data.");
            timeEffective = SimpleDateFormat.getDateTimeInstance().format(new Date());
        }

        //-----Get the extra transaction data-----------------------------------
        StringBuilder extras = new StringBuilder();
        for (Entry<String, String> e: writeData.entrySet()) {
            extras.append(e.getKey());
            extras.append(":");
            extras.append(e.getValue());
            extras.append("|");
        }

        //-----Write all data to sql tables-------------------------------------
        ContentValues cv = new ContentValues();
        cv.put(KEY_ACCOUNT_PK, accountID);
        cv.put(KEY_TRANSACTION_CLASS, transaction.getClass().getName());
        cv.put(KEY_TRANSACTION_AMOUNT, amount);
        cv.put(KEY_TRANSACTION_TIME_ENTERED, timeEntered);
        cv.put(KEY_TRANSACTION_TIME_EFFECTIVE, timeEffective);
        cv.put(KEY_TRANSACTION_EXTRAS, extras.toString());

        db.insert(TABLE_TRANSACTION, null, cv);
    }

    public List<User> getUsers() {
        String query = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        List<User> users = new ArrayList<User>(c.getCount());
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();

            Map<String, String> readData = new HashMap<String, String>();
            String className = c.getString(c.getColumnIndex(KEY_USER_CLASS));

            readData.put(User.SAVE_KEY_USERNAME, c.getString(c.getColumnIndex(KEY_USER_USERNAME)));
            readData.put(User.SAVE_KEY_PASSWORD, c.getString(c.getColumnIndex(KEY_USER_PASSWORD)));

            String extras = c.getString(c.getColumnIndex(KEY_USER_EXTRAS));
            for (String extra: extras.split("\\|")) {
                if (extra.equals("")) continue; // Skip empty entries
                int splitIndex = extra.indexOf(":");
                readData.put(extra.substring(0, splitIndex), extra.substring(splitIndex + 1));
            }

            // Create the user
            User user = null;
            try {
                @SuppressWarnings("unchecked")
                Class<? extends User> userClass = (Class<? extends User>) Class.forName(className);
                user = userClass.newInstance();
                user.read(readData);
            } catch (ClassNotFoundException e) {
                System.err.println("Error finding user class name: " + className + ".");
            } catch (InstantiationException e) {
                System.err.println("Error creating user with class name: " + className + ".");
            } catch (IllegalAccessException e) {
                System.err.println("Error creating user with class name: " + className + ".");
            }

            if (user != null) {
                users.add(user);
            }
        }

        return users;
    }

    public List<Account> getAccounts(User user) {
        int userID = getUserID(user);
        if (userID == -1) {
            return Collections.emptyList();
        }

        // Get all accounts belonging to the given userID
        String query = "SELECT * FROM " + TABLE_ACCOUNT
                + " WHERE " + KEY_USER_PK + "=" + userID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        List<Account> accounts = new ArrayList<Account>();
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();

            Map<String, String> readData = new HashMap<String, String>();
            String className = c.getString(c.getColumnIndex(KEY_ACCOUNT_CLASS));

            readData.put(Account.SAVE_KEY_NAME, c.getString(c.getColumnIndex(KEY_ACCOUNT_NAME)));
            readData.put(Account.SAVE_KEY_BALANCE, c.getString(c.getColumnIndex(KEY_ACCOUNT_BALANCE)));

            String extras = c.getString(c.getColumnIndex(KEY_ACCOUNT_EXTRAS));
            for (String extra: extras.split("\\|")) {
                if (extra.equals("")) continue; // Skip empty entries
                int splitIndex = extra.indexOf(":");
                readData.put(extra.substring(0, splitIndex), extra.substring(splitIndex + 1));
            }

            // Create the account
            Account account = null;
            try {
                @SuppressWarnings("unchecked")
                Class<? extends Account> accountClass = (Class<? extends Account>) Class.forName(className);
                account = accountClass.newInstance();
                account.read(readData);
            } catch (ClassNotFoundException e) {
                System.err.println("Error finding account class name: " + className + ".");
            } catch (InstantiationException e) {
                System.err.println("Error creating account with class name: " + className + ".");
            } catch (IllegalAccessException e) {
                System.err.println("Error creating account with class name: " + className + ".");
            }

            if (account != null) {
                accounts.add(account);
            }
        }

        return accounts;
    }

    /**
     * Returns a list of all transactions belonging to the given account owned
     * by the given user. Returns an empty list if the given user is not in the
     * database, if the given account is not in the database, or if the given
     * account does not belong to the given user.
     *
     * @param user the user under which the given account belongs to.
     * @param account the account to get all transactions from.
     * @return a list of all transactions belonging to the given account.
     */
    public List<Transaction> getTransactions(User user, Account account) {
        int accountID = getAccountID(user, account);
        if (accountID == -1) {
            return Collections.emptyList();
        }

        String query = "SELECT * FROM " + TABLE_TRANSACTION
                + " WHERE " + KEY_ACCOUNT_PK + "=" + accountID;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        List<Transaction> transactions = new ArrayList<Transaction>();
        for (int i = 0; i < c.getCount(); i++) {
            c.moveToNext();
            Map<String, String> readData = new HashMap<String, String>();

            String className = c.getString(c.getColumnIndex(KEY_TRANSACTION_CLASS));

            readData.put(Transaction.SAVE_KEY_AMOUNT, c.getString(c.getColumnIndex(KEY_TRANSACTION_AMOUNT)));
            readData.put(Transaction.SAVE_KEY_TIME_ENTERED, c.getString(c.getColumnIndex(KEY_TRANSACTION_TIME_ENTERED)));
            readData.put(Transaction.SAVE_KEY_TIME_EFFECTIVE, c.getString(c.getColumnIndex(KEY_TRANSACTION_TIME_EFFECTIVE)));

            String extras = c.getString(c.getColumnIndex(KEY_TRANSACTION_EXTRAS));
            for (String extra: extras.split("\\|")) {
                if (extra.equals("")) continue; // Skip empty entries
                int splitIndex = extra.indexOf(":");
                readData.put(extra.substring(0, splitIndex), extra.substring(splitIndex + 1));
            }

            // Create the transaction
            Transaction transaction = null;
            try {
                @SuppressWarnings("unchecked")
                Class<? extends Transaction> transactionClass = (Class<? extends Transaction>) Class.forName(className);
                transaction = transactionClass.newInstance();
                transaction.read(readData);
            } catch (ClassNotFoundException e) {
                System.err.println("Error finding transaction class name: " + className + ".");
            } catch (InstantiationException e) {
                System.err.println("Error creating transaction with class name: " + className + ".");
            } catch (IllegalAccessException e) {
                System.err.println("Error creating transaction with class name: " + className + ".");
            }

            if (transaction != null) {
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    public void updateAccount(User user, Account account) {
        int userID = getUserID(user);
        if (userID == -1) {
            return;
        }

        int accountID = getAccountID(user, account);
        if (accountID == -1) {
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_ACCOUNT_BALANCE, account.getBalance().toPlainString());

        db.update(TABLE_ACCOUNT, cv, KEY_ACCOUNT_PK + "=" + accountID, null);
    }

    private int getUserID(User user) {
        String query = "SELECT " + KEY_USER_PK + " FROM " + TABLE_USER
                + " WHERE " + KEY_USER_USERNAME + "='" + user.getUsername() + "'"
                + " AND " + KEY_USER_PASSWORD + "='" + user.getPassword() + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        if (c.getCount() == 0) {
            return -1;
        } else {
            c.moveToFirst();
            return c.getInt(c.getColumnIndex(KEY_USER_PK));
        }
    }

    private int getAccountID(User user, Account account) {
        int userID = getUserID(user);
        if (userID == -1) {
            return -1;
        }

        String query = "SELECT " + KEY_ACCOUNT_PK + " FROM " + TABLE_ACCOUNT
                + " WHERE " + KEY_USER_PK + "=" + userID
                + " AND " + KEY_ACCOUNT_NAME + "='" + account.getName() + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        if (c.getCount() == 0) {
            return -1;
        } else {
            c.moveToFirst();
            return c.getInt(c.getColumnIndex(KEY_ACCOUNT_PK));
        }
    }
}
