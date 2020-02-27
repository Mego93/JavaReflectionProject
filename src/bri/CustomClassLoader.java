package bri;

import java.net.URLClassLoader;

import codage.Decodage;

public class CustomClassLoader {
	private URLClassLoader urlcl;

	public URLClassLoader getUrlcl() {
		return urlcl;
	}

	public void setUrlcl(URLClassLoader urlcl) {
		this.urlcl = urlcl;
	}
	
	public URLClassLoader uninstallClass(String classe,URLClassLoader urlclCopy) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		URLClassLoader cloneUrlcl = new URLClassLoader(urlclCopy.getURLs());
		Class<? extends Service> classeDesinstal = (Class<? extends Service>) urlcl
				.loadClass(classe);
		if (!ServiceRegistry.containsClass(classeDesinstal)) {
			return null;
		}
		for (Class<?> classeCompare : ServiceRegistry.getServicesClasses()) {
			if (classeCompare.getName().contentEquals(classeDesinstal.getName()) && !classeCompare.equals(classeDesinstal)) {
				ServiceRegistry.getServicesClasses().remove(classeCompare);
				ServiceRegistry.getClassesEtat().remove(classeDesinstal);
				break;
			}
		}
		Service classeDup= classeDesinstal.newInstance(); // on crée une nouvelle instance et on la "déréférence"
		classeDup=null;
		System.gc();
		classeDesinstal=null; // la classe reste stockée dans le classloader
		System.gc();
		this.urlcl=null; // on décharge le classloader
		System.gc();
		this.urlcl = cloneUrlcl; // on recharge le classloader d'origine
		return urlcl; 
	}
}
