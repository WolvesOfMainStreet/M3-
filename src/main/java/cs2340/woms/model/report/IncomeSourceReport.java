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
import java.util.Map.Entry;

import cs2340.woms.model.Account;
import cs2340.woms.model.Deposit;
import cs2340.woms.model.Transaction;
import cs2340.woms.model.User;

/**
 * A report on a single user's income sources during a given period.
 */
public class IncomeSourceReport implements Report {

    /**The user which is currently being visited.*/
    private User currentUser;

    /**The user to generate this report for.*/
    private User user;
    /**The starting period for this report.*/
    private Date start;
    /**The ending period for this report.*/
    private Date end;
    /**A map of income source to the amount attributed to it.*/
    private Map<String, BigDecimal> sourceToIncome;

    /**
     * Creates a new income source report for the given user from the given
     * start date to the given end date.
     *
     * @param user the user to get income source information from.
     * @param start the beginning of the period to get deposits from.
     * @param end the end of the period to get deposits from.
     */
    public IncomeSourceReport(User user, Date start, Date end) {
        this.user = user;
        this.start = start;
        this.end = end;
        this.sourceToIncome = new HashMap<String, BigDecimal>();
    }

    @Override
    public void visit(User user) {
        currentUser = user;
    }

    @Override
    public void visit(Account account) { }

    @Override
    public void visit(Transaction transaction) {
        if (!user.equals(currentUser) || !(transaction instanceof Deposit)
                || transaction.getTimeEffective().before(start)
                || transaction.getTimeEffective().after(end)) {
            return;
        }

        Deposit deposit = (Deposit) transaction;

        // If the source has not been encountered previously, initialize a new
        // BigDecimal object for it.
        BigDecimal income = sourceToIncome.get(deposit.getSource());
        if (income == null) {
            income = new BigDecimal(0, MathContext.DECIMAL32);
        }

        income = income.add(deposit.getAmount());
        sourceToIncome.put(deposit.getSource(), income);
    }

    @Override
    public String oneLineString() {
        return "Income Source Report for " + user.getUsername();
    }

    @Override
    public String[] multiLineString() {
        List<String> lines = new ArrayList<String>();

        lines.add("Spending Category Report for " + user.getUsername());
        lines.add("\t" + SimpleDateFormat.getDateInstance().format(start)
                + " - " + SimpleDateFormat.getDateInstance().format(end));

        BigDecimal total = new BigDecimal(0, MathContext.DECIMAL32);
        for (Entry<String, BigDecimal> entry: sourceToIncome.entrySet()) {
            String source = entry.getKey();
            BigDecimal amount = entry.getValue();
            lines.add("\t" + source + ": " + NumberFormat.getCurrencyInstance().format(amount.doubleValue()));
            total = total.add(amount);
        }

        lines.add("Total: " + NumberFormat.getCurrencyInstance().format(total.doubleValue()));

        return lines.toArray(new String[lines.size()]);
    }
}
