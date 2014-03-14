package cs2340.woms.present;

import java.math.BigDecimal;
import java.util.Date;

import cs2340.woms.model.Account;
import cs2340.woms.model.ClientDatabase;
import cs2340.woms.model.Deposit;
import cs2340.woms.model.ExpenseCategory;
import cs2340.woms.model.Transaction;
import cs2340.woms.model.Withdrawal;
import cs2340.woms.view.screens.TransactionCreationScreen;

/**
 * The presenter for the transaction creation screen.
 */
public class TransactionCreationPresenter {

    /**The screen that this is presenting.*/
    protected TransactionCreationScreen screen;
    /**The type of transaction being created. See {@link TransactionCreationScreen#TRANSACTION_TYPE}.*/
    protected String transactionType;
    /**The account for which the new transaction is being created for.*/
    protected Account account;

    /**
     * Creates a new presenter for the given transaction creation screen. The
     * new transaction will be of the specified type and will be created for
     * the given account.
     *
     * @param screen the screen that this should present.
     * @param account the account to create the transaction for.
     * @param transactionType the type of transaction being created. See
     * {@link TransactionCreationScreen#TRANSACTION_TYPE}.
     */
    public TransactionCreationPresenter(TransactionCreationScreen screen, Account account, String transactionType) {
        this.screen = screen;
        this.transactionType = transactionType;
        this.account = account;

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

    /**
     * Should be called whenever the confirm button is pressed. Will retrieve
     * the user-defined information for the new transaction and create it for
     * the account specified during construction. Will create a pop-up in the
     * case of an error.
     */
    public void onConfirmButtonPressed() {
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
                ClientDatabase.get().addTransaction(account, transaction);
            }

            screen.close();
        }
    }

    /**
     * Should be called whenever the cancel button is pressed. Will simply
     *  close the transaction creation screen.
     */
    public void onCancelButtonPressed() {
        screen.close();
    }
}
