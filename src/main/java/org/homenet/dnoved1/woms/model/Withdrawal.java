package org.homenet.dnoved1.woms.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A withdrawal transaction. Deposits modify accounts by subtracting money from
 * their balance.
 */
public class Withdrawal extends Transaction {

    /**Serial version.*/
    private static final long serialVersionUID = 1L;

    /**The user-defined reason for this withdrawal.*/
    protected String reason;
    /**The type of expense this withdrawal is categorized as.*/
    protected ExpenseCategory type;

    /**
     * Creates a new withdrawal with the given amount, time entered, and time
     * at which is should become effective.
     *
     * @param type the category under which this withdrawal falls under.
     * @param reason the reason for the withdrawal.
     * @param amount the amount that is being withdrawn.
     * @param timeEntered the time at which this withdrawal was created by a
     * user.
     * @param timeEffective the time at which this withdrawal should become
     * effective.
     */
    public Withdrawal(ExpenseCategory type, String reason, BigDecimal amount, Date timeEntered, Date timeEffective) {
        super(amount, timeEntered, timeEffective);
        this.reason = reason;
        this.type = type;

    }

    @Override
    public Transaction.Type getType() {
        return Transaction.Type.WITHDRAWAL;
    }

    /**
     * Returns this withdrawal's expense category.
     *
     * @return this withdrawal's expense category.
     */
    public ExpenseCategory getExpenseCategory() {
        return type;
    }

    /**
     * Returns the reason for this withdrawal.
     *
     * @return the reason for this withdrawal.
     */
    public String getReason() {
        return reason;
    }

    @Override
    public void applyToAccount(Account account) {
        account.adjustBalance(amount.negate());
    }

    @Override
    public String oneLineString() {
        return SimpleDateFormat.getDateTimeInstance().format(timeEffective)
                + " - "
                + NumberFormat.getCurrencyInstance().format(this.amount.doubleValue());
    }

    @Override
    public String[] multiLineString() {
        return new String[] {
            "Withdrawal:",
            "\tAmount:   " + NumberFormat.getCurrencyInstance().format(this.amount.doubleValue()),
            "\tDate:     " + SimpleDateFormat.getDateTimeInstance().format(timeEffective),
            "\tCategory: " + type.name(),
            "\tReason:   " + reason
        };
    }
}
