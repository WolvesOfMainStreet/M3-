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

    /**
     * Registers a new username for logging in. Returns true if the registration
     * was successful, and false if not. Registration will usually succeed, but
     * can fail if, for example, the given username and password were already
     * registered.
     *
     * @param username the username to register.
     * @param password the password to register.
     * @return true if registration was successful, false if not.
     */
    boolean register(String username, String password);
}
