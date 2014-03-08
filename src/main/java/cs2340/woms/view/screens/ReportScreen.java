package cs2340.woms.view.screens;

import java.util.Date;

import cs2340.woms.model.report.Report;

public interface ReportScreen extends BaseScreen {

    String REPORT_TYPE = "report";

    void setOnPeriodChangeBehavior(Runnable behavior);

    Date getStartPeriod();
    Date getEndPeriod();

    void setReport(Report report);
}
