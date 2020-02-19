/**
 * Classe permettant le lancement d'un serveur pour programmeurs BRi
 * @author VO Thierry & RISI Lucas
 * @version 1.0
 */
package serveurs;

import java.io.*;
import java.net.*;
import services.*;

public class ServeurProgBRi implements Runnable {
	
	private ServerSocket listen_socket;
	
	/**
	 * Crée un serveur TCP 
	 * @throws IOException
	 */
	public ServeurProgBRi(int port) {
		try {
			listen_socket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Le serveur écoute et accepte les connexions.
	 * Chaque connexion, il crée le service de Programmeur BRi
	 * qui va la traiter
	 */
	public void run() {
		try {
			while(true)
				new ServiceProgBRi(listen_socket.accept()).start();
		}
		catch (IOException e) { 
			try {this.listen_socket.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'écoute :"+e);
		}
	}

	/**
	 * Restitution des ressources
	 */
	protected void finalize() throws Throwable {
		try {this.listen_socket.close();} catch (IOException e1) {}
	}


}
