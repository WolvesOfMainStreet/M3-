package cs2340.woms.android.view.screens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import cs2340.woms.R;
import cs2340.woms.android.view.ItemClickListSelect;
import cs2340.woms.android.view.RunnableClickListener;
import cs2340.woms.model.DataSetObserver;
import cs2340.woms.model.Transaction;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.ListSelectBehavior;
import cs2340.woms.view.screens.TransactionHistoryScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.TransactionHistoryScreen}.
 */
public class AndroidTransactionHistoryScreen extends AndroidBaseScreen implements TransactionHistoryScreen {

    private List<Transaction> transactionList;
    private BaseAdapter transactionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history_screen);

        transactionList = new ArrayList<Transaction>();
        transactionListAdapter = new ArrayAdapter<Transaction>(this, R.layout.account_listing, transactionList);
        ((ListView) this.findViewById(R.id.transactionhistoryListTransaction)).setAdapter(transactionListAdapter);

        Presenter.initTransactionHistoryScreen(this);
    }

    @Override
    public void setCreateTransactionButtonBehavior(Runnable behavior) {
        Button createButton = (Button) this.findViewById(R.id.transactionhistoryButtonNewtransaction);
        createButton.setOnClickListener(new RunnableClickListener(behavior));
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

    @Override
    public void setTransactionListSelectBehavior(ListSelectBehavior<Transaction> behavior) {
        ListView listview = (ListView) this.findViewById(R.id.transactionhistoryListTransaction);
        listview.setOnItemClickListener(new ItemClickListSelect<Transaction>(behavior));
    }
}
