package cs2340.woms.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * The base class for transactions. All subclasses should provide a public
 * no argument constructor as per the the specification of the
 * {@link SerializableData} interface. This class handles the serialization
 * of its amount, time entered, and time effective fields.
 */
public abstract class Transaction implements SerializableData, Displayable {

    // The keys that amount, time entered, and time effective are saved under.
    public static final String SAVE_KEY_AMOUNT = "amount";
    public static final String SAVE_KEY_TIME_ENTERED = "timeEntered";
    public static final String SAVE_KEY_TIME_EFFECTIVE = "timeEffective";

    // The default categories of transactions.
    public static final String TYPE_DEPOSIT = "deposit";
    public static final String TYPE_WITHDRAWAL = "withdraw";

    protected BigDecimal amount;
    protected Date timeEntered;
    protected Date timeEffective;

    /**
     * For serialization use, not for normal use.
     */
    public Transaction() { }

    /**
     * Creates a new transaction with the given amount, time entered, and time
     * at which is should become effective.
     *
     * @param amount the amount this transaction represents.
     * @param timeEntered the time at which this transaction was created by a
     * user.
     * @param timeEffective the time at which this transaction should become
     * effective.
     */
    public Transaction(BigDecimal amount, Date timeEntered, Date timeEffective) {
        this.amount = new BigDecimal(0, MathContext.DECIMAL32);
        this.amount = this.amount.add(amount);
        this.timeEntered = timeEntered;
        this.timeEffective = timeEffective;
    }

    /**
     * Returns the amount of money this transaction represents.
     *
     * @return the amount of money this transaction represents.
     */
    public BigDecimal getAmount() {
        return amount;
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
     * Returns the type of transaction this is. Can be multiple types, in which
     * case different types should be separated by a '|' symbol.
     *
     * @return the type(s) of transaction this is.
     */
    public abstract String getType();

    /**
     * Applies this transaction to the given account.
     *
     * @param account the account to apply this transaction to.
     */
    public abstract void applyToAccount(FinanceAccount account);

    @Override
    public Map<String, String> write(Map<String, String> writeData) {
        writeData.put(SAVE_KEY_AMOUNT, amount.toPlainString());
        writeData.put(SAVE_KEY_TIME_ENTERED, SimpleDateFormat.getDateTimeInstance().format(timeEntered));
        writeData.put(SAVE_KEY_TIME_EFFECTIVE, SimpleDateFormat.getDateTimeInstance().format(timeEffective));
        return writeData;
    }

    @Override
    public void read(Map<String, String> readData) {
        // TODO: Add logging to application, replace println's with logging.

        // Read amount. Default to 0.
        String amount = readData.get(SAVE_KEY_AMOUNT);
        if (amount == null) {
            System.out.println("Error reading amount.");
            this.amount = new BigDecimal(0, MathContext.DECIMAL32);
        } else {
            this.amount = new BigDecimal(amount, MathContext.DECIMAL32);
        }

        // Read time entered. Default to current time.
        String timeEntered = readData.get(SAVE_KEY_TIME_ENTERED);
        if (timeEntered == null) {
            System.out.println("Error reading time entered.");
            this.timeEntered = new Date();
        } else {
            try {

                this.timeEntered = SimpleDateFormat.getDateTimeInstance().parse(timeEntered);
            } catch (ParseException e) {
                System.out.println("Error parsing time entered.");
                this.timeEntered = new Date();
            }
        }

        // Read time effective. Default to current time.
        String timeEffective = readData.get(SAVE_KEY_TIME_EFFECTIVE);
        if (timeEffective == null) {
            System.out.println("Error reading time effective.");
            this.timeEffective = new Date();
        } else {
            try {
                this.timeEffective = SimpleDateFormat.getDateTimeInstance().parse(timeEffective);
            } catch (ParseException e) {
                System.out.println("Error parsing time effective.");
                this.timeEffective = new Date();
            }
        }
    }

    @Override
    public String toString() {
        return this.oneLineString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof Transaction)) return false;

        Transaction transaction = (Transaction) o;
        return amount.equals(transaction.amount)
                && timeEntered.equals(transaction.timeEntered)
                && timeEffective.equals(transaction.timeEffective);
    }

    @Override
    public int hashCode() {
        int hashCode = amount.hashCode();
        hashCode = hashCode * 31 + timeEntered.hashCode();
        hashCode = hashCode * 31 + timeEffective.hashCode();
        return hashCode;
    }
}
