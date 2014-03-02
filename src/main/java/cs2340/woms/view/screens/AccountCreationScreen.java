package cs2340.woms.view.screens;

/**
 * The account creation screen. The account creation screen consists of two
 * fields, one for the name of the account and the other for the starting
 * balance. This screen also has two buttons, one for confirming the creation
 * of the new account and the other for canceling.
 */
public interface AccountCreationScreen extends BaseScreen {

    /**
     * Sets the behavior for when the confirm button is pressed.
     *
     * @param behavior the behavior to execute when the confirm button is
     * pressed.
     */
    void setConfirmButtonBehavior(Runnable behavior);

    /**
     * Sets the behavior for when the cancel button is pressed.
     *
     * @param behavior the behavior to execute when the cancel button is
     * pressed.
     */
    void setCancelButtonBehavior(Runnable behavior);

    /**
     * Returns the value in the name field. Should never be null.
     *
     * @return the value in the name field.
     */
    String getNameField();

    /**
     * Returns the value in the balance field. Should never be null.
     *
     * @return the value in the balance field.
     */
    String getBalanceField();
}
