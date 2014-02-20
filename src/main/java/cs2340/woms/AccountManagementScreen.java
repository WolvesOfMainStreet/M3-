package cs2340.woms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import cs2340.woms.account.FinanceAccount;
import cs2340.woms.auth.AndroidLoginManager;

public class AccountManagementScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_management_screen);

        ListView listview = (ListView) this.findViewById(R.id.accountmanageListAccount);

        // Shouldn't happen, but if no one is logged in there is no reason to
        // be viewing their non-existent accounts
        if (AndroidLoginManager.instance.getCurrentLogin() == null) {
            this.finish();
        }

        listview.setAdapter(new ArrayAdapter<FinanceAccount>(
                this, R.layout.account_listing, AndroidLoginManager.instance.getCurrentLogin().getAccounts()));

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        Button createAccountButton = (Button) this.findViewById(R.id.accountmanageButtonNewaccount);

        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountManagementScreen.this, AccountCreationScreen.class);
                startActivity(intent);
            }
        });
    }
}
