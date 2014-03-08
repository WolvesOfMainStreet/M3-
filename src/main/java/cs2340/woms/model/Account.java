package cs2340.woms.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.util.Map;

/**
 * A financial account which stores information such as transactions and which
 * can generate reports based on them.
 */
public class Account implements Displayable, SerializableData {

    public static final String SAVE_KEY_NAME = "account-name";
    public static final String SAVE_KEY_BALANCE = "account-balance";

    private String name;
    private BigDecimal balance;

    /**
     * For serialization, not for normal use.
     */
    public Account() { }

    /**
     * Creates a new FinanceAccount with the given name and a starting balance
     * of 0.
     *
     * @param name the full name for this FinanceAccount.
     */
    public Account(String name) {
        this(name, new BigDecimal(0));
    }

    /**
     * Creates a new FinanceAccount with the given name and starting balance.
     *
     * @param name the full name for this FinanceAccount.
     * @param balance the starting balance for this FinanceAccount.
     */
    public Account(String name, BigDecimal balance) {
        this.name = name;
        this.balance = new BigDecimal(0, MathContext.DECIMAL32);
        this.balance = this.balance.add(balance);
    }

    /**
     * Returns the full name of this account.
     *
     * @return the full name of this account.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current balance of this account.
     *
     * @return the current balance of this account.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Adjust's this account's balance by the given amount.
     *
     * @param amount the amount to adjust this account's balance by.
     */
    public void adjustBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Override
    public String toString() {
        return this.oneLineString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof Account)) return false;

        Account account = (Account) o;
        return name.equals(account.name) && balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        int hashCode = name.hashCode();
        hashCode = hashCode * 31 + balance.hashCode();
        return hashCode;
    }

    @Override
    public String oneLineString() {
        return name + ": " + getBalanceString();
    }

    @Override
    public String[] multiLineString() {
        return new String[] {
                "Account:",
                "\tName:    " + name,
                "\tBalance: " + getBalanceString()
        };
    }

    private String getBalanceString() {
        String string;
        if (balance.doubleValue() < 0) {
            string = "- ";
        } else {
            string = "+ ";
        }
        string += NumberFormat.getCurrencyInstance().format(balance.abs().doubleValue());
        return  string;
    }

    @Override
    public Map<String, String> write(Map<String, String> writeData) {
        writeData.put(SAVE_KEY_NAME, name);
        writeData.put(SAVE_KEY_BALANCE, balance.toPlainString());
        return writeData;
    }

    @Override
    public void read(Map<String, String> readData) {
        // Read account name. Default to 'Unknown'.
        String name = readData.get(SAVE_KEY_NAME);
        if (name == null) {
            System.err.println("Error reading account name, defaulting to 'Unknown'.");
            name = "Unknown";
        }
        this.name = name;

        // Read account balance. Default to 0.
        String balance = readData.get(SAVE_KEY_BALANCE);
        if (balance == null) {
            System.err.println("Error reading balance for " + name + ", defaulting to 0.");
            balance = "0";
        }
        this.balance = new BigDecimal(balance, MathContext.DECIMAL32);
    }
}
