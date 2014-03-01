package cs2340.woms.present;

import cs2340.woms.model.BaseModel;
import cs2340.woms.view.screens.LoginScreen;
import cs2340.woms.view.screens.MainScreen;

/**
 * The presenter class. This class handles most of the main application logic
 * and links the model to the view.
 */
public final class Presenter {

    private static BaseModel model = (BaseModel) DependencyManager.createImplementation(BaseModel.class);

    // No instances, all methods are class methods.
    private Presenter() { }

    public static void initMainScreen(final MainScreen screen) {
        screen.setLoginButtonBehavior(new Runnable() {
            @Override
            public void run() {
                screen.open(DependencyManager.getImplementation(LoginScreen.class));
            }
        });
    }

    public static void initLoginScreen(final LoginScreen screen) {
        screen.setLoginButtonBehavior(new Runnable() {
            @Override
            public void run() {
                String username = screen.getUsernameField();
                String password = screen.getPasswordField();
                if (model.login(username, password)) {
                    screen.open(DependencyManager.getImplementation(null)); //AccountManagementScreen
                } else {
                    screen.popup("Incorrect username or password.");
                }
            }
        });

        screen.setRegisterButtonBehavior(new Runnable() {
            @Override
            public void run() {
                screen.open(DependencyManager.getImplementation(null)); //RegistrationScreen
            }
        });
    }
}
