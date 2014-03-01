package cs2340.woms.view.screens;

/**
 * The login screen. The login screen consists of two fields, one for usernames
 * and the other for passwords, as well as two buttons, one for logging in with
 * the username and password in the aforementioned fields and another for
 * registering a new account.
 */
public interface LoginScreen extends BaseScreen {

    /**
     * Sets the behavior for when the login button is pressed.
     *
     * @param behavior the behavior to execute when the login button is pressed.
     */
    void setLoginButtonBehavior(Runnable behavior);

    /**
     * Sets the behavior for when the register button is pressed.
     *
     * @param behavior the behavior to execute when the register button is
     * pressed.
     */
    void setRegisterButtonBehavior(Runnable behavior);

    /**
     * Returns the value in the username field. Should never be null.
     *
     * @return the value in the username field.
     */
    String getUsernameField();

    /**
     * Returns the value in the password field. Should never be null.
     *
     * @return the value in the password field.
     */
    String getPasswordField();
}
