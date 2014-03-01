package cs2340.woms.view.android.screens;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import cs2340.woms.view.screens.BaseScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.BaseScreen}.
 */
public abstract class AndroidBaseScreen extends Activity implements BaseScreen {

    @Override
    public void open(Class<?> screen) {
        this.startActivity(new Intent(this, screen));
    }

    @Override
    public void popup(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
