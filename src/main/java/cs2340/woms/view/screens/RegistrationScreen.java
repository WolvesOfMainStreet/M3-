package cs2340.woms.view.screens;

/**
 * The registration screen. The registration screen consists of three fields,
 * one for the username, one for the password, and one for confirming the
 * password. This screen also has two buttons, one for confirming the creation
 * of a new account and the second for canceling.
 */
public interface RegistrationScreen extends BaseScreen {

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

    /**
     * Returns the value in the confirm password field. Should never be null.
     *
     * @return the value in the confirm password field.
     */
    String getConfirmPasswordField();
}
