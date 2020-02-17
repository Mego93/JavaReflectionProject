package codage;

public class Decodage {

	public static String encoder(String s) {
		return s.replace("\n", "#n");
	}
	
	public static String decoder(String s) {
		return s.replace("#n", "\n");	
	}
}
