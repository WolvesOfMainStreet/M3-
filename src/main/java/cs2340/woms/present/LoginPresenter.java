package cs2340.woms.present;

import cs2340.woms.model.ClientDatabase;
import cs2340.woms.model.User;
import cs2340.woms.view.screens.LoginScreen;
import cs2340.woms.view.screens.RegistrationScreen;
import cs2340.woms.view.screens.UserOverviewScreen;

/**
 * The presenter for the login screen.
 */
public class LoginPresenter {

    /**The screen that this is presenting.*/
    protected LoginScreen screen;

    /**
     * Creates a new presenter for the given login screen.
     *
     * @param screen the screen that this should present.
     */
    public LoginPresenter(LoginScreen screen) {
        this.screen = screen;
    }

    /**
     * Should be called whenever the login button is pressed. Will retrieve
     * the login information and attempt to log the user in, creating a pop-up
     * in the case of an error.
     */
    public void onLoginButtonPressed() {
        String username = screen.getUsernameField();
        String password = screen.getPasswordField();

        if (ClientDatabase.get().login(new User(username, password))) {
            screen.open(DependencyManager.getImplementation(UserOverviewScreen.class));
        } else {
            screen.popup("Incorrect username or password.");
        }
    }

    /**
     * Should be called whenever the register new user button is pressed. Will
     * open the registration screen.
     */
    public void onRegisterButtonPressed() {
        screen.open(DependencyManager.getImplementation(RegistrationScreen.class));
    }
}
