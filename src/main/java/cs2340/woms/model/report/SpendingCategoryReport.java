package cs2340.woms.model.report;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs2340.woms.model.Account;
import cs2340.woms.model.ExpenseCategory;
import cs2340.woms.model.Transaction;
import cs2340.woms.model.User;
import cs2340.woms.model.Withdrawal;

/**
 * A report on a single user's expenses from a starting to an ending period.
 */
public class SpendingCategoryReport implements Report {

    // The user which is currently being visited.
    private User currentUser;

    private User user;
    private Date startPeriod;
    private Date endPeriod;
    private Map<ExpenseCategory, BigDecimal> expensesPerCategory;
    private BigDecimal totalExpenses;

    public SpendingCategoryReport(User user, Date startPeriod, Date endPeriod) {
        this.user = user;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;

        // Initialize each category with 0.
        expensesPerCategory = new HashMap<ExpenseCategory, BigDecimal>();
        for (ExpenseCategory type: ExpenseCategory.values()) {
            expensesPerCategory.put(type, new BigDecimal(0, MathContext.DECIMAL32));
        }
        totalExpenses = new BigDecimal(0, MathContext.DECIMAL32);
    }

    @Override
    public void accept(User user) {
        currentUser = user;
    }

    @Override
    public void accept(Account account) { }

    @Override
    public void accept(Transaction transaction) {
        if (!user.equals(currentUser)
                || !Transaction.TYPE_WITHDRAWAL.equals(transaction.getType())
                || transaction.getTimeEffective().before(startPeriod)
                || transaction.getTimeEffective().after(endPeriod)) {
            return;
        }

        Withdrawal withdrawal = (Withdrawal) transaction;
        BigDecimal amount = expensesPerCategory.get(withdrawal.getExpenseCategory());
        amount = amount.add(withdrawal.getAmount());
        expensesPerCategory.put(withdrawal.getExpenseCategory(), amount);

        totalExpenses = totalExpenses.add(withdrawal.getAmount());
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

        for (ExpenseCategory type: ExpenseCategory.values()) {
            BigDecimal expense = expensesPerCategory.get(type);
            lines.add("\t" + type.name() + ": " + NumberFormat.getCurrencyInstance().format(expense.doubleValue()));
        }

        lines.add("Total: " + NumberFormat.getCurrencyInstance().format(totalExpenses.doubleValue()));

        return lines.toArray(new String[lines.size()]);
    }
}
