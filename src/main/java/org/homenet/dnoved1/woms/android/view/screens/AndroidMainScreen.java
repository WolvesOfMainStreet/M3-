package org.homenet.dnoved1.woms.android.view.screens;

import org.homenet.dnoved1.woms.R;
import org.homenet.dnoved1.woms.android.model.AndroidLocalSQLConnection;
import org.homenet.dnoved1.woms.model.ClientDatabase;
import org.homenet.dnoved1.woms.present.DependencyManager;
import org.homenet.dnoved1.woms.present.MainPresenter;
import org.homenet.dnoved1.woms.view.screens.AccountCreationScreen;
import org.homenet.dnoved1.woms.view.screens.AccountOverviewScreen;
import org.homenet.dnoved1.woms.view.screens.LoginScreen;
import org.homenet.dnoved1.woms.view.screens.MainScreen;
import org.homenet.dnoved1.woms.view.screens.RegistrationScreen;
import org.homenet.dnoved1.woms.view.screens.ReportScreen;
import org.homenet.dnoved1.woms.view.screens.TransactionCreationScreen;
import org.homenet.dnoved1.woms.view.screens.TransactionHistoryScreen;
import org.homenet.dnoved1.woms.view.screens.UserOverviewScreen;

import android.os.Bundle;
import android.view.View;
import android.media.MediaPlayer;

/**
 * The android implementation of {@link cs2340.woms.view.screens.MainScreen}.
 */
public class AndroidMainScreen extends AndroidBaseScreen implements MainScreen {

    /**The presenter for this screen.*/
    private MainPresenter presenter;
    public MediaPlayer mp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        initializeAndroidEnvironment();
        this.presenter = new MainPresenter(this);
        mp1 = MediaPlayer.create(AndroidMainScreen.this, R.raw.mymusic);
        mp1.start();
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
     * Initializes the environment as android by binding android specific
     * implementations.
     */
    private void initializeAndroidEnvironment() {
        //-----Screens----------------------------------------------------------
        DependencyManager.bind(AccountCreationScreen.class, AndroidAccountCreationScreen.class);
        DependencyManager.bind(AccountOverviewScreen.class, AndroidAccountOverviewScreen.class);
        DependencyManager.bind(LoginScreen.class, AndroidLoginScreen.class);
        DependencyManager.bind(MainScreen.class, AndroidMainScreen.class);
        DependencyManager.bind(RegistrationScreen.class, AndroidRegistrationScreen.class);
        DependencyManager.bind(ReportScreen.class, AndroidReportScreen.class);
        DependencyManager.bind(UserOverviewScreen.class, AndroidUserOverviewScreen.class);
        DependencyManager.bind(TransactionCreationScreen.class, AndroidTransactionCreationScreen.class);
        DependencyManager.bind(TransactionHistoryScreen.class, AndroidTransactionHistoryScreen.class);

        //-----Models-----------------------------------------------------------
        ClientDatabase.create(new AndroidLocalSQLConnection(this));
    }
}
