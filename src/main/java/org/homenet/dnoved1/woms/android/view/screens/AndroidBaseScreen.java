package org.homenet.dnoved1.woms.android.view.screens;

import java.util.Map;
import java.util.Map.Entry;

import org.homenet.dnoved1.woms.view.screens.BaseScreen;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

/**
 * The android implementation of {@link cs2340.woms.view.screens.BaseScreen}.
 */
public abstract class AndroidBaseScreen extends Activity implements BaseScreen {

    @Override
    public void open(Class<? extends BaseScreen> screen) {
        this.startActivity(new Intent(this, screen));
    }

    @Override
    public void open(Class<? extends BaseScreen> screen, Map<String, String> args) {
        Intent openScreen = new Intent(this, screen);
        for (Entry<String, String> arg: args.entrySet()) {
            openScreen.putExtra(arg.getKey(), arg.getValue());
        }

        this.startActivity(openScreen);
    }

    @Override
    public void close() {
        this.finish();
    }

    @Override
    public void popup(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
