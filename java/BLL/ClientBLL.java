package BLL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAO.ClientDAO;
import Model.Client;

/**
* Clasa ClientBLL implementeaza logica aplicatiei corespunzatoare lui Client.
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class ClientBLL {
	
	private ClientDAO clientDAO;
	
	public ClientBLL() {
		clientDAO = new ClientDAO();
	}
	
	/**
	 * <p>Realizeaza inserarea unui client in baza de date prin apelarea metodei corespunzatoare din ClientDAO.</p>
	 * @param client clientul care va fi inserat
	 * @return id-ul clientului inserat
	 */
	public int insertClient(Client client) {
		int insertedId = clientDAO.insert(client);
		return insertedId;
	}
	
	/**
	 * <p>Realizeaza stergerea clientului cu numele si adresa data din baza de date prin apelarea metodei corespunzatoare din ClientDAO.</p>
	 * @param clientName numele clientului
	 * @param clientAddress adresa clientului
	 */
	public void deleteClient(String clientName, String clientAddress) {
		Map<String, String> columnValueForCondition = new HashMap<String, String>();
		columnValueForCondition.put("name", clientName);
		columnValueForCondition.put("address", clientAddress);
		
		clientDAO.delete(columnValueForCondition);
	}
	
	/**
	 * <p>Realizeaza selectia clientului cu numele dat din baza de date prin apelarea metodei corespunzatoare implementata in ClientDAO</p>
	 * @param clientName numele clientului care va fi cautat
	 * @return clientul gasit, sau null in caz contrar
	 */
	public Client findByName(String clientName) {
		Map<String, String> columnValueForCondition = new HashMap<String, String>();
		columnValueForCondition.put("name", clientName);
		
		List<Client> clients = clientDAO.<String>find(columnValueForCondition);
		if(clients != null)
			return clients.get(0);
		return null;
	}
	
	/**
	 * <p>Realizeaza selectia tuturor clientilor din baza de date prin apelarea metodei corespunzatoare din ClientDAO.</p>
	 * @return toti clientii din baza de date
	 */
	public List<Client> findAllClients() {
		List<Client> clients = clientDAO.<String>find(null);
		return clients;
	}	
}
