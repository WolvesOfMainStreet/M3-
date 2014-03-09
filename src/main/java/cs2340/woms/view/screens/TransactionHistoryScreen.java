package cs2340.woms.view.screens;

import cs2340.woms.model.DataSetObserver;
import cs2340.woms.model.Transaction;

/**
 * The transaction history screen. The transaction history screen has one
 * button for creating new transactions as well as a list of all the current
 * transactions. Individual transactions can be selected from this list.
 */
public interface TransactionHistoryScreen extends BaseScreen {

    /**
     * Get an observer for the list of transactions.
     *
     * @return an observer for the list of transactions.
     */
    DataSetObserver<Transaction> getTransactionListObserver();
}
