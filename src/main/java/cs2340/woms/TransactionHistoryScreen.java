package cs2340.woms;

import static cs2340.woms.TransactionCreationScreen.CREATE_TRANSACTION;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
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

    private FinanceAccount financeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history_screen);

        Bundle extraData = this.getIntent().getExtras();
        int financeAccountIndex = extraData.getInt("financeAccount");
        this.financeAccount = AndroidLoginManager.instance.getCurrentLogin().getAccounts().get(financeAccountIndex);

        //-----Set up Transaction List------------------------------------------

        ListView listview = (ListView) this.findViewById(R.id.transactionhistoryListTransaction);
        ObservableList<Transaction> updateableList = financeAccount.getTransactions();

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
                Intent intent = new Intent(TransactionHistoryScreen.this, TransactionCreationScreen.class);
                TransactionHistoryScreen.this.startActivityForResult(intent, CREATE_TRANSACTION);
            }
        });

        // TODO: add back button (go to account management screen)
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CREATE_TRANSACTION) {
            Bundle extras = data.getExtras();
            String amountString = extras.getString("amount");

            BigDecimal amount = new BigDecimal(amountString);
            Transaction newTransaction = new Transaction(amount);
            this.financeAccount.addTransaction(newTransaction);
        }
    }
}
