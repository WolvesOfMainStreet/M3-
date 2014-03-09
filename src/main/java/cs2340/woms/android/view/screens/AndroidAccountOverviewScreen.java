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
import cs2340.woms.model.DataSetObserver;
import cs2340.woms.present.AccountOverviewPresenter;
import cs2340.woms.view.screens.AccountOverviewScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.AccountOverviewScreen}.
 */
public class AndroidAccountOverviewScreen extends AndroidBaseScreen implements AccountOverviewScreen {

    private AccountOverviewPresenter presenter;

    private List<Account> accountList;
    private BaseAdapter accountListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_overview_screen);

        accountList = new ArrayList<Account>();
        accountListAdapter = new ArrayAdapter<Account>(this, R.layout.account_listing, accountList);
        ListView listview = (ListView) this.findViewById(R.id.accountmanageListAccount);
        listview.setAdapter(accountListAdapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onAccountSelected((Account) parent.getItemAtPosition(position));
            }
        });

        this.presenter = new AccountOverviewPresenter(this);
    }

    public void onCreateAccountButtonPressed(View view) {
        presenter.onCreateAccountButtonPressed();
    }

    public void onAccountSelected(Account account) {
        presenter.onAccountSelected(account);
    }

    @Override
    public DataSetObserver<Account> getAccountListObserver() {
        return new DataSetObserver<Account>() {
            @Override
            public void update(Collection<Account> dataset) {
                //TODO: this can likely be done more efficiently
                accountList.clear();
                accountList.addAll(dataset);
                accountListAdapter.notifyDataSetChanged();
            }
        };
    }
}
