package cs2340.woms.present;

import cs2340.woms.view.screens.LoginScreen;
import cs2340.woms.view.screens.RegistrationScreen;
import cs2340.woms.view.screens.UserOverviewScreen;

public class LoginPresenter {

    protected LoginScreen screen;

    public LoginPresenter(LoginScreen screen) {
        this.screen = screen;
    }

    public void onLoginButtonPressed() {
        String username = screen.getUsernameField();
        String password = screen.getPasswordField();

        if (DependencyManager.getModel().login(username, password)) {
            screen.open(DependencyManager.getImplementation(UserOverviewScreen.class));
        } else {
            screen.popup("Incorrect username or password.");
        }
    }

    public void onRegisterButtonPressed() {
        screen.open(DependencyManager.getImplementation(RegistrationScreen.class));
    }
}
