package org.homenet.dnoved1.woms.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A deposit transaction. Deposits modify accounts by adding money to their
 * balance.
 */
public class Deposit extends Transaction {

    /**Serial version.*/
    private static final long serialVersionUID = 1L;

    /**The user-defined source of this deposit.*/
    protected String source;

    /**
     * Creates a new deposit with the given source, amount, time entered, and
     * time at which is should become effective.
     *
     * @param amount the amount that is being deposited.
     * @param timeEntered the time at which this deposit was created by a
     * user.
     * @param timeEffective the time at which this deposit should become
     * effective.
     * @param source the source of this deposit.
     */
    public Deposit(String source, BigDecimal amount, Date timeEntered, Date timeEffective) {
        super(amount, timeEntered, timeEffective);
        this.source = source;
    }

    @Override
    public String getType() {
        return Transaction.TYPE_DEPOSIT;
    }

    /**
     * Returns the user defined source of this deposit.
     *
     * @return this deposit's source.
     */
    public String getSource() {
        return source;
    }

    @Override
    public void applyToAccount(Account account) {
        account.adjustBalance(amount);
    }

    @Override
    public String oneLineString() {
        return SimpleDateFormat.getDateTimeInstance().format(timeEffective)
                + " + "
                + NumberFormat.getCurrencyInstance().format(this.amount.doubleValue());
    }

    @Override
    public String[] multiLineString() {
        return new String[] {
            "Deposit:",
            "\tAmount: " + NumberFormat.getCurrencyInstance().format(this.amount.doubleValue()),
            "\tDate:   " + SimpleDateFormat.getDateTimeInstance().format(timeEffective),
            "\tSource: " + source
        };
    }
}
