/**
 * Classe de programmeur
 * @author VO Thierry & RISI Lucas
 * @version 2.0
 */

package utilisateurs;

import bri.Utilisateur;

public class Programmeur implements Utilisateur {
	private String login;
	private String password;
	private String adresseFtp;
	
	public Programmeur(String login, String password, String adresseFtp) {
		this.login = login;
		this.password = password;
		this.adresseFtp = adresseFtp;
	}
	
	public String getAdresseFtp() {
		return adresseFtp;
	}
	@Override
	public void setAdresseFtp(String adresseFtp) {
		this.adresseFtp = adresseFtp;
	}
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isProgrammer() {
		return true;
	}
	
}
