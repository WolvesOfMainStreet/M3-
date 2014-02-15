package cs2340.woms.auth;

import java.util.ArrayList;

public class AndroidLoginManager implements LoginManager {

    public static AndroidLoginManager instance = new AndroidLoginManager();
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
        	if(pair.getName() == username){
        		if(pair.getPass() == password){
        			return true;
        		}
        		else return false;
        	}
        }

        return false;
    }

    public void register(String name, String pass){
    	Pair pair = new Pair(name, pass);
    	names.add(pair);
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

