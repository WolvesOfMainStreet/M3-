package cs2340.woms.present;

import java.util.HashMap;
import java.util.Map;

import cs2340.woms.model.report.IncomeSourceReport;
import cs2340.woms.model.report.SpendingCategoryReport;
import cs2340.woms.view.screens.AccountOverviewScreen;
import cs2340.woms.view.screens.ReportScreen;
import cs2340.woms.view.screens.UserOverviewScreen;

public class UserOverviewPresenter {

    protected UserOverviewScreen screen;

    public UserOverviewPresenter(UserOverviewScreen screen) {
        this.screen = screen;
    }

    public void onAccountsButtonPressed() {
        screen.open(DependencyManager.getImplementation(AccountOverviewScreen.class));
    }

    public void onSpendingReportButtonPressed() {
        Map<String, String> args = new HashMap<String, String>();
        args.put(ReportScreen.REPORT_TYPE, SpendingCategoryReport.class.getName());
        screen.open(DependencyManager.getImplementation(ReportScreen.class), args);
    }

    public void onIncomeReportButtonPressed() {
        Map<String, String> args = new HashMap<String, String>();
        args.put(ReportScreen.REPORT_TYPE, IncomeSourceReport.class.getName());
        screen.open(DependencyManager.getImplementation(ReportScreen.class), args);
    }
}
