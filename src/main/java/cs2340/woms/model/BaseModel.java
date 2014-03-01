package cs2340.woms.model;

/**
 * The base model interface. All models should implement this.
 */
public interface BaseModel {

    /**
     * Returns whether authentication using the given credentials was
     * successful.
     *
     * @param username the username of the account to authenticate.
     * @param password the password of the account to authenticate.
     * @return true if authentication was successful, false if not.
     */
    boolean login(String username, String password);

    /**
     * Logs out the currently logged in user, if any.
     */
    void logout();

    /**
     * Attempts to register a new account, and returns whether this was
     * successful or not.
     *
     * @param username the username of the new account.
     * @param password the password of the new account.
     * @return true if the account was successfully registered, false if not.
     */
    boolean register(String username, String password);
}
