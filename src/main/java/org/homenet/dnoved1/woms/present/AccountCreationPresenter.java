package org.homenet.dnoved1.woms.present;

import java.math.BigDecimal;

import org.homenet.dnoved1.woms.model.Account;
import org.homenet.dnoved1.woms.model.ClientDatabase;
import org.homenet.dnoved1.woms.view.screens.AccountCreationScreen;

/**
 * The presenter for the account creation screen.
 */
public class AccountCreationPresenter {

    /**The screen that this is presenting.*/
    protected AccountCreationScreen screen;

    /**
     * Creates a new presenter for the given account creation screen.
     *
     * @param screen the screen that this should present.
     */
    public AccountCreationPresenter(AccountCreationScreen screen) {
        this.screen = screen;
    }

    /**
     * Should be called whenever the confirm button is pressed. Will retrieve
     * all the new account information and attempt to create a new account,
     * creating a pop-up in the case of an error.
     */
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

        if (error == null) {
            Account account = new Account(name, balance);
            if (ClientDatabase.get().getAllAccounts().contains(account)) {
                error = "An account with that name already exists.";
            }
        }

        if (error != null) {
            screen.popup(error);
        } else {
            Account account = new Account(name, balance);
            ClientDatabase.get().addAccount(account);
            screen.close();
        }
    }

    /**
     * Should be called whenever the cancel button is pressed. Will simply
     * close the screen.
     */
    public void onCancelButtonPressed() {
        screen.close();
    }
}
