package blop;

import java.io.*;
import java.net.*;

import bri.Service;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// rien à ajouter ici
public class ServiceXML implements Service {

	private final Socket client;

	public ServiceXML(Socket socket) {
		client = socket;
	}

	@Override
	public void run() {
		do {
			try {

				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				String docXMLString = "";
				final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				final DocumentBuilder builder = factory.newDocumentBuilder();
				out.println("Donnez le chemin du fichier XML dans le serveur ftp: ");
				String pathname = in.readLine();
				final Document document = builder.parse(new File(pathname));
				final Element racine = document.getDocumentElement();
				final NodeList racineNoeuds = racine.getChildNodes();
				final int nbRacineNoeuds = racineNoeuds.getLength();

				for (int i = 0; i < nbRacineNoeuds; i++) {
					if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
						docXMLString += "\n Balise : " + racineNoeuds.item(i).getNodeName() + ", attribut : " + racineNoeuds.item(i).getAttributes();
					}
				}
				String line = in.readLine();

				String invLine = new String(new StringBuffer(line).reverse());

				out.println(invLine);

				out.println("Voulez vous arrêter ? ('O')");
				String repArret = in.readLine();
				if (repArret.equals("O"))
					break;
			} catch (Exception e) {
				// Fin du service
			}
		} while (true);
	}

	protected void finalize() throws Throwable {
		client.close();
	}

	public static String toStringue() {
		return "Inversion de texte";
	}
}
