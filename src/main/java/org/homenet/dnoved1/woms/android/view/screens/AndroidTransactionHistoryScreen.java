package org.homenet.dnoved1.woms.android.view.screens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.homenet.dnoved1.woms.R;
import org.homenet.dnoved1.woms.model.Account;
import org.homenet.dnoved1.woms.model.ClientDatabase;
import org.homenet.dnoved1.woms.model.DataSetObserver;
import org.homenet.dnoved1.woms.model.Transaction;
import org.homenet.dnoved1.woms.present.TransactionHistoryPresenter;
import org.homenet.dnoved1.woms.view.screens.TransactionHistoryScreen;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * The android implementation of {@link cs2340.woms.view.screens.TransactionHistoryScreen}.
 */
public class AndroidTransactionHistoryScreen extends AndroidBaseScreen implements TransactionHistoryScreen {

    /**The presenter for this screen.*/
    private TransactionHistoryPresenter presenter;

    /**The list of transactions to display.*/
    private List<Transaction> transactionList;
    /**The list adapter for the list of transactions being displayed.*/
    private BaseAdapter transactionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history_screen);

        Account account = ClientDatabase.get().getCurrentAccount();

        transactionList = new ArrayList<Transaction>();
        transactionListAdapter = new ArrayAdapter<Transaction>(this, R.layout.account_listing, transactionList);
        ListView listview = getTransactionList();
        listview.setAdapter(transactionListAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onTransactionSelected((Transaction) parent.getItemAtPosition(position));
            }
        });

        this.presenter = new TransactionHistoryPresenter(this, account);
    }

    /**
     * Not for normal use. Is called by android whenever the create new deposit
     * button is pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onCreateDepositButtonPressed(View view) {
        presenter.onCreateDepositButtonPressed();
    }

    /**
     * Not for normal use. Is called by android whenever the create new
     * withdrawal button is pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onCreateWithdrawalButtonPressed(View view) {
        presenter.onCreateWithdrawalButtonPressed();
    }

    @Override
    public DataSetObserver<Transaction> getTransactionListObserver() {
        return new DataSetObserver<Transaction>() {
            @Override
            public void update(final Collection<Transaction> dataset) {
                getTransactionList().post(new Runnable() {
                    @Override
                    public void run() {
                        //TODO: this can likely be done more efficiently
                        transactionList.clear();
                        transactionList.addAll(dataset);
                        transactionListAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
    }

    /**
     * Returns the list view displaying this screen's transactions.
     *
     * @return the transaction list view.
     */
    private ListView getTransactionList() {
        return (ListView) this.findViewById(R.id.transactionhistoryListTransaction);
    }
}
