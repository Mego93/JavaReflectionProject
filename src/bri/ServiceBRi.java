package bri;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.Scanner;

import codage.Decodage;

class ServiceBRi implements Runnable {

	private Socket client;

	ServiceBRi(Socket socket) {
		client = socket;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		// URLClassLoader sur ftp
		URLClassLoader urlcl;
		boolean stop = false;

		String error = "";
		do {
			try {
				urlcl = new URLClassLoader(new URL[] { new URL("ftp://localhost:2121/classes/") });
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);

				// Client
				if (client.getLocalPort() == 3000) {
					out.println(
							Decodage.encoder(ServiceRegistry.toStringue() + "\nTapez le num�ro de service d�sir� :"));
					int choix = Integer.parseInt(in.readLine());
					try {
						ServiceRegistry.getServiceClass(choix).getConstructor(Socket.class).newInstance(client).run();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Programmeur
				} else if (client.getLocalPort() == 3500) {
					boolean passwordError = false;
					out.println(Decodage.encoder("Bonjour programmeur , identifiez vous !\nLogin :"));
					String login = in.readLine();
					if (ProgrammerRegistry.containsLogin(login) != null) {
						Programmer p = ProgrammerRegistry.containsLogin(login);
						out.println(Decodage.encoder("Bonjour " + login + ", votre mot de passe : "));
						String password = in.readLine();
						if (ProgrammerRegistry.isPassword(p, password)) {
							do {
								out.println(Decodage
										.encoder("Que souhaitez vous faire (�crire le chiffre correspondant ou ne rien �crire si vous voulez passer) ? \n"
												+ "1: Fournir un nouveau service\n" + "2: Mettre-�-jour un service\n"
												+ "3: D�clarer un changement d�adresse de votre serveur ftp"));
								// int choix = Integer.parseInt(in.readLine());
								String choix = in.readLine();
								switch (choix) {
								case "1":
									out.println(Decodage
											.encoder("Donnez le nom de la classe � ajouter (plus son package):"));
									String classeName = in.readLine();
									Class<? extends Service> c = (Class<? extends Service>) urlcl.loadClass(classeName);
									if (ServiceRegistry.containsClass(c)) {
										out.print(Decodage.encoder("Classe d�j� pr�sente dans le ServiceRegistry\n"));
										break;
									}
									ServiceRegistry.addService(c);
									out.print(Decodage.encoder(
											"Classe " + c.getSimpleName() + " ajout�e dans le ServiceRegistry\n"));
									System.out.println(Decodage.decoder(ServiceRegistry.toStringue()));
									break;
								case "2":
									out.println(Decodage
											.encoder("Donnez le nom de la classe � mettre � jour (plus son package):"));
									String classeName2 = in.readLine();
									Class<? extends Service> c2 = (Class<? extends Service>) urlcl
											.loadClass(classeName2);
									if (!ServiceRegistry.containsClass(c2)) {
										out.print(Decodage.encoder("Classe non pr�sente, mise � jour impossible\n"));
										break;
									}
									ServiceRegistry.replaceService(c2);
									out.print(Decodage.encoder(
											"Classe " + c2.getSimpleName() + " mise � jour dans le ServiceRegistry\n"));
									System.out.println(Decodage.decoder(ServiceRegistry.toStringue()));
									break;
								case "3":
									out.println(Decodage.encoder("Donnez votre nouvelle adresse ftp :"));
									String newAddress = in.readLine();
									out.print("Adresse chang�e de " + p.getAdresseFtp());
									p.setAdresseFtp(newAddress);
									out.print("� " + newAddress + "\n");
									break;
								default:
									out.print(Decodage.encoder("Aucune action s�lectionn�e\n"));
									break;
								}
								out.println(Decodage.encoder("Voulez vous arreter ou vous d�connecter ? (stop/deco)"));
								String arretProg = in.readLine();
								if (arretProg.equals("deco")) {
									break;
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
					if (passwordError | ProgrammerRegistry.containsLogin(login) == null) {
						if (!passwordError) {
							error = "Login";
						}
						out.println(Decodage.encoder(error
								+ " inconnu, voulez vous arreter (stop) ou r�essayer ? (n'importe quel caract�re)"));
						String arretLogin = in.readLine();
						if (arretLogin.equals("stop"))
							stop = true;
					}
				}
			} catch (Exception e) {
				// Fin du service
			}
		} while (!stop);
		try {
			client.close();
		} catch (IOException e2) {
		}
	}

	protected void finalize() throws Throwable {
		client.close();
	}

	// lancement du service
	public void start() {
		(new Thread(this)).start();
	}

}
