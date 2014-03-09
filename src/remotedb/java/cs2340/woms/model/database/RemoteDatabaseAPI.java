package cs2340.woms.model.database;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import cs2340.woms.model.database.ct.Account;
import cs2340.woms.model.database.ct.AccountSummary;

/**
 * An interface representing the WOMS Remote Database API.
 */
@WebService
public interface RemoteDatabaseAPI {

    //-----Authentication-------------------------------------------------------

    /**
     * Returns an authorization token associated with the account the given
     * username and password represent. If the given username and password do
     * not match an existing account this instead returns null.
     *
     * @param username the username of the account to authorize.
     * @param password the password of the account to authorize.
     * @return an authorization token that can be used in other methods, or nil
     * if the provided credentials are invalid.
     */
    @WebMethod
    String login(
            @WebParam(name="username") String username,
            @WebParam(name="password") String password);

    /**
     * Invalidates the given authorization token, preventing further use.
     *
     * @param token the authorization token to be invalidated.
     */
    @WebMethod
    void logout(
            @WebParam(name="token") String token);

    //-----Account Management---------------------------------------------------

    /**
     * Returns a sequence of summaries of all accounts associated with the
     * user given by the authorization token.
     *
     * @param token the authorization token representing the user to get
     * account summaries for.
     * @return a sequence of account summaries for the given user.
     */
    @WebMethod
    AccountSummary[] getAccountSummaries(
            @WebParam(name="token") String token);

    /**
     * Returns detailed account information for the account with the given name
     * associated with the given user.
     *
     * @param token the authorization token representing the user to get
     * account summaries for.
     * @param name the name of the account to return.
     * @return detailed account information for the named account.
     */
    @WebMethod
    Account getAccount(
            @WebParam(name="token") String token,
            @WebParam(name="name") String name);

    /**
     * Convenience method for obtaining information on all of a user's accounts.
     * Should be used instead of {@link #getAccount(String, String)} if many
     * accounts need to be retrieved at once.
     *
     * @param token the authorization token representing the user to get
     * account summaries for.
     * @return all the accounts associated with the given user.
     *
     * @see #getAccount(String, String)
     */
    @WebMethod
    Account[] getAllAccounts(
            @WebParam(name="token") String token);

    /**
     * Updates all the given accounts for the given user. In the case that the
     * account didn't exist previously it will be added instead.
     *
     * @param token the authorization token representing the user to get
     * account summaries for.
     * @param accountsa sequence of accounts to update for the given user.
     */
    @WebMethod
    void updateAccounts(
            @WebParam(name="token") String token,
            @WebParam(name="accounts") Account[] accounts);
}
