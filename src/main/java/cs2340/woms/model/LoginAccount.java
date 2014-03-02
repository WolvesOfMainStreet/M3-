package cs2340.woms.model;

import java.util.ArrayList;

/**
 * An account for logging into the financial application. Not to be confused
 * with an Account, which a LoginAccount can any number of.
 */
public class LoginAccount {

    // Username and password are read-only (May be changed in the future to
    // allow password changes/resets)
    private final String username;
    private final String password;

    private final ObservableList<FinanceAccount> accounts;

    public LoginAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.accounts = new ObservableList<FinanceAccount>(new ArrayList<FinanceAccount>());
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

    public ObservableList<FinanceAccount> getAccounts() {
        return accounts;
    }

    public void addAccount(FinanceAccount account) {
        accounts.add(account);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Username: ");
        string.append(username);
        string.append("\n");

        string.append("Password: ");
        string.append(password);
        return string.toString();
    }
}
