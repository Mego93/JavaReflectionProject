/**
 * Classe de test pour la norme BLTi
 * @author VO Thierry & RISI Lucas
 * @version 1.5
 */

package bri;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;

import codage.Decodage;

public class TestBRi {

	public static String validation(Class<?> classe) {
		int i = classe.getModifiers();
		StringBuilder sb = new StringBuilder();
		boolean verifServ = false;
		boolean verifParam1 = false;
		boolean verifParam2 = false;
		boolean verifField = false;
		boolean verifMethod = false;
		boolean verifGen = true;
		if (!Modifier.isPublic(i)) {
			verifGen = false;
			sb.append("Classe non publique \n");
		}
		for (Class<?> c : classe.getInterfaces()) {
			if (c.getSimpleName().equals("Service")) {
				verifServ = true;
				break;
			}
		}
		if (!verifServ) {
			verifGen = false;
			sb.append("N'impl�mente pas de BRi.Service \n");
		}
		Constructor<?>[] j = classe.getConstructors();
		for (Constructor<?> con : j) {
			Class<?>[] c = con.getParameterTypes();
			for (Class<?> constructeur : c)
				if (constructeur.equals(Socket.class)) {
					verifParam1 = true;
					break;
				}
			if (verifParam1)
				if (Modifier.isPublic(con.getModifiers()) && con.getExceptionTypes().length == 0) {
					verifParam2 = true;
					break;
				}
		}
		if (!verifParam2) {
			verifGen = false;
			sb.append("Ne poss�de pas de constructeur public avec socket en parametre et sans exception \n");
		}

		for (Field f : classe.getDeclaredFields()) {
			if (Modifier.isFinal(f.getModifiers()) && f.getType().equals(Socket.class)
					&& Modifier.isPrivate(f.getModifiers())) {
				verifField = true;
				break;
			}
			if (!verifField) {
				verifGen = false;
				sb.append("Ne poss�de pas de private final Socket \n");
			}
		}

		for (Method m : classe.getMethods())
			if (Modifier.isStatic(m.getModifiers()) && m.getExceptionTypes().length == 0
					&& m.getName().equals("toStringue") && m.getGenericReturnType().equals(String.class)) {
				verifMethod = true;
				break;
			}
		if (!verifMethod) {
			verifGen = false;
			sb.append("Ne poss�de pas de m�thode toStringue sans exception \n");
		}

		if (!verifGen)
			return (Decodage.encoder(sb.toString() + "La classe ne respecte pas la norme BLTi\n"));

		return "OK";

	}
}
