package bri;

import java.util.List;
import java.util.Vector;


public class ProgrammerRegistry {

	static {
		programmerList = new Vector();

	}
	private static List<Programmer> programmerList;
	public static List<Programmer> getProgrammerList() {
		return programmerList;
	}
	
	public static Programmer containsLogin(String login) {
		for(Programmer p : programmerList) {
			if(p.getLogin().equals(login))
				return p;
		}
		return null;
	}
	
	public static void addProgrammer(Programmer p) {
		if(programmerList.contains(p))
			return;
		programmerList.add(p);
	}

	
	public static boolean isPassword(Programmer p,String password) {
		if(p.getPassword().equals(password))
			return true;
		return false;
	}
}
