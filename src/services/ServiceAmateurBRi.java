package services;
/**
 * Classe de service dédiée aux amateurs BRi
 * @author VO Thierry & VYAS Ishan
 * @version 3.5
 */

import java.io.*;
import java.net.*;
import bri.ServiceRegistry;
import codage.Decodage;

public class ServiceAmateurBRi implements Runnable {

	private Socket client;

	public ServiceAmateurBRi(Socket socket) {
		client = socket;
	}

	@SuppressWarnings({ "unused", "resource" })
	public void run() {
	
		boolean stop = false;
		do {
			try {
				
				// URLClassLoader sur le serveur ftp
				URLClassLoader urlcl = new URLClassLoader(new URL[] { new URL("ftp://localhost:2121/classes/") });
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				
				// Le client tape le numéro du service de son choix
				out.println(Decodage.encoder(ServiceRegistry.toStringue() + "\nTapez le numéro de service désiré :"));
				int choix = Integer.parseInt(in.readLine());
				try {
					ServiceRegistry.getServiceClass(choix).getConstructor(Socket.class).newInstance(client).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// Fin du service
				out.println(Decodage.encoder("Voulez vous arreter ou vous déconnecter ? (stop/deco)"));
				String arretProg = in.readLine();
				if (arretProg.equals("stop"))
					stop = true;
			} catch (Exception e) {
				// Fin du service
			}
		} while (!stop);
		try {
			client.close();
		} catch (IOException e2) {
		}
	}

	/**
	 * Ferme le client
	 */
	protected void finalize() throws Throwable {
		client.close();
	}

	/**
	 * Crée un thread et le lance
	 */
	public void start() {
		(new Thread(this)).start();
	}

}
