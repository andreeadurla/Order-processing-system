package Model;

/**
* Clasa OrderClient contine atributele care descriu o comanda: idOrder, idClient, clientName, totalPrice, precum si metodele
* get si set corespunzatoare. De asemenea in aceasta clasa este suprascrisa metoda toString pentru a afisa comanda sub forma dorita.
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class OrderClient {
	private int idOrder;
	private int idClient;
	private String clientName;
	private double totalPrice;
	
	public OrderClient() {}
	
	public OrderClient(int idClient, String clientName, double totalPrice) {
		this.idClient = idClient;
		this.clientName = clientName;
		this.totalPrice = totalPrice;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public int getIdClient() {
		return idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public String toString() {
		return "ID_Order = " + idOrder + ", ID_Client = " + idClient + ", " + clientName + ", Total price = " + totalPrice;
	}
}