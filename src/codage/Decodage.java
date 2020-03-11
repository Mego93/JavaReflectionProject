/**
 * Classe de codage pour caractères de fin de ligne
 * @author VO Thierry & RISI Lucas
 * @version 1.0
 */
package codage;

public class Decodage {

	public static String encoder(String s) {
		return s.replace("\n", "#n");
	}
	
	public static String decoder(String s) {
		return s.replace("#n", "\n");	
	}
}
