package org.homenet.dnoved1.woms.android.view.screens;

import java.util.Calendar;
import java.util.Date;

import org.homenet.dnoved1.woms.R;
import org.homenet.dnoved1.woms.model.ClientDatabase;
import org.homenet.dnoved1.woms.model.Transaction;
import org.homenet.dnoved1.woms.present.TransactionCreationPresenter;
import org.homenet.dnoved1.woms.view.screens.TransactionCreationScreen;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

/**
 * The android implementation of {@link cs2340.woms.view.screens.TransactionCreationScreen}.
 */
public class AndroidTransactionCreationScreen extends AndroidBaseScreen implements TransactionCreationScreen {

    /**The format string for displaying the selected time effective for the new transaction.*/
    private static final String DATE_TIME_FORMAT = "%1$tD %1$tR";

    /**The presenter for this screen.*/
    private TransactionCreationPresenter presenter;
    /**The currently selected time effective for the new transaction.*/
    private Calendar timeEffective;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_creation_screen);

        // Get arguments
        Bundle extras = this.getIntent().getExtras();
        String type = extras.getString(TransactionCreationScreen.TRANSACTION_TYPE);
        if (type == null) {
            System.err.println("No transaction type specified, defaulting to deposit.");
            type = Transaction.TYPE_DEPOSIT;
        }

        EditText reasonField = (EditText) this.findViewById(R.id.transactioncreateFieldSource);
        if (type.equals(Transaction.TYPE_DEPOSIT)) {
            reasonField.setHint(R.string.source);
        } else if (type.equals(Transaction.TYPE_WITHDRAWAL)) {
            reasonField.setHint(R.string.reason);
        }

        //-----Set up time effective chooser------------------------------------

        // Initialize date/time variables
        timeEffective = Calendar.getInstance();

        // Set up date/time button
        final Button dateTimeButton = (Button) this.findViewById(R.id.transactioncreateButtonDatetime);
        final Handler datetimeHandler = new Handler();

        dateTimeButton.setText(String.format(DATE_TIME_FORMAT, timeEffective));
        datetimeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeEffective.add(Calendar.MINUTE, 1);
                dateTimeButton.setText(String.format(DATE_TIME_FORMAT, timeEffective));

                // Update every minute
                datetimeHandler.postDelayed(this, 60000);
            }
        }, 60000);
        // TODO: stop auto updating time if it has been selected by the user.

        this.presenter = new TransactionCreationPresenter(this, ClientDatabase.get().getCurrentAccount(), type);
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

    /**
     * Not for normal use. Is called by android whenever the button displaying
     * the time effective for the new transaction is pressed, as defined in
     * this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void chooseDateTime(View view) {
        if (!(view instanceof Button)) {
            throw new IllegalArgumentException("View must be a button.");
        }

        final Button dateTimeButton = (Button) view;

        final Dialog dateDialog = new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                timeEffective.set(Calendar.YEAR, year);
                timeEffective.set(Calendar.MONTH, monthOfYear);
                timeEffective.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateTimeButton.setText(String.format(DATE_TIME_FORMAT, timeEffective));
            }
        }, timeEffective.get(Calendar.YEAR), timeEffective.get(Calendar.MONTH), timeEffective.get(Calendar.DAY_OF_MONTH));

        final Dialog timeDialog = new TimePickerDialog(this, new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeEffective.set(Calendar.HOUR_OF_DAY, hourOfDay);
                timeEffective.set(Calendar.MINUTE, minute);
                dateTimeButton.setText(String.format(DATE_TIME_FORMAT, timeEffective));
            }
        }, timeEffective.get(Calendar.HOUR_OF_DAY), timeEffective.get(Calendar.MINUTE), true);

        dateDialog.show();

        dateDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                timeDialog.show();
            }
        });
    }

    @Override
    public String getAmountField() {
        EditText amountField = (EditText) this.findViewById(R.id.transactioncreateFieldAmount);
        return amountField.getText().toString();
    }

    @Override
    public String getReasonField() {
        EditText reasonField = (EditText) this.findViewById(R.id.transactioncreateFieldSource);
        return reasonField.getText().toString();
    }

    @Override
    public Date getTimeEffectiveDate() {
        return timeEffective.getTime();
    }

    @Override
    public void setCategorySpinnerContents(String[] contents) {
        Spinner categorySpinner = (Spinner) this.findViewById(R.id.transactioncreateSpinnerCategory);
        categorySpinner.setVisibility(View.VISIBLE);
        BaseAdapter adapter = new ArrayAdapter<String>(this, R.layout.account_listing, contents);
        categorySpinner.setAdapter(adapter);
    }

    @Override
    public String getCategory() {
        Spinner categorySpinner = (Spinner) this.findViewById(R.id.transactioncreateSpinnerCategory);
        if (categorySpinner.getVisibility() == View.VISIBLE) {
            return (String) categorySpinner.getSelectedItem();
        } else {
            return null;
        }
    }
}
