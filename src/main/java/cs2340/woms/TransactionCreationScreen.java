package cs2340.woms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An activity for creating a transaction. This activity should be created
 * through {@link #Activity.startActivityForResult(Intent, int)}. This activity
 * will return the data for a new transaction with the following key-value
 * pairs:
 *     amount: String - The amount of money this transaction represents.
 */
public class TransactionCreationScreen extends Activity {

    /*The request code for creating a new transaction.*/
    public static final int CREATE_TRANSACTION = 0x0008;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_creation_screen);

        //-----Set up the Cancel Button-----------------------------------------

        Button cancelButton = (Button) this.findViewById(R.id.transactioncreateButtonCancel);

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionCreationScreen.this.finish();
            }
        });

        //-----Set up the Confirm Button----------------------------------------

        Button confirmButton = (Button) this.findViewById(R.id.transactioncreateButtonCreate);
        final EditText amountField = (EditText) this.findViewById(R.id.transactioncreateFieldAmount);

        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountField.getText().toString();

                String error = null;

                if ("".equals(amount)) {
                    error = "Must set the amount.";
                }

                if (error != null) {
                    Toast.makeText(TransactionCreationScreen.this, error, Toast.LENGTH_SHORT).show();
                } else {
                    Intent result = new Intent();
                    result.putExtra("amount", amount);
                    TransactionCreationScreen.this.setResult(Activity.RESULT_OK, result);
                    TransactionCreationScreen.this.finish();
                }
            }
        });
    }
}
