package cs2340.woms;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class LoginScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        
        /*View view = this.findViewById(R.id.action_settings);
        
        View root = view.getRootView();
        
        root.setBackgroundColor(0x00000000);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
