/**
 * Classe de service dédiée aux amateurs BRi
 * @author VO Thierry & RISI Lucas
 * @version 3.5
 */


package servicesBRi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bri.IServiceBRi;
import bri.ServiceRegistry;
import codage.Decodage;

public class ServiceAmaBRi implements IServiceBRi {

	private Socket client;


	/*
	 * L'URLClassLoader est déclaré avant le switch et déclaré lors des ajouts/majs
	 * de services avant d'être mis à null En effet, le garbage collector récuperera
	 * l'urlcl référencé à null
	 */

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			boolean stop = false;

			System.out.println("Connexion d'un client au port : " + client.getLocalPort());
			do {
				try {

					// Le client tape le numéro du service de son choix
					out.println(
							Decodage.encoder(ServiceRegistry.toStringue() + "\nTapez le numéro de service désiré :"));
					int choix = Integer.parseInt(in.readLine());
					if (ServiceRegistry.containsClass(ServiceRegistry.getServiceClass(choix))) {
						System.out.println("Contenu");
						if (ServiceRegistry.getEtatService(ServiceRegistry.getServiceClass(choix))) {
							System.out.println("Démarré");
							try {
								ServiceRegistry.getServiceClass(choix).getConstructor(Socket.class).newInstance(client)
										.run();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							System.out.println("Non démarré");
							out.print(Decodage.encoder("Service non démarré\n"));
						}
					} else {
						System.out.println("Inconnu");
						out.print(Decodage.encoder("Service inconnu\n"));
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

		} catch (Exception e) {
			// Fin du service
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

	public void setSocket(Socket s) {
		this.client = s;
	}

}
