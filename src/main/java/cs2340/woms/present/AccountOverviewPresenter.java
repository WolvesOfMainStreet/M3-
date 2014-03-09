package cs2340.woms.present;

import java.util.HashMap;
import java.util.Map;

import cs2340.woms.model.Account;
import cs2340.woms.view.screens.AccountCreationScreen;
import cs2340.woms.view.screens.AccountOverviewScreen;
import cs2340.woms.view.screens.TransactionHistoryScreen;

public class AccountOverviewPresenter {

    protected AccountOverviewScreen screen;

    public AccountOverviewPresenter(AccountOverviewScreen screen) {
        this.screen = screen;
        DependencyManager.getModel().registerAccountsObserver(screen.getAccountListObserver());
    }

    public void onCreateAccountButtonPressed() {
        screen.open(DependencyManager.getImplementation(AccountCreationScreen.class));
    }

    public void onAccountSelected(Account account) {
        Map<String, String> args = new HashMap<String, String>();
        args.put(TransactionHistoryScreen.ACCOUNT_NAME, account.getName());
        args.put(TransactionHistoryScreen.ACCOUNT_BALANCE, account.getBalance().toEngineeringString());
        screen.open(DependencyManager.getImplementation(TransactionHistoryScreen.class), args);
    }
}
