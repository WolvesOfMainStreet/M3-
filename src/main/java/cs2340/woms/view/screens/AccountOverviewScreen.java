package cs2340.woms.view.screens;

import cs2340.woms.model.Account;
import cs2340.woms.model.DataSetObserver;

/**
 * The account management screen. The account management screen consists of one
 * button for creating new accounts and a list of current accounts. Individual
 * accounts can be selected from this list.
 */
public interface AccountOverviewScreen extends BaseScreen {

    /**
     * Get an observer for the list of accounts.
     *
     * @return an observer for the list of accounts.
     */
    DataSetObserver<Account> getAccountListObserver();
}
