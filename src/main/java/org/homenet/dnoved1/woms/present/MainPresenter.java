package org.homenet.dnoved1.woms.present;

import org.homenet.dnoved1.woms.view.screens.LoginScreen;
import org.homenet.dnoved1.woms.view.screens.MainScreen;

/**
 * The presenter for the main screen.
 */
public class MainPresenter {

    /**The screen that this is presenting.*/
    protected MainScreen screen;

    /**
     * Creates a new presenter for the given main screen.
     *
     * @param screen the screen that this should present.
     */
    public MainPresenter(MainScreen screen) {
        this.screen = screen;
    }

    /**
     * Should be called whenever the login button is pressed. Will simply
     * open the login screen.
     */
    public void onLoginButtonPressed() {
        screen.open(DependencyManager.getImplementation(LoginScreen.class));
    }
}
