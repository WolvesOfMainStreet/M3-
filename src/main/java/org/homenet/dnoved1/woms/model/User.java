package org.homenet.dnoved1.woms.model;

import java.io.Serializable;

/**
 * A user of this application.
 */
public class User implements Displayable, Serializable {

    /**Serial version.*/
    private static final long serialVersionUID = 1L;

    /**This user's username. Also the unique identifies for this object.*/
    private String username;
    /**This user's password.*/
    private String password;

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
}
