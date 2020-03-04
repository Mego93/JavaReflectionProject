package servicesBRi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import bri.IServiceBRi;
import bri.Programmer;
import bri.ProgrammerRegistry;
import bri.Service;
import bri.ServiceRegistry;
import codage.Decodage;

public class ServiceProgBRi implements IServiceBRi {

	private Socket client;


	/* L'URLClassLoader est déclaré avant le switch et déclaré
	 * lors des ajouts/majs de services avant d'être mis à null
	 * En effet, le garbage collector récuperera l'urlcl 
	 * référencé à null
	 */

	
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			URLClassLoader urlcl; 
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			boolean stop = false;

			String error = "";
			System.out.println("Connexion d'un client au port : " + client.getLocalPort());

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
									out.println(Decodage.encoder(ServiceRegistry.toStringue()
											+ "\nQue souhaitez vous faire (écrire le chiffre correspondant ou ne rien écrire si vous voulez passer) ? \n"
											+ "1: Fournir un nouveau service\n" + "2: Mettre-à-jour un service\n"
											+ "3: Déclarer un changement d’adresse de votre serveur ftp\n"
											+ "4: Arrêter ou démarrer un service\n" + "5: Désinstaller un service"));
									String choix = in.readLine();
									switch (choix) {

									// 1 : Ajout de la classe donnée
									case "1":
										urlcl = new URLClassLoader(new URL[] { new URL("ftp://localhost:2121/") });
										out.println(Decodage.encoder(
												"Donnez le nom de la classe à ajouter (situé dans votre repository):"));
										String classeName = in.readLine();
										Class<? extends Service> classeAjout = (Class<? extends Service>) urlcl
												.loadClass(p.getLogin() + "." + classeName);
										if (ServiceRegistry.containsClass(classeAjout)) {
											out.print(
													Decodage.encoder("Classe déjà présente dans le ServiceRegistry\n"));
											break;
										}
										ServiceRegistry.addService(classeAjout);
										out.print(Decodage.encoder(
												"Classe " + classeAjout.getSimpleName() + " ajoutée dans le ServiceRegistry\n"));
										urlcl = null;
										break;

									// 2 : Mise à jour de la classe donnée
									case "2":
										urlcl = new URLClassLoader(new URL[] { new URL("ftp://localhost:2121/") });
										out.println(Decodage.encoder(
												"Donnez le nom de la classe à mettre à jour (situé dans votre repository et dans le ServiceRegistry):"));
										String classeName2 = in.readLine();
										Class<? extends Service> classeUpdate = (Class<? extends Service>) urlcl
												.loadClass(p.getLogin() + "." + classeName2);
										if (!ServiceRegistry.containsClass(classeUpdate)) {
											out.print(
													Decodage.encoder("Classe non présente, mise à jour impossible\n"));
											urlcl=null;
											break;
										}
										ServiceRegistry.replaceService(classeUpdate);
										out.print(Decodage.encoder("Classe " + classeUpdate.getSimpleName()
												+ " mise à jour dans le ServiceRegistry\n"));
										urlcl=null;
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

									// 4 : Démarrage ou arrêt d'un service
									case "4":
										out.println(Decodage.encoder(
												"Voulez vous démarrer ou arrêter un service (ne rien écrire pour partir) ? (1/2)"));
										String repArretDem = in.readLine();
										switch (repArretDem) {

										case "1":
											urlcl = new URLClassLoader(new URL[] { new URL("ftp://localhost:2121/") });
											out.println(Decodage
													.encoder(ServiceRegistry.toStringue() + "\nTapez le numéro de service désiré :"));
											int choixDem = Integer.parseInt(in.readLine());
											Class<? extends Service> classeStart = ServiceRegistry.getServiceClass(choixDem);
											if (ServiceRegistry.getEtatService(classeStart) == true) {
												out.print(Decodage.encoder("Le service est déjà démarré \n"));
												urlcl=null;
												break;
											}
											if (ServiceRegistry.changeService(classeStart, true)) {
												out.print(Decodage.encoder("Service démarré \n"));
											} else {
												out.print(Decodage.encoder("Erreur de démarrage \n"));
											}
											urlcl = null;
											break;
										case "2":
											out.println(Decodage
													.encoder(ServiceRegistry.toStringue() + "\nTapez le numéro de service désiré :"));
											int choixArret = Integer.parseInt(in.readLine());
											Class<? extends Service> classeStop = ServiceRegistry.getServiceClass(choixArret);
											if (ServiceRegistry.getEtatService(classeStop) == false) {
												out.print(Decodage.encoder("Le service est déjà arrêté \n"));
												break;
											}
											if (ServiceRegistry.changeService(classeStop, false)) {
												out.print(Decodage.encoder("Service arrêté \n"));
											} else {
												out.print(Decodage.encoder("Erreur d'arrêt \n"));
											}
											break;
										default:
											out.print(Decodage.encoder("Aucune action sélectionnée\n"));
											break;
										}
										break;

									// 5 : Désinstallation d'un service
									case "5":
										out.println(Decodage.encoder(ServiceRegistry.toStringue() + "\nQuel service à désinstaller ?"));
										int choixDesinstall = Integer.parseInt(in.readLine());

										if(ServiceRegistry.removeService(ServiceRegistry.getServiceClass(choixDesinstall))) {
											out.print(Decodage.encoder("Désinstallation impossible\n"));
											break;
										}
										out.print(Decodage.encoder("Désinstallation effectuée, le service n'est plus dans le ServiceRegistry\n"));
										break;
									// Non reconnu / rien : on sort du switch
									default:
										out.print(Decodage.encoder("Aucune action sélectionnée\n"));
										break;
									}

									// Fin du service
									out.println(Decodage.encoder(
											"Fin du service, voulez vous arreter ou vous déconnecter ? (stop/deco/caractère quelconque)"));
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
						out.print(Decodage.encoder("Exception attrapée, veuillez relancer\n"));
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
