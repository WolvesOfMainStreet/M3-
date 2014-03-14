package org.homenet.dnoved1.woms.present;

import java.util.HashMap;
import java.util.Map;

import org.homenet.dnoved1.woms.model.Account;
import org.homenet.dnoved1.woms.model.ClientDatabase;
import org.homenet.dnoved1.woms.model.Transaction;
import org.homenet.dnoved1.woms.view.screens.TransactionCreationScreen;
import org.homenet.dnoved1.woms.view.screens.TransactionHistoryScreen;

/**
 * The presenter for the transaction history screen.
 */
public class TransactionHistoryPresenter {

    /**The screen that this is presenting.*/
    protected TransactionHistoryScreen screen;
    /**The account to get transactions from for display.*/
    protected Account account;

    /**
     * Creates a new presenter for the given transaction history screen for
     * the given account.
     *
     * @param screen the screen that this should present.
     * @param account the account to get transactions from for display.
     */
    public TransactionHistoryPresenter(TransactionHistoryScreen screen, Account account) {
        this.screen = screen;
        this.account = account;
        ClientDatabase.get().registerTransactionsObserver(account, screen.getTransactionListObserver());
    }

    /**
     * Should be called whenever the create deposit button is pressed. Will
     * open a new transaction creation screen for deposits.
     */
    public void onCreateDepositButtonPressed() {
        Map<String, String> args = new HashMap<String, String>();
        args.put(TransactionCreationScreen.TRANSACTION_TYPE, Transaction.TYPE_DEPOSIT);
        screen.open(DependencyManager.getImplementation(TransactionCreationScreen.class), args);
    }

    /**
     * Should be called whenever the create withdrawal button is pressed. Will
     * open a new transaction creation screen for withdrawals.
     */
    public void onCreateWithdrawalButtonPressed() {
        Map<String, String> args = new HashMap<String, String>();
        args.put(TransactionCreationScreen.TRANSACTION_TYPE, Transaction.TYPE_WITHDRAWAL);
        screen.open(DependencyManager.getImplementation(TransactionCreationScreen.class), args);
    }

    /**
     * Should be called whenever a transaction is selected. Currently does
     * nothing.
     *
     * @param transaction the selected transaction.
     */
    public void onTransactionSelected(Transaction transaction) { }
}
