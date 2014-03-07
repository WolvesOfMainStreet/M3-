package cs2340.woms.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * A withdrawal transaction. Deposits modify accounts by subtracting money from
 * their balance.
 */
public class Withdrawal extends Transaction {

    /**
     * For serialization use, not for normal use.
     */
    public Withdrawal() {
        super();
    }

    /**
     * Creates a new withdrawal with the given amount, time entered, and time
     * at which is should become effective.
     *
     * @param amount the amount that is being withdrawn.
     * @param timeEntered the time at which this withdrawal was created by a
     * user.
     * @param timeEffective the time at which this withdrawal should become
     * effective.
     */
    public Withdrawal(BigDecimal amount, Date timeEntered, Date timeEffective) {
        super(amount, timeEntered, timeEffective);
    }

    @Override
    public String getType() {
        return Transaction.TYPE_WITHDRAWAL;
    }

    @Override
    public void applyToAccount(FinanceAccount account) {
        account.adjustBalance(amount.negate());
    }

    @Override
    public String toString() {
        return "Withdraw: " + super.toString();
    }
}
