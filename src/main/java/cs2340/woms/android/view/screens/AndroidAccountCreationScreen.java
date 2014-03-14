package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cs2340.woms.R;
import cs2340.woms.present.AccountCreationPresenter;
import cs2340.woms.view.screens.AccountCreationScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.AccountCreationScreen}.
 */
public class AndroidAccountCreationScreen extends AndroidBaseScreen implements AccountCreationScreen {

    /**The presenter for this screen.*/
    private AccountCreationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation_screen);
        this.presenter = new AccountCreationPresenter(this);
    }

    /**
     * Not for normal use. Is called by android whenever the confirm button is
     * pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onConfirmButtonPressed(View view) {
        presenter.onConfirmButtonPressed();
    }

    /**
     * Not for normal use. Is called by android whenever the cancel button is
     * pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onCancelButtonPressed(View view) {
        presenter.onCancelButtonPressed();
    }

    @Override
    public String getNameField() {
        EditText nameField = (EditText) this.findViewById(R.id.accountcreateFieldName);
        return nameField.getText().toString();
    }

    @Override
    public String getBalanceField() {
        EditText balanceField = (EditText) this.findViewById(R.id.accountcreateFieldBalance);
        return balanceField.getText().toString();
    }
}
