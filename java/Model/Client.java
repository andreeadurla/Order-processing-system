package Model;

/**
* Clasa Client contine atributele care descriu un client: idClient, name, address, precum si metodele get si set corespunzatoare. 
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class Client {
	private int idClient;
	private String name;
	private String address;
	
	public Client() {}
	
	public Client(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public int getIdClient() {
		return idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
