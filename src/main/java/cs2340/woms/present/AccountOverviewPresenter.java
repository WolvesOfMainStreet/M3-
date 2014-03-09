package cs2340.woms.present;

import cs2340.woms.model.Account;
import cs2340.woms.model.ClientDatabase;
import cs2340.woms.view.screens.AccountCreationScreen;
import cs2340.woms.view.screens.AccountOverviewScreen;
import cs2340.woms.view.screens.TransactionHistoryScreen;

public class AccountOverviewPresenter {

    protected AccountOverviewScreen screen;

    public AccountOverviewPresenter(AccountOverviewScreen screen) {
        this.screen = screen;
        ClientDatabase.get().registerAccountsObserver(screen.getAccountListObserver());
    }

    public void onCreateAccountButtonPressed() {
        screen.open(DependencyManager.getImplementation(AccountCreationScreen.class));
    }

    public void onAccountSelected(Account account) {
        ClientDatabase.get().setCurrentAccount(account);
        screen.open(DependencyManager.getImplementation(TransactionHistoryScreen.class));
    }
}
