package org.homenet.dnoved1.woms.present;

import java.math.BigDecimal;
import java.util.Date;

import org.homenet.dnoved1.woms.model.Account;
import org.homenet.dnoved1.woms.model.ClientDatabase;
import org.homenet.dnoved1.woms.model.Deposit;
import org.homenet.dnoved1.woms.model.ExpenseCategory;
import org.homenet.dnoved1.woms.model.Transaction;
import org.homenet.dnoved1.woms.model.Withdrawal;
import org.homenet.dnoved1.woms.view.screens.TransactionCreationScreen;

/**
 * The presenter for the transaction creation screen.
 */
public class TransactionCreationPresenter {

    /**The screen that this is presenting.*/
    protected TransactionCreationScreen screen;
    /**The type of transaction being created. See {@link TransactionCreationScreen#TRANSACTION_TYPE}.*/
    protected Transaction.Type transactionType;
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
    public TransactionCreationPresenter(TransactionCreationScreen screen, Account account, Transaction.Type transactionType) {
        this.screen = screen;
        this.transactionType = transactionType;
        this.account = account;

        // Set the spinner contents if type is withdrawal
        if (transactionType == Transaction.Type.WITHDRAWAL) {
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

        // Check that input fields are not null
        if ("".equals(amountString)) {
            screen.popup("Must set the amount.");
            return;
        }
        if ("".equals(reason)) {
            if (transactionType == Transaction.Type.DEPOSIT) {
                screen.popup("Must set the source of the deposit");
            } else if (transactionType == Transaction.Type.WITHDRAWAL) {
                screen.popup("Must set the reason for the withdrawal");
            }
            return;
        }

        // Check that amount is a number.
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountString);
        } catch (NumberFormatException e) {
            screen.popup("Amount field is not a number.");
            return;
        }

        Date timeEffective = screen.getTimeEffectiveDate();
        Date timeEntered = new Date();

        Transaction transaction;
        switch (transactionType) {
            case DEPOSIT:
                transaction = new Deposit(reason, amount, timeEffective, timeEntered);
                break;
            case WITHDRAWAL:
                ExpenseCategory category;
                try {
                    category = ExpenseCategory.valueOf(screen.getCategory());
                } catch (IllegalArgumentException e) {
                    category = ExpenseCategory.OTHER;
                }

                transaction = new Withdrawal(category, reason, amount, timeEffective, timeEntered);
                break;
            default:
                // Should never happen
                System.err.println("Invalid transaction type " + transactionType + ".");
                return;
        }

        ClientDatabase.get().addTransaction(account, transaction);
        screen.close();
    }

    /**
     * Should be called whenever the cancel button is pressed. Will simply
     *  close the transaction creation screen.
     */
    public void onCancelButtonPressed() {
        screen.close();
    }
}
