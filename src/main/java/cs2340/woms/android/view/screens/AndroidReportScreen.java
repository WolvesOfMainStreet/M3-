package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.widget.TextView;
import cs2340.woms.R;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.screens.ReportScreen;

public class AndroidReportScreen extends AndroidBaseScreen implements ReportScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments
        Bundle extras = this.getIntent().getExtras();
        String[] report = extras.getStringArray(ReportScreen.REPORT);
        if (report == null) {
            System.out.println("No report given, closing screen.");
            this.close();
        }

        setContentView(R.layout.report_screen);
        Presenter.initReportScreen(this, report);
    }

    @Override
    public void setReportText(String[] lines) {
        TextView x = (TextView) this.findViewById(R.id.reportTextMain);
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
        x.setText(text.toString());
    }
}
