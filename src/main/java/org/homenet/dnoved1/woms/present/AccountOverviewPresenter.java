package org.homenet.dnoved1.woms.present;

import org.homenet.dnoved1.woms.model.Account;
import org.homenet.dnoved1.woms.model.ClientDatabase;
import org.homenet.dnoved1.woms.view.screens.AccountCreationScreen;
import org.homenet.dnoved1.woms.view.screens.AccountOverviewScreen;
import org.homenet.dnoved1.woms.view.screens.TransactionHistoryScreen;

/**
 * The presenter for the account overview screen.
 */
public class AccountOverviewPresenter {

    /**The screen that this is presenting.*/
    protected AccountOverviewScreen screen;

    /**
     * Creates a new presenter for the given account overview screen.
     *
     * @param screen the screen that this should present.
     */
    public AccountOverviewPresenter(AccountOverviewScreen screen) {
        this.screen = screen;
        ClientDatabase.get().registerAccountsObserver(screen.getAccountListObserver());
    }

    /**
     * Should be called whenever the 'create new account' button is pressed.
     * Will open a new account creation screen.
     */
    public void onCreateAccountButtonPressed() {
        screen.open(DependencyManager.getImplementation(AccountCreationScreen.class));
    }

    /**
     * Should be called whenever a specific account is selected. Will open the
     * transaction history screen for the given account.
     *
     * @param account the account that has been selected.
     */
    public void onAccountSelected(Account account) {
        ClientDatabase.get().setCurrentAccount(account);
        screen.open(DependencyManager.getImplementation(TransactionHistoryScreen.class));
    }
}
