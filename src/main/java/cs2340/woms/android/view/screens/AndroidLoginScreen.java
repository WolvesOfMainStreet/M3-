package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import cs2340.woms.R;
import cs2340.woms.android.view.RunnableClickListener;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.screens.LoginScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.LoginScreen}.
 */
public class AndroidLoginScreen extends AndroidBaseScreen implements LoginScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        Presenter.initLoginScreen(this);
    }

    @Override
    public void setLoginButtonBehavior(Runnable behavior) {
        Button loginButton = (Button) this.findViewById(R.id.loginButtonLogin);
        loginButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public void setRegisterButtonBehavior(Runnable behavior) {
        Button loginButton = (Button) this.findViewById(R.id.loginButtonRegister);
        loginButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public String getUsernameField() {
        EditText userField = (EditText) this.findViewById(R.id.loginFieldUsername);
        return userField.getText().toString();
    }

    @Override
    public String getPasswordField() {
        EditText passField = (EditText) this.findViewById(R.id.loginFieldPassword);
        return passField.getText().toString();
    }
}
