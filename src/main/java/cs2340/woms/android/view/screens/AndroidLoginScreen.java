package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cs2340.woms.R;
import cs2340.woms.present.LoginPresenter;
import cs2340.woms.view.screens.LoginScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.LoginScreen}.
 */
public class AndroidLoginScreen extends AndroidBaseScreen implements LoginScreen {

    /**The presenter for this screen.*/
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        this.presenter = new LoginPresenter(this);
    }

    /**
     * Not for normal use. Is called by android whenever the login button is
     * pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onLoginButtonPressed(View view) {
        presenter.onLoginButtonPressed();
    }

    /**
     * Not for normal use. Is called by android whenever the register new user
     * button is pressed, as defined in this screen's layout file.
     *
     * @param view the button that was pressed.
     */
    public void onRegisterButtonPressed(View view) {
        presenter.onRegisterButtonPressed();
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
