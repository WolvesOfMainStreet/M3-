package cs2340.woms.android.view.screens;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import cs2340.woms.R;
import cs2340.woms.android.view.RunnableClickListener;
import cs2340.woms.model.Transaction;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.screens.TransactionCreationScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.TransactionCreationScreen}.
 */
public class AndroidTransactionCreationScreen extends AndroidBaseScreen implements TransactionCreationScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments
        Bundle extras = this.getIntent().getExtras();
        String type = extras.getString(TransactionCreationScreen.TRANSACTION_TYPE);
        if (type == null) {
            System.out.println("No transaction type specified, defaulting to deposit.");
            type = Transaction.TYPE_DEPOSIT;
        }

        setContentView(R.layout.transaction_creation_screen);
        Presenter.initTransactionCreationScreen(this, type);
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

    @Override
    public Date getTimeEffectiveDate() {
        DatePicker date = (DatePicker) this.findViewById(R.id.transactioncreateDatePicker);
        TimePicker time = (TimePicker) this.findViewById(R.id.transactioncreateTimePicker);

        Calendar c = Calendar.getInstance();
        c.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute());
        return c.getTime();
    }
}
