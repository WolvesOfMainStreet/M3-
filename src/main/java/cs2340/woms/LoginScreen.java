package cs2340.woms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

        Button loginButton = (Button) this.findViewById(R.id.loginButtonLogin);
        Button registerButton = (Button) this.findViewById(R.id.loginButtonRegister);

        final EditText userField = (EditText) this.findViewById(R.id.loginFieldUsername);
        final EditText passField = (EditText) this.findViewById(R.id.loginFieldPassword);

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                LoginManager loginManager = AndroidLoginManager.instance;
                if (loginManager.handleLogin(userField.getText().toString(), passField.getText().toString())) {
                    Intent intent = new Intent(LoginScreen.this, AccountManagementScreen.class);
                    startActivity(intent);
                }
            };
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, RegistrationScreen.class);
                startActivity(intent);
            }
        });
    }
}
