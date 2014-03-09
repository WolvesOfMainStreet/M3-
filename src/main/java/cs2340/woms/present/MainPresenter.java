package cs2340.woms.present;

import cs2340.woms.view.screens.LoginScreen;
import cs2340.woms.view.screens.MainScreen;

public class MainPresenter {

    protected MainScreen screen;

    public MainPresenter(MainScreen screen) {
        this.screen = screen;
    }

    public void onLoginButtonPressed() {
        screen.open(DependencyManager.getImplementation(LoginScreen.class));
    }
}
