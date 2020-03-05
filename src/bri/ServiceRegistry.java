package bri;

import java.util.HashMap;
import java.util.Map.Entry;

import codage.Decodage;

public class ServiceRegistry {

	static {
		classesEtat = new HashMap<Class<?>, Boolean>();
	}
	private static HashMap<Class<?>, Boolean> classesEtat;

/**
 * Ajoute une classe de service après test de norme BLTi
 * @param La classe de service c à ajouter
 */
	public static void addService(Class<?> c) {
		try {
			TestBRi.validation(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		classesEtat.put(c, true);
	}

/**
 * Renvoie la classe de service à l'indice en paramètre
 * @param L'indice numService
 * @return La classe de service
 * @throws InstantiationException
 * @throws IllegalAccessException
 */
	@SuppressWarnings("unchecked")
	public static Class<? extends Service> getServiceClass(int numService)
			throws InstantiationException, IllegalAccessException {
		return (Class<? extends Service>) classesEtat.keySet().toArray()[numService - 1];

	}

	/**
	 * Renvoie l'état de la classe de service
	 * @param La classe en question
	 * @return Un booléen de l'état
	 */
	public static boolean getEtatService(Class<?> c) {
		return classesEtat.get(c);
	}

	/**
	 * Enlève la classe de service du ServiceRegistry
	 * @param La classe de service c à enlever
	 * @return Un booléan de succès ou échec
	 */
	public static boolean removeService(Class<?> c) {
		for (Entry<Class<?>, Boolean> classUpdate : classesEtat.entrySet()) {
			if (c.equals(classUpdate.getKey())) {
				classesEtat.remove(classUpdate.getKey());
				return true;
			}
			
		}
		return false;
	}

	/**
	 * Renvoie la Map des services et leurs états
	 * @return La HashMap
	 */
	public static HashMap<Class<?>, Boolean> getClassesEtat() {
		return classesEtat;
	}

	/**
	 * Renvoie vrai ou faux si la classe est contenue dans le
	 * ServiceRegistry
	 * @param La classe c en question
	 * @return Un booléen de présence
	 */
	public static boolean containsClass(Class<?> c) {
		if (classesEtat.containsKey(c))
			return true;
		return false;
	}
	
	/**
	 * Remplace la classe de service contenue dans le ServiceRegistry
	 * par celle en paramètre
	 * @param La classe de service c à mettre à jour
	 * @return Un booléen de succès ou d'échec
	 */
	public static boolean replaceService(Class<?> c) {
		for (Entry<Class<?>, Boolean> classRemove : classesEtat.entrySet()) {
			if (classRemove.getKey().getName().contentEquals(c.getName()) && !classRemove.getKey().equals(c)) {
				classesEtat.remove(c);
				break;
			} else {
				return false;
			}
		}
		addService(c);
		return true;
	}

	/**
	 * Change l'état de la classe de service, allumé ou éteint
	 * @param La classe de service c
	 * @param Un booléen état
	 * @return Un booléen de comparaison en cas de succès/échec
	 */
	public static boolean changeService(Class<?> c, boolean etat) {
		boolean oldEtat = classesEtat.get(c);
		classesEtat.replace(c, etat);
		if (!classesEtat.get(c).equals(oldEtat))
			return true;
		return false;
	}

	/**
	 * Renvoie la liste des activités présentes, démarrées ou arrêtées
	 * @return un String de la liste
	 */
	public static String toStringue() {
		String result = "Activités présentes :";
		int i = 1;
		for (Entry<Class<?>, Boolean> classe : classesEtat.entrySet()) {
			result += "\n" + i + ") " + classe.getKey().getName();
			if (!classe.getValue())
				result += " : Service indisponible ";
			else
				result += " : Service disponible ";
			i++;
		}
		return Decodage.encoder(result);
	}

}
