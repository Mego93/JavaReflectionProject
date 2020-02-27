package bri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import codage.Decodage;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partag?e en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = new Vector();
		classesEtat = new HashMap<Class<?>, Boolean>();
	}
	private static List<Class<?>> servicesClasses;
	private static HashMap<Class<?>, Boolean> classesEtat;

// ajoute une classe de service apr?s contr?le de la norme BLTi
	public static void addService(Class<?> c) {
		try {
			TestBRi.validation(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getServicesClasses().add(c);
		classesEtat.put(c, false);
	}

// renvoie la classe de service (numService -1)	
	@SuppressWarnings("unchecked")
	public static Class<? extends Service> getServiceClass(int numService)
			throws InstantiationException, IllegalAccessException {
//		if (containsClass(getServicesClasses().get(numService - 1))) {
//			System.out.println("Entrée 1");
//			if (classesEtat.get(getServicesClasses().get(numService - 1)).equals(true)) {
//				System.out.println("Entrée 2");
//				return (Class<? extends Service>) getServicesClasses().get(numService - 1);
//			}
//		}
//		return null;
		return (Class<? extends Service>) getServicesClasses().get(numService - 1);

	}

	public static boolean getEtatService(Class<?> c) {
		return classesEtat.get(c);
	}

	public static void removeService(Class<?> c) {
		for (Class<?> classUpdate : getServicesClasses())
			if (classUpdate.getName().contentEquals(c.getName()) && !classUpdate.equals(c)) {
				getServicesClasses().remove(classUpdate);
				classesEtat.remove(c);
				break;
			}
	}

	public static HashMap<Class<?>, Boolean> getClassesEtat() {
		return classesEtat;
	}

	public static boolean containsClass(Class<?> c) {
		if (getServicesClasses().contains(c) && classesEtat.containsKey(c))
			return true;
		return false;
	}

	public static boolean replaceService(Class<?> c) {
		for (Class<?> classUpdate : getServicesClasses())
			if (classUpdate.getName().contentEquals(c.getName()) && !classUpdate.equals(c)) {
				getServicesClasses().remove(classUpdate);
				classesEtat.remove(c);
				break;
			} else {
				return false;
			}
		addService(c);
		return true;
	}

	public static boolean changeService(Class<?> c, boolean etat) {
		boolean oldEtat = classesEtat.get(c);
		classesEtat.replace(c, etat);
		if(!classesEtat.get(c).equals(oldEtat))
			return true;
		return false;
	}

// liste les activit?s pr?sentes
	public static String toStringue() {
		String result = "Activités présentes :";
		for (Class<?> c1 : getServicesClasses()) {
			result += "\n" + getServicesClasses().indexOf(c1) + 1 + ": " + c1.getName();
		}
		for (Entry<Class<?>, Boolean> c2 : classesEtat.entrySet()) {
			if (!c2.getValue())
				result += " : Service indisponible ";
			else
				result += " : Service disponible ";
		}
		return Decodage.encoder(result);
	}

	public static List<Class<?>> getServicesClasses() {
		return servicesClasses;
	}

}
