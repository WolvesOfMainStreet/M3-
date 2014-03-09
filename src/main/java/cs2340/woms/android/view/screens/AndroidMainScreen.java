package cs2340.woms.android.view.screens;

import android.os.Bundle;
import android.view.View;
import cs2340.woms.R;
import cs2340.woms.android.model.AndroidLocalDatabase;
import cs2340.woms.android.model.AndroidLocalStorageModel;
import cs2340.woms.model.BaseModel;
import cs2340.woms.model.LocalStorageModel;
import cs2340.woms.present.DependencyManager;
import cs2340.woms.present.MainPresenter;
import cs2340.woms.view.screens.AccountCreationScreen;
import cs2340.woms.view.screens.AccountOverviewScreen;
import cs2340.woms.view.screens.LoginScreen;
import cs2340.woms.view.screens.MainScreen;
import cs2340.woms.view.screens.RegistrationScreen;
import cs2340.woms.view.screens.ReportScreen;
import cs2340.woms.view.screens.TransactionCreationScreen;
import cs2340.woms.view.screens.TransactionHistoryScreen;
import cs2340.woms.view.screens.UserOverviewScreen;

/**
 * The android implementation of {@link cs2340.woms.view.screens.MainScreen}.
 */
public class AndroidMainScreen extends AndroidBaseScreen implements MainScreen {

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        initializeAndroidEnvironment();
        this.presenter = new MainPresenter(this);
    }

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
        AndroidLocalDatabase.create(this);
        DependencyManager.bind(BaseModel.class, AndroidLocalStorageModel.class);
        DependencyManager.bind(LocalStorageModel.class, AndroidLocalStorageModel.class);
    }
}
