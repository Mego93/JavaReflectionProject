/**
 * Service inversion, sans fermeture du socket
 * @author VO Thierry & RISI Lucas
 * @version 1.5
 */

package rosi;

import java.io.*;
import java.net.*;

import bri.Service;
import codage.Decodage;

// rien à ajouter ici
public class ServiceInversion implements Service {
	
	private final Socket client;
	
	public ServiceInversion(Socket socket) {
		client = socket;
	}
@Override
	public void run() {
		try {BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
			PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);

			out.println("Service de ROSI : Tapez le texte à inverser");
		
			String line = in.readLine();		
	
			String invLine = new String (new StringBuffer(line).reverse());
			
			out.println(Decodage.encoder("Texte inversé : " + invLine+"\nTapez un caractère pour quitter"));
			in.readLine();
			
		}
		catch (IOException e) {
			//Fin du service d'inversion
		}
		
	}
	
	protected void finalize() throws Throwable {
		 client.close(); 
	}

	public static String toStringue() {
		return "Inversion de texte";
	}
}
