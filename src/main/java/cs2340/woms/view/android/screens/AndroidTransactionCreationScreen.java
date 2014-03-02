package cs2340.woms.view.android.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import cs2340.woms.R;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.android.RunnableClickListener;
import cs2340.woms.view.screens.TransactionCreationScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.TransactionCreationScreen}.
 */
public class AndroidTransactionCreationScreen extends AndroidBaseScreen implements TransactionCreationScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_creation_screen);
        Presenter.initTransactionCreationScreen(this);
    }

    @Override
    public void setConfirmButtonBehavior(Runnable behavior) {
        Button confirmButton = (Button) this.findViewById(R.id.transactioncreateButtonCreate);
        confirmButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public void setCancelButtonBehavior(Runnable behavior) {
        Button cancelButton = (Button) this.findViewById(R.id.transactioncreateButtonCancel);
        cancelButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public String getAmountField() {
        EditText amountField = (EditText) this.findViewById(R.id.transactioncreateFieldAmount);
        return amountField.getText().toString();
    }
}
