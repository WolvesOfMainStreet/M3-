package cs2340.woms.android.view.screens;

import java.util.Calendar;
import java.util.Date;

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
import cs2340.woms.R;
import cs2340.woms.android.view.RunnableClickListener;
import cs2340.woms.model.Transaction;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.screens.TransactionCreationScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.TransactionCreationScreen}.
 */
public class AndroidTransactionCreationScreen extends AndroidBaseScreen implements TransactionCreationScreen {

    private static final String DATE_TIME_FORMAT = "%02d/%02d/%04d %02d:%02d";

    // Time effective fields
    private int year, month, day, hour, minute;

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

        EditText reasonField = (EditText) this.findViewById(R.id.transactioncreateFieldSource);
        if (type.equals(Transaction.TYPE_DEPOSIT)) {
            reasonField.setHint(R.string.source);
        } else if (type.equals(Transaction.TYPE_WITHDRAWAL)) {
            reasonField.setHint(R.string.reason);
        }

        //-----Set up time effective chooser------------------------------------

        // Initialize date/time variables
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        // Set up date/time button
        final Button dateTimeButton = (Button) this.findViewById(R.id.transactioncreateButtonDatetime);
        final Handler datetimeHandler = new Handler();

        datetimeHandler.post(new Runnable() {
            @Override
            public void run() {
                minute++;
                if (minute >= 60) {
                    minute = 0;
                    hour++;
                }
                if (hour >= 24) {
                    hour = 0;
                    day++;
                }
                // TODO: day/month/year handling. Much rarer, but still can happen

                dateTimeButton.setText(String.format(DATE_TIME_FORMAT, month + 1, day, year, hour, minute));

                // Update every minute
                datetimeHandler.postDelayed(this, 60000);
            }
        });
    }

    public void chooseDateTime(View view) {
        final Button dateTimeButton = (Button) view;

        final Dialog dateDialog = new DatePickerDialog(AndroidTransactionCreationScreen.this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int aYear, int aMonth, int aDay) {
                year = aYear;
                month = aMonth;
                day = aDay;
                dateTimeButton.setText(String.format(DATE_TIME_FORMAT, month + 1, day, year, hour, minute));
            }
        }, year, month, day);

        final Dialog timeDialog = new TimePickerDialog(AndroidTransactionCreationScreen.this, new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int aHour, int aMinute) {
                hour = aHour;
                minute = aMinute;
                dateTimeButton.setText(String.format(DATE_TIME_FORMAT, month + 1, day, year, hour, minute));
            }
        }, hour, minute, true);

        dateDialog.show();

        dateDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                timeDialog.show();
            }
        });
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
    public String getReasonField() {
        EditText reasonField = (EditText) this.findViewById(R.id.transactioncreateFieldSource);
        return reasonField.getText().toString();
    }

    @Override
    public Date getTimeEffectiveDate() {
        Calendar calendar = Calendar.getInstance();

        // Update with selected time values
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
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
