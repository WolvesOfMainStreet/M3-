package cs2340.woms.present;

import cs2340.woms.model.ClientDatabase;
import cs2340.woms.model.report.IncomeSourceReport;
import cs2340.woms.model.report.Report;
import cs2340.woms.model.report.SpendingCategoryReport;
import cs2340.woms.view.screens.ReportScreen;

/**
 * The presenter for the report screen.
 */
public class ReportPresenter {

    /**The screen that this is presenting.*/
    protected ReportScreen screen;
    /**The type of report being displayed. See {@link ReportScreen#REPORT_TYPE}.*/
    protected String reportType;

    /**
     * Creates a new presenter for the given report screen displaying the given
     * type of report.
     *
     * @param screen the screen that this should present.
     * @param reportType the type of report being displayed. See
     * {@link ReportScreen#REPORT_TYPE}.
     */
    public ReportPresenter(ReportScreen screen, String reportType) {
        this.screen = screen;
        this.reportType = reportType;
    }

    /**
     * Should be called whenever the user-defined period over which the report
     * should be displayed is changed. Will re-create the report with the new
     * period and set it for display by the report screen.
     */
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
