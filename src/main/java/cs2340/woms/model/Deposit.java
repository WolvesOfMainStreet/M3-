package cs2340.woms.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * A deposit transaction. Deposits modify accounts by adding money to their
 * balance.
 */
public class Deposit extends Transaction {

    public static final String SAVE_KEY_SOURCE = "source";

    protected String source;

    /**
     * For serialization use, not for normal use.
     */
    public Deposit() {
        super();
    }

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
    public Map<String, String> write(Map<String, String> writeData) {
        writeData = super.write(writeData);
        writeData.put(SAVE_KEY_SOURCE, source);
        return writeData;
    }

    @Override
    public void read(Map<String, String> readData) {
        super.read(readData);

        // Read source. Default to 'Unknown'.
        String source = readData.get(SAVE_KEY_SOURCE);
        if (source == null) {
            System.out.println("Error reading source.");
            source = "Unknown";
        }
        this.source = source;
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
