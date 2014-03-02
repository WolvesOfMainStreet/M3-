package cs2340.woms.view.android.screens;

import android.os.Bundle;
import android.widget.Button;
import cs2340.woms.R;
import cs2340.woms.model.BaseModel;
import cs2340.woms.model.android.AndroidBaseModel;
import cs2340.woms.present.DependencyManager;
import cs2340.woms.present.Presenter;
import cs2340.woms.view.android.RunnableClickListener;
import cs2340.woms.view.screens.AccountCreationScreen;
import cs2340.woms.view.screens.AccountManagementScreen;
import cs2340.woms.view.screens.LoginScreen;
import cs2340.woms.view.screens.MainScreen;
import cs2340.woms.view.screens.RegistrationScreen;
import cs2340.woms.view.screens.TransactionCreationScreen;
import cs2340.woms.view.screens.TransactionHistoryScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.MainScreen}.
 */
public class AndroidMainScreen extends AndroidBaseScreen implements MainScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_matt);

        initializeAndroidEnvironment();
        Presenter.initMainScreen(this);
    }

    @Override
    public void setLoginButtonBehavior(Runnable behavior) {
        Button loginButton = (Button) this.findViewById(R.id.mainButtonLogin);
        loginButton.setOnClickListener(new RunnableClickListener(behavior));
    }

    /**
     * Initializes the environment as android by binding android specific
     * implementations.
     */
    private static void initializeAndroidEnvironment() {
        //-----Screens----------------------------------------------------------
        DependencyManager.bind(AccountCreationScreen.class, AndroidAccountCreationScreen.class);
        DependencyManager.bind(AccountManagementScreen.class, AndroidAccountManagementScreen.class);
        DependencyManager.bind(LoginScreen.class, AndroidLoginScreen.class);
        DependencyManager.bind(MainScreen.class, AndroidMainScreen.class);
        DependencyManager.bind(RegistrationScreen.class, AndroidRegistrationScreen.class);
        DependencyManager.bind(TransactionCreationScreen.class, AndroidTransactionCreationScreen.class);
        DependencyManager.bind(TransactionHistoryScreen.class, AndroidTransactionHistoryScreen.class);

        //-----Models-----------------------------------------------------------
        DependencyManager.bind(BaseModel.class, AndroidBaseModel.class);
    }
}
