package org.homenet.dnoved1.woms.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;

/**
 * A financial account which stores information such as transactions and which
 * can generate reports based on them.
 */
public class Account implements Displayable, Serializable {

    /**Serial version.*/
    private static final long serialVersionUID = 1L;

    /**The name of this account. Also the unique identifier for this object.*/
    private String name;
    /**The current balance of this account.*/
    private BigDecimal balance;

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
        if (o == this) {
            return true;
        } else if (o == null || !(o instanceof Account)) {
            return false;
        }

        Account account = (Account) o;
        return name.equals(account.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
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

    /**
     * Returns this account's balance as a string formatted as either '+ $x.xx'
     * or '- $x.xx', depending on whether it is positive or negative.
     *
     * @return this account's balance formatted as described above.
     */
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
}
