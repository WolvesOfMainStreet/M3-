package cs2340.woms.model.database;

import javax.jws.WebService;

import cs2340.woms.model.database.ct.Account;
import cs2340.woms.model.database.ct.AccountSummary;

@WebService(endpointInterface="cs2340.woms.model.database.RemoteDatabaseAPI")
public class RemoteDatabaseAPIImpl implements RemoteDatabaseAPI {

    public RemoteDatabaseAPIImpl() { }

    @Override
    public String login(String username, String password) {
        // TODO: Implement
        return null;
    }

    @Override
    public void logout(String token) {
        // TODO: Implement
    }

    @Override
    public AccountSummary[] getAccountSummaries(String token) {
        // TODO: Implement
        return null;
    }

    @Override
    public Account getAccount(String token, String name) {
        // TODO: Implement
        return null;
    }

    @Override
    public Account[] getAllAccounts(String token) {
        // TODO: Implement
        return null;
    }

    @Override
    public void updateAccounts(String token, Account[] accounts) {
        // TODO: Implement
    }
}
