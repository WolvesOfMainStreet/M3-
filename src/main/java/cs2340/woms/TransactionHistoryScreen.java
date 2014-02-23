package cs2340.woms;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import cs2340.woms.account.FinanceAccount;
import cs2340.woms.account.Transaction;
import cs2340.woms.auth.AndroidLoginManager;

public class TransactionHistoryScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history_screen);

        Bundle extraData = this.getIntent().getExtras();
        int financeAccountIndex = extraData.getInt("financeAccount");
        FinanceAccount currentFinanceAccount = AndroidLoginManager.instance.getCurrentLogin().getAccounts().get(financeAccountIndex);

        //-----Set up Transaction List------------------------------------------

        ListView listview = (ListView) this.findViewById(R.id.transactionhistoryListTransaction);
        ObservableList<Transaction> updateableList = currentFinanceAccount.getTransactions();

        final BaseAdapter accountListAdapter = new ArrayAdapter<Transaction>(this, R.layout.account_listing, updateableList);
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

        //-----Set up New Transaction Button------------------------------------

        Button newTransactionButton = (Button) this.findViewById(R.id.transactionhistoryButtonNewtransaction);
        newTransactionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Link to Transaction creation screen
            }
        });
    }
}
