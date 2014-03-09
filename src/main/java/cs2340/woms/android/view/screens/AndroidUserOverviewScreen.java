package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.view.View;
import cs2340.woms.R;
import cs2340.woms.present.UserOverviewPresenter;
import cs2340.woms.view.screens.UserOverviewScreen;

public class AndroidUserOverviewScreen extends AndroidBaseScreen implements UserOverviewScreen {

    private UserOverviewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_overview_screen);
        this.presenter = new UserOverviewPresenter(this);

        // TODO: initialize accounts button with overview of accounts.
    }

    public void onAccountsButtonPressed(View view) {
        presenter.onAccountsButtonPressed();
    }

    public void onSpendingReportButtonPressed(View view) {
        presenter.onSpendingReportButtonPressed();
    }

    public void onIncomeReportButtonPressed(View view) {
        presenter.onIncomeReportButtonPressed();
    }
}
