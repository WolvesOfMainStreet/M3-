package cs2340.woms.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;

/**
 * A financial account which stores information such as transactions and which
 * can generate reports based on them. Not to be confused with LoginAccounts,
 * which are used for logging into the application and which can have any number
 * of FinanceAccounts.
 */
public class FinanceAccount implements Displayable {

    private final String name;
    private BigDecimal balance;

    /**
     * Creates a new FinanceAccount with the given name and a starting balance
     * of 0.
     *
     * @param name the full name for this FinanceAccount.
     */
    public FinanceAccount(String name) {
        this(name, new BigDecimal(0));
    }

    /**
     * Creates a new FinanceAccount with the given name and starting balance.
     *
     * @param name the full name for this FinanceAccount.
     * @param balance the starting balance for this FinanceAccount.
     */
    public FinanceAccount(String name, BigDecimal balance) {
        this.name = name;
        this.balance = new BigDecimal(0, MathContext.DECIMAL32);
        this.balance = this.balance.add(balance);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

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
        if (o == null || !(o instanceof FinanceAccount)) return false;

        FinanceAccount account = (FinanceAccount) o;
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
        string += NumberFormat.getCurrencyInstance().format(balance.doubleValue());
        return  string;
    }
}
