/**
 * Classe permettant le lancement d'un serveur BRi
 * @author VO Thierry & RISI Lucas
 * @version 1.0
 */
package bri;

import java.io.*;
import java.net.*;


public class ServeurBRi implements Runnable {

	private ServerSocket listen_socket;
	private IServiceBRi serviceBRi;
	/**
	 * Crée un serveur TCP
	 * 
	 * @throws IOException
	 */
	public ServeurBRi(int port, IServiceBRi s) {
		try {
			listen_socket = new ServerSocket(port);
			this.serviceBRi = s;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Le serveur écoute et accepte les connexions.
	 */
	public void run() {
		try {
			while(true) {
			this.serviceBRi.setSocket(listen_socket.accept());
			new Thread(serviceBRi).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Restitution des ressources
	 */
	protected void finalize() throws Throwable {
		try {
			this.listen_socket.close();
		} catch (IOException e1) {
		}
	}

}
