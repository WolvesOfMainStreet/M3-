package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.widget.Button;
import cs2340.woms.R;
import cs2340.woms.android.view.RunnableClickListener;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.screens.UserOverviewScreen;

public class AndroidUserOverviewScreen extends AndroidBaseScreen implements UserOverviewScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_overview_screen);
        Presenter.initUserOverviewScreen(this);

        // TODO: initialize accounts button with overview of accounts.
    }

    @Override
    public void setAccountsButtonBehavior(Runnable behavior) {
        Button accountsButton = (Button) this.findViewById(R.id.useroverviewButtonAccounts);
        accountsButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public void setSpendingReportButtonBehavior(Runnable behavior) {
        Button spendingReportButton = (Button) this.findViewById(R.id.useroverviewButtonSpendingreport);
        spendingReportButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public void setIncomeReportButtonBehavior(Runnable behavior) {
        Button incomeReportButton = (Button) this.findViewById(R.id.useroverviewButtonIncomereport);
        incomeReportButton.setOnClickListener(new RunnableClickListener(behavior));
    }
}
