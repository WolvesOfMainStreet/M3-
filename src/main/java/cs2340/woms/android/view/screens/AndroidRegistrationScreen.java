package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cs2340.woms.R;
import cs2340.woms.present.RegistrationPresenter;
import cs2340.woms.view.screens.RegistrationScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.RegistrationScreen}.
 */
public class AndroidRegistrationScreen extends AndroidBaseScreen implements RegistrationScreen {

    private RegistrationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        this.presenter = new RegistrationPresenter(this);
    }

    public void onConfirmButtonPressed(View view) {
        presenter.onConfirmButtonPressed();
    }

    public void onCancelButtonPressed(View view) {
        presenter.onCancelButtonPressed();
    }

    @Override
    public String getUsernameField() {
        EditText usernameField = (EditText) this.findViewById(R.id.registerFieldUsername);
        return usernameField.getText().toString();
    }

    @Override
    public String getPasswordField() {
        EditText passwordField = (EditText) this.findViewById(R.id.registerFieldPassword1);
        return passwordField.getText().toString();
    }

    @Override
    public String getConfirmPasswordField() {
        EditText confirmPasswordField = (EditText) this.findViewById(R.id.registerFieldPassword2);
        return confirmPasswordField.getText().toString();
    }
}
