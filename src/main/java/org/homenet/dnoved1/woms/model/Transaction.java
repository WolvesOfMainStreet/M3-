package org.homenet.dnoved1.woms.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

/**
 * The base class for transactions. All subclasses should provide a public
 * no argument constructor as per the the specification of the
 * {@link SerializableData} interface. This class handles the serialization
 * of its amount, time entered, and time effective fields.
 */
public abstract class Transaction implements Displayable, Serializable {

    /**Serial version.*/
    protected static final long serialVersionUID = 1L;

    /**The amount of money this transaction represents.*/
    protected BigDecimal amount;
    /**The time at which this transaction was created. Also the unique identifier for this object.*/
    protected Date timeEntered;
    /**The time at which this transaction should become effective.*/
    protected Date timeEffective;

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
        this.timeEntered = (Date) timeEntered.clone();
        this.timeEffective = (Date) timeEffective.clone();
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
        return (Date) timeEntered.clone();
    }

    /**
     * Returns the time at which this transaction should become effective.
     *
     * @return the time at which this transaction becomes effective.
     */
    public Date getTimeEffective() {
        return (Date) timeEffective.clone();
    }

    /**
     * Returns the type of transaction this is. Can be multiple types, in which
     * case different types should be separated by a '|' symbol.
     *
     * @return the type(s) of transaction this is.
     */
    public abstract Type getType();

    /**
     * Applies this transaction to the given account.
     *
     * @param account the account to apply this transaction to.
     */
    public abstract void applyToAccount(Account account);

    @Override
    public String toString() {
        return this.oneLineString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !(o instanceof Transaction)) {
            return false;
        }

        Transaction transaction = (Transaction) o;
        return timeEntered.equals(transaction.timeEntered);
    }

    @Override
    public int hashCode() {
        return timeEntered.hashCode();
    }

    /**
     * An enumeration of the kinds of transactions.
     */
    public enum Type {
        /**A deposit type transaction.*/
        DEPOSIT,
        /**A withdrawal type transaction.*/
        WITHDRAWAL;
    }
}
