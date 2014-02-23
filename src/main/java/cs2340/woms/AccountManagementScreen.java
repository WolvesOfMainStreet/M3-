package cs2340.woms;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import cs2340.woms.account.FinanceAccount;
import cs2340.woms.auth.AndroidLoginManager;

public class AccountManagementScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_management_screen);

        //-----Set up Account List----------------------------------------------

        ListView listview = (ListView) this.findViewById(R.id.accountmanageListAccount);

        // Shouldn't happen, but if no one is logged in there is no reason to
        // be viewing their non-existent accounts
        if (AndroidLoginManager.instance.getCurrentLogin() == null) {
            this.finish();
        }

        // Much of the following is setting up the account list adapter to
        // automatically recieve updates to the account list. There is likely
        // a simpler way which, if discovered, should replace this.
        ObservableList<FinanceAccount> updateableList = AndroidLoginManager.instance.getCurrentLogin().getAccounts();

        final BaseAdapter accountListAdapter = new ArrayAdapter<FinanceAccount>(this, R.layout.account_listing, updateableList);
        updateableList.registerObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                accountListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                accountListAdapter.notifyDataSetInvalidated();
            }
        });
        listview.setAdapter(accountListAdapter);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AccountManagementScreen.this, TransactionHistoryScreen.class);
                intent.putExtra("financeAccount", position);
                startActivity(intent);
            }
        });

        //-----Set up Account Creation Button----------------------------------

        Button createAccountButton = (Button) this.findViewById(R.id.accountmanageButtonNewaccount);

        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountManagementScreen.this, AccountCreationScreen.class);
                startActivity(intent);
            }
        });

        // TODO: add back button (logout)
    }
}
