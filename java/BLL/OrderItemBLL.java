package BLL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAO.OrderItemDAO;
import Model.OrderItem;

/**
* Clasa OrderItemBLL implementeaza logica aplicatiei corespunzatoare lui OrderItem.
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class OrderItemBLL {
	private OrderItemDAO orderItemDAO;

	public OrderItemBLL() {
		orderItemDAO = new OrderItemDAO();
	}
	
	/**
	 * <p>Realizeaza inserarea produsului aferent unei comenzi in baza de date prin apelarea metodei corespunzatoare din OrderItemDAO.</p>
	 * @param orderItem comanda care va fi inserata
	 * @return id-ul comenzii inserate
	 */
	public int insertOrderItem(OrderItem orderItem) {
		int insertedId = orderItemDAO.insert(orderItem);
		return insertedId;
	}
	
	/**
	 * <p>Realizeaza selectia produselor aferente unei comenzi cu idOrder-ul dat din baza de date prin apelarea metodei corespunzatoare din OrderItemDAO.</p>
	 * @param idOrder id-ul comenzii
	 * @return produsele aferente comenzii, sau null in caz contrar
	 */
	public List<OrderItem> findByIdOrder(int idOrder) {
		Map<String, Integer> columnValueForCondition = new HashMap<String, Integer>();
		columnValueForCondition.put("idOrder", idOrder);
		
		List<OrderItem> ordersItem = orderItemDAO.<Integer>find(columnValueForCondition);
		return ordersItem;
	}
	
	/**
	 * <p>Realizeaza selectia produselor aferente tuturor comenzilor din baza de date prin apelarea metodei corespunzatoare din OrderItemDAO.</p>
	 * @return produsele aferente tuturor comenzilor
	 */
	public List<OrderItem> findAllOrdersItem() {
		List<OrderItem> ordersItem = orderItemDAO.<String>find(null);
		return ordersItem;
	}
}
