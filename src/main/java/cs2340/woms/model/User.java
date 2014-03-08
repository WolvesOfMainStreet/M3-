package cs2340.woms.model;

/**
 * An account for logging into the financial application. Not to be confused
 * with an Account, which a LoginAccount can any number of.
 */
public class User implements Displayable {

    // Username and password are read-only (May be changed in the future to
    // allow password changes/resets)
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * A convenience method for checking if a username and password matches
     * this account.
     *
     * @param username the username to check.
     * @param password the password to check.
     * @return true if the given username and password match this account,
     * false if they do not.
     */
    public boolean matches(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public String toString() {
        return oneLineString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof User)) return false;

        User user = (User)o;
        return username.equals(user.username) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        int hashCode = username.hashCode();
        hashCode = hashCode * 31 + password.hashCode();
        return hashCode;
    }

    @Override
    public String oneLineString() {
        return username;
    }

    @Override
    public String[] multiLineString() {
        return new String[] {
                "User:",
                "\tUsername: " + username,
                "\tPassword: " + password
        };
    }
}
