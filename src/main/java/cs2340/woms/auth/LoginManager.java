package cs2340.woms.auth;

public interface LoginManager {

    /**
     * Returns true if the given login details are valid, false if they are
     * not.
     *
     * @param username The username to test for validity.
     * @param password the password to test for validity.
     * @return true if the login details are valid, false if they are not.
     */
    boolean handleLogin(String username, String password);
}
