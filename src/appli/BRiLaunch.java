package appli;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import bri.ServeurBRi;
import bri.Service;
import bri.ServiceRegistry;

public class BRiLaunch {
	private final static int PORT_SERVICE = 3000;
	
	public static void main(String[] args) throws MalformedURLException {
		@SuppressWarnings("resource")
		Scanner clavier = new Scanner(System.in);
		
		// URLClassLoader sur ftp
		URLClassLoader urlcl = new URLClassLoader(new URL[] { new URL("ftp://localhost:2121/classes/")}) {
			public Class<?> loadClass(String name) throws ClassNotFoundException{
				return super.loadClass(name);
			}
		};
		
		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
		System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		
		new Thread(new ServeurBRi(PORT_SERVICE)).start();
		
		while (true){
				try {
					String classeName = clavier.next();
					Class<? extends Service> c = (Class<? extends Service>) urlcl.loadClass(classeName);
					ServiceRegistry.addService(c);
					// charger la classe et la déclarer au ServiceRegistry
				} catch (Exception e) {
					System.out.println(e);
				}
			}		
	}
}
