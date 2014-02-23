package cs2340.woms;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cs2340.woms.account.FinanceAccount;
import cs2340.woms.auth.AndroidLoginManager;

public class AccountCreationScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation_screen);

        final EditText nameField = (EditText) this.findViewById(R.id.accountcreateFieldName);
        final EditText balanceField = (EditText) this.findViewById(R.id.accountcreateFieldBalance);

        Button cancelButton = (Button) this.findViewById(R.id.accountcreateButtonCancel);
        Button createAccountButton = (Button) this.findViewById(R.id.accountcreateButtonCreate);

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountCreationScreen.this.finish();
            }
        });

        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String balanceString = balanceField.getText().toString();
                BigDecimal balance = null;

                String error = null;

                if ("".equals(name)) {
                    error = "Name field cannot be empty.";
                } else if ("".equals(balanceString)) {
                    error = "Balance field cannot be empty.";
                }

                if (error == null) {
                    try {
                        balance = new BigDecimal(balanceString);
                    } catch (NumberFormatException e) {
                        error = "Balance field is not a number.";
                    }
                }

                if (error != null) {
                    Toast.makeText(AccountCreationScreen.this, error, Toast.LENGTH_SHORT).show();
                } else {
                    FinanceAccount newAccount = new FinanceAccount(name, balance);
                    AndroidLoginManager.instance.getCurrentLogin().addAccount(newAccount);
                    AccountCreationScreen.this.finish();
                }
            }
        });
    }
}
