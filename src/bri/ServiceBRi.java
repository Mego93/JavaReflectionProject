/**
 * Classe de service dédiée aux programmeurs BRi
 * @author VO Thierry & VYAS Ishan
 * @version 3.5
 */
package bri;

import java.io.*;
import java.net.*;

import bri.Programmer;
import bri.ProgrammerRegistry;
import bri.Service;
import bri.ServiceRegistry;
import codage.Decodage;

public class ServiceBRi implements Runnable {

	private Socket client;

	public ServiceBRi(Socket socket) {
		client = socket;

	}

	@SuppressWarnings("unchecked")
	public void run() {

		// URLClassLoader sur ftp
		try {

			URLClassLoader urlcl = new URLClassLoader(new URL[] { new URL("ftp://localhost:2121/classes/") });
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			boolean stop = false;

			String error = "";
			switch (client.getLocalPort()) {

			case 3000:
				do {
					try {

						// Le client tape le numéro du service de son choix
						out.println(Decodage
								.encoder(ServiceRegistry.toStringue() + "\nTapez le numéro de service désiré :"));
						int choix = Integer.parseInt(in.readLine());
						if (ServiceRegistry.containsClass(ServiceRegistry.getServiceClass(choix))) {
							try {
								ServiceRegistry.getServiceClass(choix).getConstructor(Socket.class).newInstance(client)
										.run();
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
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
				break;

			case 3500:
				do {
					try {
						boolean passwordError = false;

						out.println(Decodage.encoder("Bonjour programmeur , identifiez vous !\nLogin :"));
						String login = in.readLine();

						// Si le programmer registry ne connait pas le login
						if (ProgrammerRegistry.containsLogin(login) != null) {
							Programmer p = ProgrammerRegistry.containsLogin(login);
							out.println(Decodage.encoder("Bonjour " + login + ", votre mot de passe : "));
							String password = in.readLine();
							// Si le login correspond au mot de passe donné
							if (ProgrammerRegistry.isPassword(p, password)) {
								do {
									URLClassLoader urlclProg = new URLClassLoader(
											new URL[] { new URL(p.getAdresseFtp()) });
									out.println(Decodage.encoder(
											"Que souhaitez vous faire (écrire le chiffre correspondant ou ne rien écrire si vous voulez passer) ? \n"
													+ "1: Fournir un nouveau service\n"
													+ "2: Mettre-à-jour un service\n"
													+ "3: Déclarer un changement d’adresse de votre serveur ftp"));
									String choix = in.readLine();
									switch (choix) {

									// 1 : Ajout de la classe donnée
									case "1":
										out.println(Decodage
												.encoder("Donnez le nom de la classe à ajouter (plus son package):"));
										String classeName = in.readLine();
										Class<? extends Service> c = (Class<? extends Service>) urlclProg
												.loadClass(classeName);
										if (ServiceRegistry.containsClass(c)) {
											out.print(
													Decodage.encoder("Classe déjà présente dans le ServiceRegistry\n"));
											break;
										}
										ServiceRegistry.addService(c);
										out.print(Decodage.encoder(
												"Classe " + c.getSimpleName() + " ajoutée dans le ServiceRegistry\n"));
										System.out.println(Decodage.decoder(ServiceRegistry.toStringue()));
										break;

									// 2 : Mise à jour de la classe donnée
									case "2":
										out.println(Decodage.encoder(
												"Donnez le nom de la classe à mettre à jour (plus son package):"));
										String classeName2 = in.readLine();
										Class<? extends Service> c2 = (Class<? extends Service>) urlclProg
												.loadClass(classeName2);
										if (!ServiceRegistry.containsClass(c2)) {
											out.print(
													Decodage.encoder("Classe non présente, mise à jour impossible\n"));
											break;
										}
										ServiceRegistry.replaceService(c2);
										out.print(Decodage.encoder("Classe " + c2.getSimpleName()
												+ " mise à jour dans le ServiceRegistry\n"));
										System.out.println(Decodage.decoder(ServiceRegistry.toStringue()));
										break;

									// 3 : Changement de l'adresse ftp du programmeur
									case "3":
										out.println(Decodage.encoder(
												"Donnez votre nouvelle adresse ftp ( ftp://localhost:2121/nom_repertoire ) :"));
										String newAddress = in.readLine();
										out.print("Adresse changée de " + p.getAdresseFtp());
										p.setAdresseFtp(newAddress);
										out.print("à " + newAddress + "\n");
										break;

									// Non reconnu / rien : on sort du switch
									default:
										out.print(Decodage.encoder("Aucune action sélectionnée\n"));
										break;
									}

									// Fin du service
									out.println(Decodage.encoder(
											"Voulez vous arreter ou vous déconnecter ? (stop/deco/caractère quelconque)"));
									String arretProg = in.readLine();

									// Déconnexion du programmeur
									if (arretProg.equals("deco")) {
										break;
										// Arret du service pour programmeur
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
						if (passwordError | ProgrammerRegistry.containsLogin(login) == null) {
							if (!passwordError) {
								error = "Login";
							}

							// Demande au programmeur
							out.println(Decodage.encoder(error
									+ " inconnu, voulez vous arreter ou réessayer ? (stop/caractère quelconque)"));
							String arretLogin = in.readLine();
							if (arretLogin.equals("stop"))
								stop = true;
						}

					} catch (Exception e) {
						// Fin du service
					}
				} while (!stop);
				break;
			default:
				out.print("Erreur");
				break;
			}
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

}
