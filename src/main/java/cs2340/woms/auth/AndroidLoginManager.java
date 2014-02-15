package cs2340.woms.auth;

public class AndroidLoginManager implements LoginManager {

    public static LoginManager instance = new AndroidLoginManager();

    private AndroidLoginManager() { }

    @Override
    public void handleLogin(String username, String password) {

    }
}
