package cs2340.woms.view.screens;

public interface UserOverviewScreen extends BaseScreen {

    void setAccountsButtonBehavior(Runnable behavior);

    void setSpendingReportButtonBehavior(Runnable behavior);

    void setIncomeReportButtonBehavior(Runnable behavior);
}
