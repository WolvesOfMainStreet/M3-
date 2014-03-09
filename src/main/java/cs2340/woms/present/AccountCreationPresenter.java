package cs2340.woms.present;

import java.math.BigDecimal;

import cs2340.woms.model.Account;
import cs2340.woms.view.screens.AccountCreationScreen;

public class AccountCreationPresenter {

    protected AccountCreationScreen screen;

    public AccountCreationPresenter(AccountCreationScreen screen) {
        this.screen = screen;
    }

    public void onConfirmButtonPressed() {
        String name = screen.getNameField();
        String balanceString = screen.getBalanceField();
        BigDecimal balance = null;

        String error = null;

        if ("".equals(name)) {
            error = "Name field cannot be empty.";
        } else if ("".equals(balanceString)) {
            error = "Balance field cannot be empty.";
        }

        if (error == null) {
            try {
                balance = new BigDecimal(balanceString);
            } catch (NumberFormatException e) {
                error = "Balance field is not a number.";
            }
        }

        if (error != null) {
            screen.popup(error);
        } else {
            Account account = new Account(name, balance);
            DependencyManager.getModel().addAccount(account);
            screen.close();
        }
    }

    public void onCancelButtonPressed() {
        screen.close();
    }
}
