package cs2340.woms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cs2340.woms.auth.AndroidLoginManager;
import cs2340.woms.auth.LoginManager;

public class LoginScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        Button loginButton = (Button) this.findViewById(R.id.button1);

        final EditText userField = (EditText) this.findViewById(R.id.editText1);
        final EditText passField = (EditText) this.findViewById(R.id.editText2);

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                LoginManager loginManager = AndroidLoginManager.instance;
                if (loginManager.handleLogin(userField.getText().toString(), passField.getText().toString())) {
                    Intent intent = new Intent(LoginScreen.this, SuccessScreen.class);
                    startActivity(intent);
                }
            };
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
