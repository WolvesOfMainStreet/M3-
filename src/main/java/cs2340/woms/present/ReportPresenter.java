package cs2340.woms.present;

import cs2340.woms.model.ClientDatabase;
import cs2340.woms.model.report.IncomeSourceReport;
import cs2340.woms.model.report.Report;
import cs2340.woms.model.report.SpendingCategoryReport;
import cs2340.woms.view.screens.ReportScreen;

public class ReportPresenter {

    protected ReportScreen screen;
    protected String reportType;

    public ReportPresenter(ReportScreen screen, String reportType) {
        this.screen = screen;
        this.reportType = reportType;
    }

    public void onPeriodChanged() {
        ClientDatabase db = ClientDatabase.get();
        Report report = null;

        if (SpendingCategoryReport.class.getName().equals(reportType)) {
            report = new SpendingCategoryReport(db.getCurrentUser(), screen.getStartPeriod(), screen.getEndPeriod());
        } else if (IncomeSourceReport.class.getName().equals(reportType)) {
            report = new IncomeSourceReport(db.getCurrentUser(), screen.getStartPeriod(), screen.getEndPeriod());
        } else {
            System.err.println("Unsupported report " + reportType + ", closing screen.");
            screen.close();
        }

        db.accept(report);
        screen.setReport(report);
    }
}
