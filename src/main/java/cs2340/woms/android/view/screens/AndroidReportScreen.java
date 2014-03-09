package cs2340.woms.android.view.screens;

import java.util.Calendar;
import java.util.Date;

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
import cs2340.woms.R;
import cs2340.woms.model.report.Report;
import cs2340.woms.present.ReportPresenter;
import cs2340.woms.view.screens.ReportScreen;

public class AndroidReportScreen extends AndroidBaseScreen implements ReportScreen {

    private static final String DATE_FORMAT = "%1$tB %1$te, %1$tY";

    private ReportPresenter presenter;

    private Calendar startPeriod;
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

        onPeriodChanged();
    }

    public void onPeriodChanged() {
        presenter.onPeriodChanged();
    }

    public void chooseStartPeriod(View view) {
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
                onPeriodChanged();
            }
        });

        dateDialog.show();
    }

    public void chooseEndPeriod(View view) {
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
                onPeriodChanged();
            }
        });

        dateDialog.show();
    }

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
