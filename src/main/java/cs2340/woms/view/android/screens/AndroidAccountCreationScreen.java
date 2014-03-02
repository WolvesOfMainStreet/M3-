package cs2340.woms.view.android.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import cs2340.woms.R;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.android.RunnableClickListener;
import cs2340.woms.view.screens.AccountCreationScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.AccountCreationScreen}.
 */
public class AndroidAccountCreationScreen extends AndroidBaseScreen implements AccountCreationScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation_screen);
        Presenter.initAccountCreationScreen(this);
    }

    @Override
    public void setConfirmButtonBehavior(Runnable behavior) {
        Button confirmButton = (Button) this.findViewById(R.id.accountcreateButtonCreate);
        confirmButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public void setCancelButtonBehavior(Runnable behavior) {
        Button cancelButton = (Button) this.findViewById(R.id.accountcreateButtonCancel);
        cancelButton.setOnClickListener(new RunnableClickListener(behavior));
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
