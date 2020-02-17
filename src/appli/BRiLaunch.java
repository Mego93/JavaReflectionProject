package appli;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

import bri.Programmer;
import bri.ProgrammerRegistry;
import bri.ServeurBRi;
import bri.Service;
import bri.ServiceRegistry;

public class BRiLaunch {
	private final static int PORT_SERVICE = 3000;
	private final static int PORT_PROG = 3500;

	public static void main(String[] args) throws MalformedURLException {


		
//		System.out.println("Bienvenue dans votre gestionnaire dynamique d'activité BRi");
//		System.out.println("Pour ajouter une activité, celle-ci doit être présente sur votre serveur ftp");
//		System.out.println("A tout instant, en tapant le nom de la classe, vous pouvez l'intégrer");
//		System.out.println("Les clients se connectent au serveur 3000 pour lancer une activité");
		

		ProgrammerRegistry.getProgrammerList().add( new Programmer("toto", "titi", "ftp://localhost:2121/toto/"));
		ProgrammerRegistry.getProgrammerList().add( new Programmer("popo", "papa", "ftp://localhost:2121/popo/"));

		new Thread(new ServeurBRi(PORT_SERVICE)).start();
		System.out.println("Serveur pour amateurs lancé, port " + PORT_SERVICE);
		new Thread(new ServeurBRi(PORT_PROG)).start();
		System.out.println("Serveur pour programmeurs lancés, port " + PORT_PROG);
		
//		while (true){
//				try {
//					String classeName = clavier.next();
//					for(URL u : urlcl.getURLs())
//						System.out.println(u.toString());
//					Class<? extends Service> c = (Class<? extends Service>) urlcl.loadClass(classeName);
//					ServiceRegistry.addService(c);
//				} catch (Exception e) {
//					System.out.println(e);
//				}
//			}		
	}
}
