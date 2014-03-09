package cs2340.woms.present;

import java.math.BigDecimal;
import java.util.Date;

import cs2340.woms.model.Account;
import cs2340.woms.model.BaseModel;
import cs2340.woms.model.Deposit;
import cs2340.woms.model.ExpenseCategory;
import cs2340.woms.model.Transaction;
import cs2340.woms.model.Withdrawal;
import cs2340.woms.view.screens.TransactionCreationScreen;

public class TransactionCreationPresenter {

    protected TransactionCreationScreen screen;
    protected String transactionType;
    protected Account account;

    public TransactionCreationPresenter(TransactionCreationScreen screen, Account account, String transactionType) {
        this.screen = screen;
        this.transactionType = transactionType;

        // Set the spinner contents if type is withdrawal
        if (Transaction.TYPE_WITHDRAWAL.equals(transactionType)) {
            ExpenseCategory[] categories = ExpenseCategory.values();
            String[] categoryStrings = new String[categories.length];
            for (int i = 0; i < categories.length; i++) {
                categoryStrings[i] = categories[i].name();
            }

            screen.setCategorySpinnerContents(categoryStrings);
        }
    }

    public void onConfirmButtonPressed() {
        BaseModel model = DependencyManager.getModel();

        String amountString = screen.getAmountField();
        String reason = screen.getReasonField();
        String categoryString = screen.getCategory();
        Date timeEffective = screen.getTimeEffectiveDate();
        Date timeEntered = new Date();
        BigDecimal amount = null;

        String error = null;

        if ("".equals(amountString)) {
            error = "Must set the amount.";
        } else if ("".equals(reason)) {
            if (transactionType.equals(Transaction.TYPE_DEPOSIT)) {
                error = "Must set the source of the deposit";
            } else if (transactionType.equals(Transaction.TYPE_WITHDRAWAL)) {
                error = "Must set the reason for the withdrawal";
            }
        }

        if (error == null) {
            try {
                amount = new BigDecimal(amountString);
            } catch (NumberFormatException e) {
                error = "Amount field is not a number.";
            }
        }

        if (error != null) {
            screen.popup(error);
        } else {
            Transaction transaction;
            if (transactionType.equals(Transaction.TYPE_DEPOSIT)) {
                transaction = new Deposit(reason, amount, timeEffective, timeEntered);
            } else if (transactionType.equals(Transaction.TYPE_WITHDRAWAL)) {
                ExpenseCategory category;
                try {
                    category = ExpenseCategory.valueOf(categoryString);
                } catch (Exception e) {
                    category = ExpenseCategory.OTHER;
                }

                transaction = new Withdrawal(category, reason, amount, timeEffective, timeEntered);
            } else {
                transaction = null;
            }

            if (transaction == null) {
                System.err.println("Invalid transaction type " + transactionType + ".");
            } else {
                model.addTransaction(account, transaction);
            }

            screen.close();
        }
    }

    public void onCancelButtonPressed() {
        screen.close();
    }
}
