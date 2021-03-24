package Model;

/**
* Clasa OrderItem contine atributele care descriu o parte a unei comenzi (fiecare produs aferent): idOrderItem, idOrder, idProduct, productName, quantity, price.
* Aceast clasa contine si metodele get si set corespunzatoare.
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class OrderItem {
	private int idOrderItem;
	private int idOrder;
	private int idProduct;
	private String productName;
	private int quantity;
	private double price;
	
	public OrderItem() {}
	
	public OrderItem(int idOrder, int idProduct, String productName, int quantity, double price) {
		this.idOrder = idOrder;
		this.idProduct = idProduct;
		this.productName = productName;
		this.quantity = quantity;
		this.price = price;
	}

	public int getIdOrderItem() {
		return idOrderItem;
	}

	public void setIdOrderItem(int idOrderItem) {
		this.idOrderItem = idOrderItem;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public int getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
