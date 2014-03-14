package org.homenet.dnoved1.woms.android.view.screens;

import java.util.Calendar;
import java.util.Date;

import org.homenet.dnoved1.R;
import org.homenet.dnoved1.woms.model.report.Report;
import org.homenet.dnoved1.woms.present.ReportPresenter;
import org.homenet.dnoved1.woms.view.screens.ReportScreen;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * The android implementation of {@link cs2340.woms.view.screens.ReportScreen}.
 */
public class AndroidReportScreen extends AndroidBaseScreen implements ReportScreen {

    /**The format string for displaying the selected start/end period dates.*/
    private static final String DATE_FORMAT = "%1$tB %1$te, %1$tY";

    /**The presenter for this screen.*/
    private ReportPresenter presenter;

    /**The user-selected starting period for this screen's report.*/
    private Calendar startPeriod;
    /**The user-selected ending period for this screen's report.*/
    private Calendar endPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments
        Bundle extras = this.getIntent().getExtras();
        String reportClass = extras.getString(ReportScreen.REPORT_TYPE);
        if (reportClass == null) {
            System.err.println("No report specified, closing screen.");
            this.close();
        }

        setContentView(R.layout.report_screen);
        this.presenter = new ReportPresenter(this, reportClass);

        // Default period to one month ago to present
        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        this.startPeriod = start;
        this.endPeriod = Calendar.getInstance();

        Button startPeriodButton = (Button) this.findViewById(R.id.reportButtonStartperiod);
        Button endPeriodButton = (Button) this.findViewById(R.id.reportButtonEndperiod);

        startPeriodButton.setText(String.format(DATE_FORMAT, startPeriod));
        endPeriodButton.setText(String.format(DATE_FORMAT, endPeriod));
    }

    /**
     * Not for normal use. Is called by android whenever the button displaying
     * the starting period is pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void chooseStartPeriod(View view) {
        if (!(view instanceof Button)) {
            throw new IllegalArgumentException("View must be a button.");
        }

        final Button startPeriodButton = (Button) view;

        Dialog dateDialog = new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                startPeriod.set(Calendar.YEAR, year);
                startPeriod.set(Calendar.MONTH, month);
                startPeriod.set(Calendar.DAY_OF_MONTH, day);
                startPeriodButton.setText(String.format(DATE_FORMAT, startPeriod));
            }
        }, startPeriod.get(Calendar.YEAR), startPeriod.get(Calendar.MONTH), startPeriod.get(Calendar.DAY_OF_MONTH));

        dateDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                presenter.onPeriodChanged();
            }
        });

        dateDialog.show();
    }

    /**
     * Not for normal use. Is called by android whenever the button displaying
     * the ending period is pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void chooseEndPeriod(View view) {
        if (!(view instanceof Button)) {
            throw new IllegalArgumentException("View must be a button.");
        }

        final Button endPeriodButton = (Button) view;

        Dialog dateDialog = new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                endPeriod.set(Calendar.YEAR, year);
                endPeriod.set(Calendar.MONTH, month);
                endPeriod.set(Calendar.DAY_OF_MONTH, day);
                endPeriodButton.setText(String.format(DATE_FORMAT, endPeriod));
            }
        }, endPeriod.get(Calendar.YEAR), endPeriod.get(Calendar.MONTH), endPeriod.get(Calendar.DAY_OF_MONTH));

        dateDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                presenter.onPeriodChanged();
            }
        });

        dateDialog.show();
    }

    /**
     * Convenience method for setting this screen's display to the given array
     * of lines.
     *
     * @param lines the lines to display.
     */
    private void setReportText(String[] lines) {
        TextView reportText = (TextView) this.findViewById(R.id.reportTextMain);
        StringBuilder text = new StringBuilder();
        boolean first = true;
        for (String line: lines) {
            if (first) {
                first = false;
            } else {
                text.append('\n');
            }
            text.append(line);
        }
        reportText.setText(text.toString());
    }

    @Override
    public Date getStartPeriod() {
        return startPeriod.getTime();
    }

    @Override
    public Date getEndPeriod() {
        return endPeriod.getTime();
    }

    @Override
    public void setReport(Report report) {
        setReportText(report.multiLineString());
    }
}
