package org.homenet.dnoved1.woms.present;

import java.util.HashMap;
import java.util.Map;

import org.homenet.dnoved1.woms.model.report.IncomeSourceReport;
import org.homenet.dnoved1.woms.model.report.SpendingCategoryReport;
import org.homenet.dnoved1.woms.view.screens.AccountOverviewScreen;
import org.homenet.dnoved1.woms.view.screens.ReportScreen;
import org.homenet.dnoved1.woms.view.screens.UserOverviewScreen;

/**
 * The presenter for the user overview screen.
 */
public class UserOverviewPresenter {

    /**The screen that this is presenting.*/
    protected UserOverviewScreen screen;

    /**
     * Creates a new presenter for the given user overview screen.
     *
     * @param screen the screen that this should present.
     */
    public UserOverviewPresenter(UserOverviewScreen screen) {
        this.screen = screen;
    }

    /**
     * Should be called whenever the accounts button is pressed. Will simply
     * open the account overview screen.
     */
    public void onAccountsButtonPressed() {
        screen.open(DependencyManager.getImplementation(AccountOverviewScreen.class));
    }

    /**
     * Should be called whenever the spending report button is pressed. Will
     * simply open a new report screen for the current user's spending report.
     */
    public void onSpendingReportButtonPressed() {
        Map<String, String> args = new HashMap<String, String>();
        args.put(ReportScreen.REPORT_TYPE, SpendingCategoryReport.class.getName());
        screen.open(DependencyManager.getImplementation(ReportScreen.class), args);
    }

    /**
     * Should be called whenever the income report button is pressed. Will
     * simply open a new report screen for the current user's income report.
     */
    public void onIncomeReportButtonPressed() {
        Map<String, String> args = new HashMap<String, String>();
        args.put(ReportScreen.REPORT_TYPE, IncomeSourceReport.class.getName());
        screen.open(DependencyManager.getImplementation(ReportScreen.class), args);
    }
}
