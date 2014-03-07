package cs2340.woms.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * A withdrawal transaction. Deposits modify accounts by subtracting money from
 * their balance.
 */
public class Withdrawal extends Transaction {

    public static final String SAVE_KEY_REASON = "reason";

    protected String reason;

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
     * @param reason the reason for the withdrawal.
     * @param amount the amount that is being withdrawn.
     * @param timeEntered the time at which this withdrawal was created by a
     * user.
     * @param timeEffective the time at which this withdrawal should become
     * effective.
     */
    public Withdrawal(String reason, BigDecimal amount, Date timeEntered, Date timeEffective) {
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
    public Map<String, String> write(Map<String, String> writeData) {
        writeData = super.write(writeData);
        writeData.put(SAVE_KEY_REASON, reason);
        return writeData;
    }

    @Override
    public void read(Map<String, String> readData) {
        super.read(readData);

        // Read reason. Default to 'Unknown'.
        String reason = readData.get(SAVE_KEY_REASON);
        if (reason == null) {
            System.out.println("Error reading source.");
            reason = "Unknown";
        }
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Withdraw: " + reason + ", " + super.toString();
    }
}
