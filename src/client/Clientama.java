/**
 * Classe d'application dédiée aux amateurs BRi
 * @author VO Thierry & RISI Lucas
 * @version 3.5
 */

package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import codage.Decodage;


public class Clientama {
		private final static int PORT_SERVICE = 3000;
		private final static String HOST = "localhost"; 
	
	public static void main(String[] args) {
		Socket s = null;		
		try {
			s = new Socket(HOST, PORT_SERVICE);

			BufferedReader sin = new BufferedReader (new InputStreamReader(s.getInputStream ( )));
			PrintWriter sout = new PrintWriter (s.getOutputStream ( ), true);
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));			
		
			System.out.println("Connecté au serveur pour amateurs BRi" );
			
			String line;
			do {
				// menu et choix du service
				line = sin.readLine();
				if (line == null)
					break; // fermeture par le service
				
				// On décode le message reçu (les #n décodés en \n)
				System.out.println(Decodage.decoder(line));
				System.out.print("=>");
				
				line = clavier.readLine();
				if (line.equals("EMERGENCY"))
					break; // fermeture par le client
				// saisie/envoie du choix
				sout.println(line);
				
			} while (true);
			s.close();
		} catch (IOException e) {
			System.err.println("Fin du service");
		}	
		// Refermer dans tous les cas la socket
		try {
			if (s != null)
				s.close();
		} catch (IOException e2) {}
	}
}