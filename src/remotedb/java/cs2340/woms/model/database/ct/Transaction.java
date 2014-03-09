package cs2340.woms.model.database.ct;

import java.util.Date;

/**
 * A complex type for Transactions. Contains fields for the time the
 * transaction was created, the time it should become effective, and the amount
 * of money the transaction represents.
 */
public class Transaction {

    private Date timeEntered;
    private Date timeEffective;
    private String amount;

    public Date getTimeEntered() {
        return timeEntered;
    }

    public void setTimeEntered(Date timeEntered) {
        this.timeEntered = timeEntered;
    }

    public Date getTimeEffective() {
        return timeEffective;
    }

    public void setTimeEffective(Date timeEffective) {
        this.timeEffective = timeEffective;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
