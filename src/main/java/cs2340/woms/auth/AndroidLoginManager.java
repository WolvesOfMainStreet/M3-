package cs2340.woms.auth;

import java.util.HashSet;
import java.util.Set;

import cs2340.woms.account.LoginAccount;

public class AndroidLoginManager implements LoginManager {

    public static LoginManager instance = new AndroidLoginManager();
    private Set<LoginAccount> logins;

    // The currently logged in account
    private LoginAccount currentLogin = null;

    private AndroidLoginManager() {
        logins = new HashSet<LoginAccount>();

        // Admin Account
        register("admin", "pass123");
    }

    @Override
    public boolean handleLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        for(LoginAccount login: logins) {
        	if (login.matches(username, password)) {
        	    currentLogin = login;
        	    return true;
        	}
        }

        return false;
    }

    @Override
    public boolean handleLogout() {
        // Can't log out if no one was logged in in the first place.
        if (currentLogin == null) {
            return false;
        } else {
            currentLogin = null;
            return true;
        }
    }

    @Override
    public boolean register(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        // Don't re-register an already registered name username.
        for (LoginAccount login: logins) {
            if (login.getUsername().equals(username)) {
                return false;
            }
        }

    	LoginAccount newLogin = new LoginAccount(username, password);
    	logins.add(newLogin);
    	return true;
    }

    @Override
    public LoginAccount getCurrentLogin() {
        return currentLogin;
    }
}