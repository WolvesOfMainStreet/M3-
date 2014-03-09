package cs2340.woms.model.database.ct;

import java.util.Date;

/**
 * A complex type for Accounts. Contains fields for the account's name, time of
 * last edit, current balance, and a sequence of the account's transactions.
 */
public class Account {

    private String name;
    private Date lastEdited;
    private String balance;
    private Transaction[] transactions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    public void setTransactions(Transaction[] transactions) {
        this.transactions = transactions;
    }
}
