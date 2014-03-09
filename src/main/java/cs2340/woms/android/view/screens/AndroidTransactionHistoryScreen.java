package cs2340.woms.android.view.screens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cs2340.woms.R;
import cs2340.woms.model.Account;
import cs2340.woms.model.ClientDatabase;
import cs2340.woms.model.DataSetObserver;
import cs2340.woms.model.Transaction;
import cs2340.woms.present.TransactionHistoryPresenter;
import cs2340.woms.view.screens.TransactionHistoryScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.TransactionHistoryScreen}.
 */
public class AndroidTransactionHistoryScreen extends AndroidBaseScreen implements TransactionHistoryScreen {

    private TransactionHistoryPresenter presenter;

    private List<Transaction> transactionList;
    private BaseAdapter transactionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history_screen);

        Account account = ClientDatabase.get().getCurrentAccount();

        transactionList = new ArrayList<Transaction>();
        transactionListAdapter = new ArrayAdapter<Transaction>(this, R.layout.account_listing, transactionList);
        ListView listview = (ListView) this.findViewById(R.id.transactionhistoryListTransaction);
        listview.setAdapter(transactionListAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTransactionSelected((Transaction) parent.getItemAtPosition(position));
            }
        });

        this.presenter = new TransactionHistoryPresenter(this, account);
    }

    public void onCreateDepositButtonPressed(View view) {
        presenter.onCreateDepositButtonPressed();
    }

    public void onCreateWithdrawalButtonPressed(View view) {
        presenter.onCreateWithdrawalButtonPressed();
    }

    public void onTransactionSelected(Transaction transaction) {
        presenter.onTransactionSelected(transaction);
    }

    @Override
    public DataSetObserver<Transaction> getTransactionListObserver() {
        return new DataSetObserver<Transaction>() {
            @Override
            public void update(Collection<Transaction> dataset) {
                //TODO: this can likely be done more efficiently
                transactionList.clear();
                transactionList.addAll(dataset);
                transactionListAdapter.notifyDataSetChanged();
            }
        };
    }
}
