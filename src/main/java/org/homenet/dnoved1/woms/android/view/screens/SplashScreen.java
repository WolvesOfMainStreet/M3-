package org.homenet.dnoved1.woms.android.view.screens;

import org.homenet.dnoved1.woms.R;
import org.homenet.dnoved1.woms.present.LoginPresenter;
import org.homenet.dnoved1.woms.view.screens.LoginScreen;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * The android implementation of {@link cs2340.woms.view.screens.SplashScreen}.
 */
public class SplashScreen extends AndroidBaseScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        this.presenter = new LoginPresenter(this);
        
        Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		}, 120000);
		
	

    }

}
