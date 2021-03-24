package BLL;

import java.util.List;

import DAO.OrderDAO;
import Model.OrderClient;

/**
* Clasa OrderBLL implementeaza logica aplicatiei corespunzatoare lui OrderClient.
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class OrderBLL {
	private OrderDAO orderDAO;

	public OrderBLL() {
		orderDAO = new OrderDAO();
	}
	
	/**
	 * <p>Realizeaza inserarea unei comenzi in baza de date prin apelarea metodei corespunzatoare din OrderDAO</p>
	 * @param order comanda care va fi inserata
	 * @return id-ul comenzii inserate
	 */
	public int insertOrder(OrderClient order) {
		int insertedId = orderDAO.insert(order);
		return insertedId;
	}
	
	/**
	 * <p>Realizeaza selectia tuturor comenzilor din baza de date prin apelarea metodei corespunzatoare din OrderDAO</p>
	 * @return toate comenzile din baza de date
	 */
	public List<OrderClient> findAllOrders() {
		List<OrderClient> orders = orderDAO.<Integer>find(null);
		return orders;
	}
}
