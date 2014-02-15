package cs2340.woms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_screen);

		Button registerButton = (Button) this.findViewById(R.id.button1);

        final EditText userField = (EditText) this.findViewById(R.id.editText1);
        final EditText passField = (EditText) this.findViewById(R.id.EditText01);
        final EditText passField2 = (EditText) this.findViewById(R.id.editText2);

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                // TODO: add user to login manager
            };
        });
	}
}
