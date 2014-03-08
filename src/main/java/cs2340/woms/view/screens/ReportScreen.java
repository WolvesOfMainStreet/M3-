package cs2340.woms.view.screens;

/**
 * A report screen. A report screen consists of a multi-line text view in which
 * a report can be displayed.
 */
public interface ReportScreen extends BaseScreen {

    String REPORT = "report";

    /**
     * Set the lines to display in the multi-line text view representing the
     * report.
     *
     * @param lines the lines to display for the report.
     */
    void setReportText(String[] lines);
}
