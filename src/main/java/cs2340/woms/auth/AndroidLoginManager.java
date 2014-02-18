package cs2340.woms.auth;

import java.util.ArrayList;

public class AndroidLoginManager implements LoginManager {

    public static LoginManager instance = new AndroidLoginManager();
    private ArrayList<Pair> names;

    private AndroidLoginManager() {
        names = new ArrayList<Pair>();

        // Admin Account
        register("admin", "pass123");
    }

    @Override
    public boolean handleLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        for(Pair pair: names){
        	if(pair.getName().equals(username)){
        		if(pair.getPass().equals(password)){
        			return true;
        		}
        		else return false;
        	}
        }

        return false;
    }

    @Override
    public boolean register(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        // Don't re-register an already registered name username.
        for (Pair login: names) {
            if (login.name.equals(username)) {
                return false;
            }
        }

    	Pair pair = new Pair(username, password);
    	names.add(pair);
    	return true;
    }

    private class Pair{
    	private String name, pass;

    	public Pair(String name, String pass){
    		this.name = name;
    		this.pass = pass;
    	}
    	public String getName(){
    		return name;
    	}
    	public String getPass(){
        	return pass;
    	}
    }
}

