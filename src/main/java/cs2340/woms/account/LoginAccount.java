package cs2340.woms.account;

/**
 * An account for logging into the financial application. Not to be confused
 * with an Account, which a LoginAccount can any number of.
 */
public class LoginAccount {

    // Username and password are read-only (May be changed in the future to
    // allow password changes/resets)
    private final String username;
    private final String password;

    public LoginAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
