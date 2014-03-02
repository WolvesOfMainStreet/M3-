package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import cs2340.woms.R;
import cs2340.woms.android.view.RunnableClickListener;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.screens.RegistrationScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.RegistrationScreen}.
 */
public class AndroidRegistrationScreen extends AndroidBaseScreen implements RegistrationScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_screen);
        Presenter.initRegistrationScreen(this);
    }

    @Override
    public void setConfirmButtonBehavior(Runnable behavior) {
        Button confirmButton = (Button) this.findViewById(R.id.registerButtonRegister);
        confirmButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    @Override
    public void setCancelButtonBehavior(Runnable behavior) {
        // TODO: create cancel button
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
