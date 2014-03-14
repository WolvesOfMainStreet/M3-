package org.homenet.dnoved1.woms.present;

import org.homenet.dnoved1.woms.model.ClientDatabase;
import org.homenet.dnoved1.woms.model.User;
import org.homenet.dnoved1.woms.view.screens.RegistrationScreen;

/**
 * The presenter for the registration screen.
 */
public class RegistrationPresenter {

    /**The screen that this is presenting.*/
    protected RegistrationScreen screen;

    /**
     * Creates a new presenter for the given registration screen.
     *
     * @param screen the screen that this should present.
     */
    public RegistrationPresenter(RegistrationScreen screen) {
        this.screen = screen;
    }

    /**
     * Should be called whenever the confirm button is pressed. Will retrieve
     * the user-specified information for the new user to register and attempt
     * to register them, creating a pop-up in the case of an error.
     */
    public void onConfirmButtonPressed() {
        String username = screen.getUsernameField();
        String password1 = screen.getPasswordField();
        String password2 = screen.getConfirmPasswordField();

        String error = null;

        if (username.equals("")) {
            error = "Username cannot be empty.";
        } else if (password1.equals("") || password2.equals("")) {
            error = "Password fields cannot be empty.";
        } else if (!password1.equals(password2)) {
            error = "Password fields do no match.";
        } else if (!ClientDatabase.get().register(new User(username, password1))) {
            error = "Could not create account with that username.";
        }

        if (error != null) {
            screen.popup(error);
        } else {
            screen.close();
        }
    }

    /**
     * Should be called whenever the cancel button is pressed. Will simply
     * close the registration screen.
     */
    public void onCancelButtonPressed() {
        screen.close();
    }
}
