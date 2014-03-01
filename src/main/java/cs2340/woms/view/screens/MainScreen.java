package cs2340.woms.view.screens;

/**
 * The main screen. The main screen should be the entry point for the program
 * and has one button for logging in.
 */
public interface MainScreen extends BaseScreen {

    /**
     * Sets the behavior for when the login button is pressed.
     *
     * @param behavior the behavior to execute when the login button is pressed.
     */
    void setLoginButtonBehavior(Runnable behavior);
}
