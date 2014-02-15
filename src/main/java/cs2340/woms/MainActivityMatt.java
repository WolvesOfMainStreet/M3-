package cs2340.woms;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.content.Intent;
import android.widget.Button;
import android.view.View.OnClickListener;


public class MainActivityMatt extends Activity {

    Button loginButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_matt);
        
        loginButton = (Button) this.findViewById(R.id.button1);
        
        loginButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivityMatt.this, LoginScreen.class);
                startActivity(intent);
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