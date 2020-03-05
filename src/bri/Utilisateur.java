package bri;

public interface Utilisateur {
	
	boolean isProgrammer();

	String getLogin();
	
	String getPassword();
	
	void setAdresseFtp(String adresseFtp);
	
	String getAdresseFtp();
}
