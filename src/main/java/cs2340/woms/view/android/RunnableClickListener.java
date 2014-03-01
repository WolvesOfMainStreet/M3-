package cs2340.woms.view.android;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * An OnClickListener which delegates its onClick method to a runnable.
 */
public class RunnableClickListener implements OnClickListener {

    private Runnable behavior;

    public RunnableClickListener(Runnable behavior) {
        this.behavior = behavior;
    }

    @Override
    public void onClick(View v) {
        behavior.run();
    }
}
