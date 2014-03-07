package cs2340.woms.view.screens;

import cs2340.woms.model.DataSetObserver;
import cs2340.woms.model.Transaction;
import cs2340.woms.view.ListSelectBehavior;

/**
 * The transaction history screen. The transaction history screen has one
 * button for creating new transactions as well as a list of all the current
 * transactions. Individual transactions can be selected from this list.
 */
public interface TransactionHistoryScreen extends BaseScreen {

    /**
     * Sets the behavior for when the create deposit button is pressed.
     *
     * @param behavior the behavior to execute when the create deposit
     * button is pressed.
     */
    void setCreateDepositButtonBehavior(Runnable behavior);

    /**
     * Sets the behavior for when the create withdrawal button is pressed.
     *
     * @param behavior the behavior to execute when the create withdrawal
     * button is pressed.
     */
    void setCreateWithdrawalButtonBehavior(Runnable behavior);

    /**
     * Get an observer for the list of transactions.
     *
     * @return an observer for the list of transactions.
     */
    DataSetObserver<Transaction> getTransactionListObserver();

    /**
     * Sets the behavior for when a transaction is selected from the
     * transaction list.
     *
     * @param behavior the behavior to execute when a transaction is selected.
     */
    void setTransactionListSelectBehavior(ListSelectBehavior<Transaction> behavior);
}
