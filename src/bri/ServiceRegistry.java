package bri;

import java.util.List;
import java.util.Vector;

import codage.Decodage;

public class ServiceRegistry {
	// cette classe est un registre de services
	// partag?e en concurrence par les clients et les "ajouteurs" de services,
	// un Vector pour cette gestion est pratique

	static {
		servicesClasses = new Vector();
	}
	private static List<Class<?>> servicesClasses;

// ajoute une classe de service apr?s contr?le de la norme BLTi
	public static void addService(Class<?> c) {
		try {
			TestBRi.validation(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		servicesClasses.add(c);
	}

// renvoie la classe de service (numService -1)	
	@SuppressWarnings("unchecked")
	public static Class<? extends Service> getServiceClass(int numService)
			throws InstantiationException, IllegalAccessException {
		return (Class<? extends Service>) servicesClasses.get(numService - 1);
	}

	public static boolean containsClass(Class<?> c) {
		if (servicesClasses.contains(c))
			return true;
		return false;
	}

	public static boolean replaceService(Class<?> c) {
		for (Class<?> classUpdate : servicesClasses)
			if (classUpdate.getName().contentEquals(c.getName()) && !classUpdate.equals(c)) {
				servicesClasses.remove(classUpdate);
				break;
			} else {
				return false;
			}
		addService(c);
		return true;
	}

// liste les activit?s pr?sentes
	public static String toStringue() {
		String result = "Activités présentes :";
		for (Class<?> c : servicesClasses)
			result += "\n" 
				//	+ c.toString() + "  "
				//	+ c.getSimpleName() + "  " 
					+ c.getName() + "  "
				//	+ c.toGenericString()
					;
		return Decodage.encoder(result);
	}

}
