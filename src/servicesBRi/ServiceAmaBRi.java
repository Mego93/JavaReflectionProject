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
import bri.UserRegistry;
import bri.Utilisateur;
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

			String error = "";
			System.out.println("Connexion d'un client au port : " + client.getLocalPort());
			do {
				try {
					boolean passwordError = false;

					out.println(Decodage.encoder("Bonjour amateur, identifiez vous !\nLogin :"));
					String login = in.readLine();
					// Si l'UserRegistry ne connait pas le login
					if (UserRegistry.containsLogin(login) != null) {
						Utilisateur p = UserRegistry.containsLogin(login);
						out.println(Decodage.encoder("Bonjour " + login + ", votre mot de passe : "));
						String password = in.readLine();

						// Si le login correspond au mot de passe donné
						if (UserRegistry.isPassword(p, password)) {
							do {
								do {
								// Le client tape le numéro du service de son choix
								out.println(Decodage.encoder(
										ServiceRegistry.toStringue() + "\nTapez le numéro de service désiré, n'importe quel autre caractère pour actualiser :"));
							
									try {
										int choix = Integer.parseInt(in.readLine());
										if (ServiceRegistry.containsClass(ServiceRegistry.getServiceClass(choix))) {
											if (ServiceRegistry
													.getEtatService(ServiceRegistry.getServiceClass(choix))) {
												ServiceRegistry.getServiceClass(choix).getConstructor(Socket.class)
														.newInstance(client).run();
												break;
											} else {
												out.print(Decodage.encoder("Service non démarré\n"));
											}
										} else {
											out.print(Decodage.encoder("Service inconnu\n"));
										}
									} catch (Exception e) {
									}
								} while (true);

								// Fin du service
								out.println(Decodage.encoder(
										"Fin du service, voulez vous arreter ou vous déconnecter ? (stop/deco/caractère quelconque)"));
								String arretProg = in.readLine();

								// Déconnexion de l'amateur
								if (arretProg.equals("deco")) {
									break;
									// Arret du service pour amateur
								} else if (arretProg.equals("stop")) {
									stop = true;
									break;
								}
							} while (true);
						} else {
							passwordError = true;
							error = "Mot de passe";
						}
					}

					// En cas d'erreur de login / mot de passe
					if (passwordError | UserRegistry.containsLogin(login) == null) {
						if (!passwordError) {
							error = "Login";
						}

						// Demande à l'amateur
						out.println(Decodage.encoder(
								error + " inconnu, voulez vous arreter ou réessayer ? (stop/caractère quelconque)"));
						String arretLogin = in.readLine();
						if (arretLogin.equals("stop"))
							stop = true;
					}
				} catch (Exception e) {
					out.print(Decodage.encoder("Exception attrapée, reessayez\n"));
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
