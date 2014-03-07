package cs2340.woms.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * A deposit transaction. Deposits modify accounts by adding money to their
 * balance.
 */
public class Deposit extends Transaction {

    /**
     * For serialization use, not for normal use.
     */
    public Deposit() {
        super();
    }

    /**
     * Creates a new deposit with the given amount, time entered, and time
     * at which is should become effective.
     *
     * @param amount the amount that is being deposited.
     * @param timeEntered the time at which this deposit was created by a
     * user.
     * @param timeEffective the time at which this deposit should become
     * effective.
     */
    public Deposit(BigDecimal amount, Date timeEntered, Date timeEffective) {
        super(amount, timeEntered, timeEffective);
    }

    @Override
    public String getType() {
        return Transaction.TYPE_DEPOSIT;
    }

    @Override
    public void applyToAccount(FinanceAccount account) {
        account.adjustBalance(amount);
    }

    @Override
    public String toString() {
        return "Deposit: " + super.toString();
    }
}
