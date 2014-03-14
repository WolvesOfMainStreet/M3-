package cs2340.woms.view.screens;

import java.util.Date;

import cs2340.woms.model.report.Report;

/**
 * A report screen. The report screen has two fields for entering a starting
 * and ending period for the report as well as a display for said report.
 */
public interface ReportScreen extends BaseScreen {

    /**
     * A parameter name for the type of report that is being displayed by this
     * screen. The associated argument should be the classname of the report
     * being displayed.
     */
    String REPORT_TYPE = "report";

    /**
     * Returns the user-selected starting period for the report.
     *
     * @return the starting period.
     */
    Date getStartPeriod();

    /**
     * Returns the user-selected ending period for the report.
     *
     * @return the ending period.
     */
    Date getEndPeriod();

    /**
     * Used to set the report that is being displayed by this screen.
     *
     * @param report the report to display.
     */
    void setReport(Report report);
}
