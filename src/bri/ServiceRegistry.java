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
	public static Class<? extends Service> getServiceClass(int numService) throws InstantiationException, IllegalAccessException {
		return (Class<? extends Service>) servicesClasses.get(numService-1);
	}
	
// liste les activit?s pr?sentes
	public static String toStringue() {
		String result = "Activités présentes :\n";
		for(Class<?> c : servicesClasses)
			result+=c.toString()+"\n";
		return Decodage.encoder(result);
	}

}
