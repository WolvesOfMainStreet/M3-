package org.homenet.dnoved1.woms.android.view.screens;

import org.homenet.dnoved1.woms.R;
import org.homenet.dnoved1.woms.present.UserOverviewPresenter;
import org.homenet.dnoved1.woms.view.screens.UserOverviewScreen;

import android.os.Bundle;
import android.view.View;

/**
 * The android implementation of {@link cs2340.woms.view.screens.UserOverviewScreen}.
 */
public class AndroidUserOverviewScreen extends AndroidBaseScreen implements UserOverviewScreen {

    /**The presenter for this screen.*/
    private UserOverviewPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_overview_screen);
        this.presenter = new UserOverviewPresenter(this);

        // TODO: initialize accounts button with overview of accounts.
    }

    /**
     * Not for normal use. Is called by android whenever the accounts button is
     * pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onAccountsButtonPressed(View view) {
        presenter.onAccountsButtonPressed();
    }

    /**
     * Not for normal use. Is called by android whenever the spending report
     * button is pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onSpendingReportButtonPressed(View view) {
        presenter.onSpendingReportButtonPressed();
    }

    /**
     * Not for normal use. Is called by android whenever the income report
     * button is pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onIncomeReportButtonPressed(View view) {
        presenter.onIncomeReportButtonPressed();
    }
}
