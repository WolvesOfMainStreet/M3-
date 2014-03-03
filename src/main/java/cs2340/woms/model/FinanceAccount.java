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
public class FinanceAccount {

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
        StringBuilder string = new StringBuilder();
        string.append("Name: ");
        string.append(name);
        string.append("\n");

        string.append("\tBalance: ");
        //TODO: add support for accounts in multiple currencies
        string.append(NumberFormat.getCurrencyInstance().format(balance.doubleValue()));
        return string.toString();
    }
}
