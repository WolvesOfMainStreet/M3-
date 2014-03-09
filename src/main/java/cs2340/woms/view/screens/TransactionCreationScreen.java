package cs2340.woms.view.screens;

import java.util.Date;

/**
 * The transaction creation screen. The transaction creation screen has one
 * field for entering the amount the new transaction represents. This screen
 * also has two buttons, one for confirming the creation of the new transaction
 * and the other for canceling it.
 * <p>
 * This screen supports the following extra arguments:<li>
 *     transactionType - the type of transaction being created.
 * </li></p>
 */
public interface TransactionCreationScreen extends BaseScreen {

    String TRANSACTION_TYPE = "transactionType";

    /**
     * Returns the value in the amount field. Should never be null.
     *
     * @return the value in the amount field.
     */
    String getAmountField();

    /**
     * Returns the value in the reason field. Should never be null.
     *
     * @return the value in the reason field.
     */
    String getReasonField();

    /**
     * Returns the date in the time effective date field. Should never be null.
     *
     * @return the date in the time effective date field.
     */
    Date getTimeEffectiveDate();

    void setCategorySpinnerContents(String[] contents);

    String getCategory();
}
