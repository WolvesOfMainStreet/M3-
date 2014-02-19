package cs2340.woms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
            	if(passField.getText().toString().equals(passField2.getText().toString())) {
            		AndroidLoginManager.instance.register(userField.getText().toString(), passField.getText().toString());
            		RegistrationScreen.this.finish();
            	}
            };
        });
	}
}
