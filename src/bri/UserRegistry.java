package bri;

import java.util.List;
import java.util.Vector;


public class UserRegistry {

	static {
		userList = new Vector<Utilisateur>();

	}
	private static List<Utilisateur> userList;
	public static List<Utilisateur> getUserList() {
		return userList;
	}
	
	public static Utilisateur containsLogin(String login) {
		for(Utilisateur p : userList) {
			if(p.getLogin().equals(login))
				return p;
		}
		return null;
	}
	
	public static void addProgrammer(Utilisateur p) {
		if(userList.contains(p))
			return;
		userList.add(p);
	}

	
	public static boolean isPassword(Utilisateur p,String password) {
		if(p.getPassword().equals(password))
			return true;
		return false;
	}
}
