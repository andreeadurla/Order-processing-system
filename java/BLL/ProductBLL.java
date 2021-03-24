package BLL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAO.ProductDAO;
import Model.Product;

/**
* Clasa ProductBLL implementeaza logica aplicatiei corespunzatoare lui Product.
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class ProductBLL {
	
	private ProductDAO productDAO;

	public ProductBLL() {
		productDAO = new ProductDAO();
	}

	/**
	 * <p>Realizeaza inserarea unui produs in baza de date prin apelarea metodei corespunzatoare din ProductDAO.</p>
	 * @param product produsul care va fi inserat
	 * @return id-ul produsului inserat
	 */
	public int insertProduct(Product product) {
		int insertedId = productDAO.insert(product);
		return insertedId;
	}

	/**
	 * <p>Realizeaza stergerea produsului care are un anumit nume din baza de date prin apelarea metodei corespunzatoare din ProductDAO.</p>
	 * @param productName numele produsului care va fi sters
	 */
	public void deleteByName(String productName) {
		Map<String, String> columnValueForCondition = new HashMap<String, String>();
		columnValueForCondition.put("name", productName);
		
		productDAO.delete(columnValueForCondition);
	}
	
	/**
	 * <p>Realizeaza actualizarea unui produs din baza de date prin apelarea metodei corespunzatoare din ProductDAO.</p>
	 * @param productName numele produsului care va fi actualizat
	 * @param addQuantity cantitatea care trebuie adaugata la cantitatea existenta
	 * @return numarul de produse actualizate
	 */
	public int updateCantity(String productName, int addQuantity) {
		Product product = findByName(productName);
		if(product == null)
			return 0;
		
		Map<String, Integer> columnValueForSet = new HashMap<String, Integer>();
		columnValueForSet.put("quantity", product.getQuantity() + addQuantity);
		
		Map<String, String> columnValueForCondition = new HashMap<String, String>();
		columnValueForCondition.put("name", productName);
		
		return productDAO.update(columnValueForSet, columnValueForCondition);
	}
	
	/**
	 * <p>Realizeaza selectia produsului cu numele dat din baza de date prin apelarea metodei corespunzatoare din ProductDAO.</p>
	 * @param productName numele produsului care trebuie cautat
	 * @return produsul gasit, sau null in caz contrar
	 */
	public Product findByName(String productName) {
		Map<String, String> columnValueForCondition = new HashMap<String, String>();
		columnValueForCondition.put("name", productName);
		
		List<Product> products = productDAO.find(columnValueForCondition);
		if(products != null)
			return products.get(0);
		return null;
	}
	
	/**
	 * <p>Realizeaza selectia tuturor produselor din baza de date prin apelarea metodei corespunzatoare din ProductDAO.</p>
	 * @return toate produsele din baza de date
	 */
	public List<Product> findAllProducts() {
		List<Product> products = productDAO.<String>find(null);
		return products;
	}
}