package cs2340.woms.present;

import cs2340.woms.model.ClientDatabase;
import cs2340.woms.model.User;
import cs2340.woms.view.screens.RegistrationScreen;

public class RegistrationPresenter {

    protected RegistrationScreen screen;

    public RegistrationPresenter(RegistrationScreen screen) {
        this.screen = screen;
    }

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

    public void onCancelButtonPressed() {
        screen.close();
    }
}
