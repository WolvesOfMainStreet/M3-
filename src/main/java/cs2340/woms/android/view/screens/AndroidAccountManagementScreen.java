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
import cs2340.woms.model.FinanceAccount;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.ListSelectBehavior;
import cs2340.woms.view.screens.AccountManagementScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.AccountManagementScreen}.
 */
public class AndroidAccountManagementScreen extends AndroidBaseScreen implements AccountManagementScreen {

    private List<FinanceAccount> accountList;
    private BaseAdapter accountListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_management_screen);

        accountList = new ArrayList<FinanceAccount>();
        accountListAdapter = new ArrayAdapter<FinanceAccount>(this, R.layout.account_listing, accountList);
        ((ListView) this.findViewById(R.id.accountmanageListAccount)).setAdapter(accountListAdapter);

        Presenter.initAccountManagementScreen(this);
    }

    @Override
    public void setCreateAccountButtonBehavior(Runnable behavior) {
        Button createAccountButton = (Button) this.findViewById(R.id.accountmanageButtonNewaccount);
        createAccountButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public DataSetObserver<FinanceAccount> getAccountListObserver() {
        return new DataSetObserver<FinanceAccount>() {
            @Override
            public void update(Collection<FinanceAccount> dataset) {
                //TODO: this can likely be done more efficiently
                accountList.clear();
                accountList.addAll(dataset);
                accountListAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public void setAccountListSelectBehavior(ListSelectBehavior<FinanceAccount> behavior) {
        ListView listview = (ListView) this.findViewById(R.id.accountmanageListAccount);
        listview.setOnItemClickListener(new ItemClickListSelect<FinanceAccount>(behavior));
    }
}
