package bri;

public class Programmer {
	private String login;
	private String password;
	private String adresseFtp;
	
	public Programmer(String login, String password, String adresseFtp) {
		this.login = login;
		this.password = password;
		this.adresseFtp = adresseFtp;
	}
	
	public String getAdresseFtp() {
		return adresseFtp;
	}
	public void setAdresseFtp(String adresseFtp) {
		this.adresseFtp = adresseFtp;
	}
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
	
	
}
