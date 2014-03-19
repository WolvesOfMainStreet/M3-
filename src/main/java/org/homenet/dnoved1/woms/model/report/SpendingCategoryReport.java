package org.homenet.dnoved1.woms.model.report;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.homenet.dnoved1.woms.model.Account;
import org.homenet.dnoved1.woms.model.ExpenseCategory;
import org.homenet.dnoved1.woms.model.Transaction;
import org.homenet.dnoved1.woms.model.User;
import org.homenet.dnoved1.woms.model.Withdrawal;

/**
 * A report on a single user's expenses from a starting to an ending period.
 */
public class SpendingCategoryReport implements Report {

    /**The user which is currently being visited.*/
    private User currentUser;

    /**The user to generate this report for.*/
    private User user;
    /**The starting period for this report.*/
    private Date startPeriod;
    /**The ending period for this report.*/
    private Date endPeriod;
    /**A map of expense categories to the amount attributed to them.*/
    private Map<ExpenseCategory, BigDecimal> expensesPerCategory;

    /**
     * Creates a new spending category report for the given user from the given
     * start date and end date.
     *
     * @param user the user to generate the spending report from.
     * @param startPeriod the starting period. Only transactions entered after
     * this date will be considered.
     * @param endPeriod the ending period. Only transactions entered before
     * this date will be considered.
     */
    public SpendingCategoryReport(User user, Date startPeriod, Date endPeriod) {
        this.user = user;
        this.startPeriod = (Date) startPeriod.clone();
        this.endPeriod = (Date) endPeriod.clone();

        // Initialize each category with 0.
        expensesPerCategory = new HashMap<ExpenseCategory, BigDecimal>();
        for (ExpenseCategory type: ExpenseCategory.values()) {
            expensesPerCategory.put(type, new BigDecimal(0, MathContext.DECIMAL32));
        }
    }

    @Override
    public void visit(User user) {
        currentUser = user;
    }

    @Override
    public void visit(Account account) { }

    @Override
    public void visit(Transaction transaction) {
        if (!user.equals(currentUser)
                || !(transaction instanceof Withdrawal)
                || transaction.getTimeEffective().before(startPeriod)
                || transaction.getTimeEffective().after(endPeriod)) {
            return;
        }

        Withdrawal withdrawal = (Withdrawal) transaction;
        BigDecimal amount = expensesPerCategory.get(withdrawal.getExpenseCategory());
        amount = amount.add(withdrawal.getAmount());
        expensesPerCategory.put(withdrawal.getExpenseCategory(), amount);
    }

    @Override
    public String oneLineString() {
        return "Spending Category Report for " + user.getUsername();
    }

    @Override
    public String[] multiLineString() {
        List<String> lines = new ArrayList<String>();

        lines.add("Spending Category Report for " + user.getUsername());
        lines.add("\t" + SimpleDateFormat.getDateInstance().format(startPeriod)
                + " - " + SimpleDateFormat.getDateInstance().format(endPeriod));

        BigDecimal totalExpenses = BigDecimal.ZERO;
        for (ExpenseCategory type: ExpenseCategory.values()) {
            BigDecimal expense = expensesPerCategory.get(type);
            lines.add("\t" + type.name() + ": " + NumberFormat.getCurrencyInstance().format(expense.doubleValue()));
            totalExpenses = totalExpenses.add(expense);
        }

        lines.add("Total: " + NumberFormat.getCurrencyInstance().format(totalExpenses.doubleValue()));

        return lines.toArray(new String[lines.size()]);
    }
}
