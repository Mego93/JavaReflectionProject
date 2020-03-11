/**
 * Classe d'application de lancement 
 * @author VO Thierry & RISI Lucas
 * @version 3.5
 */

package appli;

import java.net.MalformedURLException;

import bri.UserRegistry;
import bri.Utilisateur;
import bri.ServeurBRi;
import servicesBRi.ServiceAmaBRi;
import servicesBRi.ServiceProgBRi;
import utilisateurs.Amateur;
import utilisateurs.Programmeur;

public class BRiLaunch {
	private final static int PORT_SERVICE = 3000;
	private final static int PORT_PROG = 3500;

	public static void main(String[] args) throws MalformedURLException {

		Utilisateur prog1 = new Programmeur("toto", "toto", "ftp://localhost:2121/toto/");
		Utilisateur prog2 = new Programmeur("blop", "kpop", "ftp://localhost:2121/blop/");
		Utilisateur prog3 = new Programmeur("rosi", "pepelog", "ftp://localhost:2121/rosi/");
		Utilisateur prog4 = new Programmeur("link", "global", "ftp://localhost:2121/link/");
		
		Utilisateur ama1 = new Amateur("titi", "titi");
		Utilisateur ama2 = new Amateur("lucas", "risi");


		UserRegistry.getUserList().add(prog1);
		UserRegistry.getUserList().add(prog2);
		UserRegistry.getUserList().add(prog3);
		UserRegistry.getUserList().add(prog4);
		
		UserRegistry.getUserList().add(ama1);
		UserRegistry.getUserList().add(ama2);



		new Thread(new ServeurBRi(PORT_PROG, new ServiceProgBRi())).start();
		System.out.println("Serveur pour programmeurs lancé, port " + PORT_PROG);
		new Thread(new ServeurBRi(PORT_SERVICE, new ServiceAmaBRi())).start();
		System.out.println("Serveur pour amateurs lancé, port " + PORT_SERVICE);
	
	}
}
