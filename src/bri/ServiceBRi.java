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

	public void run() {
		// URLClassLoader sur ftp
		URLClassLoader urlcl;
		try {
			urlcl = new URLClassLoader(new URL[] { new URL("ftp://localhost:2121/classes/") });
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			if (client.getLocalPort() == 3000) {
				out.println(Decodage.encoder(ServiceRegistry.toStringue() + "\nTapez le numéro de service désiré :"));
				int choix = Integer.parseInt(in.readLine());
				try {
					ServiceRegistry.getServiceClass(choix).getConstructor(Socket.class).newInstance(client).run();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (client.getLocalPort() == 3500) {
				out.println(Decodage.encoder("\nBonjour programmeur , identifiez vous !\nLogin :"));
				String login = in.readLine();
				if (ProgrammerRegistry.containsLogin(login) != null) {
					Programmer p = ProgrammerRegistry.containsLogin(login);
					out.println(Decodage.encoder("\n Bonjour " + login + ", votre mot de passe : "));
					String password = in.readLine();
					if (ProgrammerRegistry.isPassword(p, password)) {
						out.println(Decodage.encoder(
								"Que souhaitez vous faire ? \n 1: Fournir un nouveau service \n	2: Mettre-à-jour un service \n 3: Déclarer un changement d’adresse de votre serveur ftp \n"));
						int choix = Integer.parseInt(in.readLine());
						switch (choix) {
						case 1:
							try {
								String classeName = in.readLine();
								Class<? extends Service> c = (Class<? extends Service>) urlcl.loadClass(classeName);
								ServiceRegistry.addService(c);
							} catch (Exception e) {
								System.out.println(e);
							}

						case 2:

						}

					}
				}
			}
		}

		catch (Exception e) {
			// Fin du service
		}

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
