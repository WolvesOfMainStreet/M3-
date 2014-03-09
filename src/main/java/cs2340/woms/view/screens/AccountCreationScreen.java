package cs2340.woms.view.screens;

/**
 * The account creation screen. The account creation screen consists of two
 * fields, one for the name of the account and the other for the starting
 * balance. This screen also has two buttons, one for confirming the creation
 * of the new account and the other for canceling.
 */
public interface AccountCreationScreen extends BaseScreen {

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
