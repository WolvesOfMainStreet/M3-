package cs2340.woms.model;

import java.util.Map;

/**
 * A user of this application.
 */
public class User implements Displayable, SerializableData {

    /**The save key for this user's username field.*/
    public static final String SAVE_KEY_USERNAME = "user-username";
    /**The save key for this user's password field.*/
    public static final String SAVE_KEY_PASSWORD = "user-password";

    /**This user's username. Also the unique identifies for this object.*/
    private String username;
    /**This user's password.*/
    private String password;

    /**
     * For serialization, not for normal use.
     */
    public User() { }

    /**
     * Creates a new user object with the given username and password.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns this user's username.
     *
     * @return this user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns this user's password.
     *
     * @return this user's password.
     */
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
        if (o == this) {
            return true;
        } else if (o == null || !(o instanceof User)) {
            return false;
        }

        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
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

    @Override
    public Map<String, String> write(Map<String, String> writeData) {
        writeData.put(SAVE_KEY_USERNAME, username);
        writeData.put(SAVE_KEY_PASSWORD, password);
        return writeData;
    }

    @Override
    public void read(Map<String, String> readData) {
        // Read username. Default to 'unknown'.
        String username = readData.get(SAVE_KEY_USERNAME);
        if (username == null) {
            System.err.println("Error reading username, defaulting to 'unknown'.");
            username = "unknown";
        }
        this.username = username;

        // Read password. Default to 'pass123'.
        String password = readData.get(SAVE_KEY_PASSWORD);
        if (password == null) {
            System.err.println("Error reading password for " + username + ", defaulting to 'pass123'.");
            password = "pass123";
        }
        this.password = password;
    }
}
