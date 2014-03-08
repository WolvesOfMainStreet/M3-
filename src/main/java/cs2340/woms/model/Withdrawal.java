package cs2340.woms.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * A withdrawal transaction. Deposits modify accounts by subtracting money from
 * their balance.
 */
public class Withdrawal extends Transaction {

    public static final String SAVE_KEY_REASON = "reason";
    public static final String SAVE_KEY_EXPENSE_TYPE = "expenseType";

    protected String reason;
    protected ExpenseCategory type;

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
    public String getType() {
        return Transaction.TYPE_WITHDRAWAL;
    }

    public ExpenseCategory getExpenseCategory() {
        return type;
    }

    @Override
    public void applyToAccount(Account account) {
        account.adjustBalance(amount.negate());
    }

    @Override
    public Map<String, String> write(Map<String, String> writeData) {
        writeData = super.write(writeData);
        writeData.put(SAVE_KEY_REASON, reason);
        writeData.put(SAVE_KEY_EXPENSE_TYPE, type.name());
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

        // Read expense type. Default to OTHER.
        String type = readData.get(SAVE_KEY_EXPENSE_TYPE);
        if (type == null) {
            System.out.println("Error reading expense type.");
            this.type = ExpenseCategory.OTHER;
        } else {
            try {
                this.type = ExpenseCategory.valueOf(type);
            } catch (IllegalArgumentException e) {
                System.out.println("Error getting saved expense type.");
                this.type = ExpenseCategory.OTHER;
            }
        }
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

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o) || !(o instanceof Withdrawal)) return false;

        Withdrawal withdrawal = (Withdrawal) o;
        return reason.equals(withdrawal.reason) && type == withdrawal.type;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode = hashCode * 31 + reason.hashCode();
        hashCode = hashCode * 31 + type.hashCode();
        return hashCode;
    }
}
