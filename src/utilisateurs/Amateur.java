/**
 * Classe d'amateur
 * @author VO Thierry & RISI Lucas
 * @version 2.0
 */

package utilisateurs;

import bri.Utilisateur;

public class Amateur implements Utilisateur {
	private String login;
	private String password;
	
	public Amateur(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isProgrammer() {
		return false;
	}

	@Override
	public void setAdresseFtp(String adresseFtp) {
		return;
		
	}

	@Override
	public String getAdresseFtp() {
		return null;
	}
	
}
