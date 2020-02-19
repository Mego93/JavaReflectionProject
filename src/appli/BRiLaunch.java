package appli;

import java.net.MalformedURLException;


import bri.Programmer;
import bri.ProgrammerRegistry;
import serveurs.ServeurAmateurBRi;
import serveurs.ServeurProgBRi;

public class BRiLaunch {
	private final static int PORT_SERVICE = 3000;
	private final static int PORT_PROG = 3500;

	public static void main(String[] args) throws MalformedURLException {
	
		ProgrammerRegistry.getProgrammerList().add( new Programmer("toto", "toto", "ftp://localhost:2121/toto/"));
		ProgrammerRegistry.getProgrammerList().add( new Programmer("blop", "kpop", "ftp://localhost:2121/blop/"));
		ProgrammerRegistry.getProgrammerList().add( new Programmer("rosi", "pepelog", "ftp://localhost:2121/rosi/"));
		ProgrammerRegistry.getProgrammerList().add( new Programmer("link", "global", "ftp://localhost:2121/link/"));


		new Thread(new ServeurAmateurBRi(PORT_SERVICE)).start();
		System.out.println("Serveur pour amateurs lancé, port " + PORT_SERVICE);
		new Thread(new ServeurProgBRi(PORT_PROG)).start();
		System.out.println("Serveur pour programmeurs lancés, port " + PORT_PROG);
		

	}
}
