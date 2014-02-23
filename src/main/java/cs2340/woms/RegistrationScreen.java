package cs2340.woms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cs2340.woms.auth.AndroidLoginManager;

public class RegistrationScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_screen);

		Button registerButton = (Button) this.findViewById(R.id.registerButtonRegister);

        final EditText userField = (EditText) this.findViewById(R.id.registerFieldUsername);
        final EditText passField = (EditText) this.findViewById(R.id.registerFieldPassword1);
        final EditText passField2 = (EditText) this.findViewById(R.id.registerFieldPassword2);

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                String username = userField.getText().toString();
                String password1 = passField.getText().toString();
                String password2 = passField2.getText().toString();

                String error = null;

                if (username.equals("")) {
                    error = "Username cannot be empty.";
                } else if (password1.equals("") || password2.equals("")) {
                    error = "Password fields cannot be empty.";
                } else if (!password1.equals(password2)) {
                    error = "Password fields do no match.";
                } else if (!AndroidLoginManager.instance.register(username, password1)) {
                    error = "Could not create account with that username.";
                }

                if (error != null) {
                    Toast.makeText(RegistrationScreen.this, error, Toast.LENGTH_SHORT).show();
                } else {
                    RegistrationScreen.this.finish();
                }
            };
        });

        //TODO: Create a cancel button
	}
}
