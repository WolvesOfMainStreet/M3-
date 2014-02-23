package cs2340.woms.account;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.Date;

/**
 * A class for holding transaction information.
 */
public class Transaction implements Comparable<Transaction> {

    // The time at which the user creates the transaction on their phone.
    private final Date timeEntered;

    // The user defined time at which this transaction should become effective.
    private Date timeEffective;

    private BigDecimal amount;

    /**
     * Creates a new Transaction with the given amount. The time entered and
     * time at which this transaction become effective are both automatically
     * initialized to the current time.
     *
     * @param amount the amount of money this transaction represents. Can be
     * negative.
     */
    public Transaction(BigDecimal amount) {
        this(amount, new Date());
    }

    /**
     * Creates a new Transaction with the given amount and time at which it
     * becomes effective. The time entered is automatically initialized to the
     * current time.
     *
     * @param amount the amount of money this transaction represents. Can be
     * negative.
     * @param timeEffective the time at which this transaction should become
     * effective.
     */
    public Transaction(BigDecimal amount, Date timeEffective) {
        this.timeEntered = new Date(); // Current time
        this.amount = new BigDecimal(0, MathContext.DECIMAL32);
        this.amount = this.amount.add(amount);
        this.timeEffective = timeEffective;
    }

    /**
     * Returns the time at which this transaction was entered into the
     * application.
     *
     * @return the time at which this transaction was entered.
     */
    public Date getTimeEntered() {
        return timeEntered;
    }

    /**
     * Returns the time at which this transaction should become effective.
     *
     * @return the time at which this transaction becomes effective.
     */
    public Date getTimeEffective() {
        return timeEffective;
    }

    /**
     * Returns the amount of money this transaction represents. Can be negative.
     *
     * @return the amount of money this transaction represents.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(timeEffective);
        string.append(" : ");
        string.append(NumberFormat.getCurrencyInstance().format(amount.doubleValue()));
        return string.toString();
    }

    /**
     * Transactions are comparable based on effective date, allowing automatic
     * sorting based on such.
     */
    @Override
    public int compareTo(Transaction other) {
        if (timeEffective.after(other.timeEffective)) {
            return 1;
        } else if (timeEffective.before(other.timeEffective)) {
            return -1;
        } else {
            return 0;
        }
    }
}
