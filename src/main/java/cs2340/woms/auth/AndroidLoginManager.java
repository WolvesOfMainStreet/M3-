package cs2340.woms.auth;

public class AndroidLoginManager implements LoginManager {

    public static LoginManager instance = new AndroidLoginManager();

    private AndroidLoginManager() { }

    @Override
    public boolean handleLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        if (username.equals("admin") && password.equals("pass123")) {
            return true;
        }

        return false;
    }
}
