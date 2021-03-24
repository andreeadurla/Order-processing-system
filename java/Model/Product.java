package Model;
/**
* Clasa Product contine atributele care descriu un produs: idProduct, name, quantity, price, precum si metodele get si set corespunzatoare.
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class Product {
	private int idProduct;
	private String name;
	private int quantity;
	private double price;
	
	public Product() {}
	
	public Product(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}
	
	public Product(String name, int quantity, double price) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}

	public int getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
