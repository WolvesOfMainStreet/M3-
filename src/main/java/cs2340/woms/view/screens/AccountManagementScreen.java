package cs2340.woms.view.screens;

import cs2340.woms.model.DataSetObserver;
import cs2340.woms.model.FinanceAccount;
import cs2340.woms.view.ListSelectBehavior;

/**
 * The account management screen. The account management screen consists of one
 * button for creating new accounts and a list of current accounts. Individual
 * accounts can be selected from this list.
 */
public interface AccountManagementScreen extends BaseScreen {

    /**
     * Sets the behavior for when the create new account button is pressed.
     *
     * @param behavior the behavior to execute when the create new account
     * button is pressed.
     */
    void setCreateAccountButtonBehavior(Runnable behavior);

    /**
     * Get an observer for the list of accounts.
     *
     * @return an observer for the list of accounts.
     */
    DataSetObserver<FinanceAccount> getAccountListObserver();

    /**
     * Sets the behavior for when an account is selected from the account list.
     *
     * @param behavior the behavior to execute when an account is selected.
     */
    void setAccountListSelectBehavior(ListSelectBehavior<FinanceAccount> behavior);
}
