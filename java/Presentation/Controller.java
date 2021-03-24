package Presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import BLL.ClientBLL;
import BLL.OrderBLL;
import BLL.OrderItemBLL;
import BLL.ProductBLL;
import Model.Client;
import Model.OrderClient;
import Model.OrderItem;
import Model.Product;

/**
* Clasa Controller realizeaza parsarea unui fisier dat pentru a obtine comenzile care trebuie executate.
* De asemenea, aceasta clasa apeleaza metodele corespunzatoare efectuarii comenzilor dorite.
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class Controller {

	private ClientBLL clientBLL;
	private ProductBLL productBLL;
	private OrderBLL orderBLL;
	private OrderItemBLL orderItemBLL;
	private Validator validator;
	private Printer printer;

	/**
	 * <p>Creeaza o noua instanta File si apeleaza metoda de citire a datelor din acel fisier.</p>
	 * @param inputFile numele fisierului
	 * @throws Exception daca exista deja folder-ul in care se vor stoca fisierele pdf
	 */
	public Controller(String inputFile) throws Exception {
		clientBLL = new ClientBLL();
		productBLL = new ProductBLL();
		orderBLL = new OrderBLL();
		orderItemBLL = new OrderItemBLL();
		validator = new Validator();
		printer = new Printer();
		File file = new File(inputFile);
		readData(file);
	}

	/**
	 * <p>Citeste datele din fisier si le prelucreaza.</p>
	 * @param inputFile numele fisierului
	 */
	private void readData(File inputFile) {
		Scanner scanner;
		try {
			scanner = new Scanner(inputFile);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				processLine(line);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
		} 
	}

	/**
	 * <p>Parseaza String-ul pentru a obtine informatiile necesare si apeleaza o metoda care stabileste ce comanda se va executa.</p>
	 * @param line linia care trebuie procesata
	 */
	private void processLine(String line) {
		String[] arrOfStr = line.split(": ");
		String command = arrOfStr[0];
		String[] commandParameters = null;

		if(arrOfStr.length == 2)
			commandParameters = arrOfStr[1].split(", ");

		executeCommand(command, commandParameters);
	}
	
	/**
	 * <p>Executa o anumita comanda.</p>
	 * @param command numele comenzii
	 * @param commandParameters parametrii acelei comenzi
	 */
	private void executeCommand(String command, String[] commandParameters) {
		if(command.equalsIgnoreCase("Insert client") == true) {
			insertClient(commandParameters);
			return;
		}
		if(command.equalsIgnoreCase("Delete client") == true) {
			deleteClient(commandParameters);
			return ;
		}
		if(command.equalsIgnoreCase("Insert product") == true) {
			insertProduct(commandParameters);
			return ;
		}
		if(command.equalsIgnoreCase("Delete product") == true) {
			deleteProduct(commandParameters);
			return ;
		}
		if(command.equalsIgnoreCase("Order") == true) {
			order(commandParameters);
			return ;
		}
		if(command.equalsIgnoreCase("Report client") == true) {
			printer.createPdfClients();
			return ;
		}
		if(command.equalsIgnoreCase("Report product") == true) {
			printer.createPdfProducts();
			return ;
		}
		if(command.equalsIgnoreCase("Report order") == true) {
			printer.createPdfOrder();
			return ;
		}
	}
	
	/**
	 * <p>Creeaza un client pe care il va insera in baza de date.</p>
	 * @param commandParameters parametrii comenzii Insert Client
	 */
	private void insertClient(String[] commandParameters) {
		if(validator.validateInsertClient(commandParameters) == false) {
			System.out.println("Command \"Insert client\" has wrong parameters!");
			return ;
		}

		Client client = new Client(commandParameters[0], commandParameters[1]);
		if(clientBLL.findByName(commandParameters[0]) == null) 
			clientBLL.insertClient(client);
		else
			System.out.println("Client " + commandParameters[0] + " already exists!");
	}
	
	/**
	  * <p>Sterge un client din baza de date.</p>
	 * @param commandParameters parametrii comenzii Delete Client
	 */
	private void deleteClient(String[] commandParameters) {
		if(validator.validateDeleteClient(commandParameters) == false) {
			System.out.println("Command \"Delete client\" has wrong parameters!");
			return ;
		}

		clientBLL.deleteClient(commandParameters[0], commandParameters[1]);
		return ;
	}

	/**
	 * <p>Creeaza un produs pe care il va insera in baza de date. In cazul in care produsul deja exista 
	 * se va incrementa cantitatea acestuia.</p>
	 * @param commandParameters parametrii comenzii Insert Product
	 */
	private void insertProduct(String[] commandParameters) {
		if(validator.validateInsertProduct(commandParameters) == false) {
			System.out.println("Command \"Insert productt\" has wrong parameters!");
			return ;
		}

		int quantity = Integer.parseInt(commandParameters[1]);  
		double price = Double.parseDouble(commandParameters[2]);
		Product product = productBLL.findByName(commandParameters[0]);
		
		if(product != null && product.getPrice() != price) {
			System.out.println("Product " + product.getName() + " already exists and has price " + product.getPrice() + ". You cannot insert a product with the same name and different price.");
			return;
		}

		if(productBLL.updateCantity(commandParameters[0], quantity) == 0){
			product = new Product(commandParameters[0], quantity, price);
			productBLL.insertProduct(product);
		}
		return ;
	}

	/**
	 * <p>Sterge un produs din baza de date.</p>
	 * @param commandParameters parametrii comenzii Delete Product
	 */
	private void deleteProduct(String[] commandParameters) {
		if(validator.validateDeleteProduct(commandParameters) == false) {
			System.out.println("Command \"Delete product\" has wrong parameters!");
			return ;
		}

		productBLL.deleteByName(commandParameters[0]);
		return ;
	}
	
	/**
	 * <p>Creeaza o comanda a unui client pe care o va insera in baza de date.</p>
	 * @param commandParameters parametrii comenzii Order
	 */
	private void order(String[] commandParameters) {
		if(validator.validateOrder(commandParameters) == false) {
			System.out.println("Command \"Order\" has wrong parameters!");
			return ;
		}
		
		Client client = clientBLL.findByName(commandParameters[0]);
		if(client == null) {
			String message = "Client " + commandParameters[0] + " doesn't exist!";
			printer.createPdfOrderMessage(commandParameters[0], message);
			return ;
		}
		
		ArrayList<Product> products = new ArrayList<Product>();
		for(int i = 1; i < commandParameters.length; i += 2) {
			String productName = commandParameters[i];
			int quantity = Integer.parseInt(commandParameters[i+1]);
			boolean productFound = false;
			for(Product p: products) {
				if(p.getName().equalsIgnoreCase(productName) == true) {
					p.addQuantity(quantity);
					productFound = true;
					break;
				}
			}
			if(productFound == false)
				products.add(new Product(productName, quantity));
		}
		
		double orderPrice = processOrder(client, products);
		if(orderPrice == -1)
			return ;
		placeOrder(client, products, orderPrice);	
	}
	
	/**
	 * <p>Verifica daca produsele exista in baza de date si, totodata, daca exista stoc suficient.</p>
	 * @param client clientul care executa comanda
	 * @param products produsele dorite
	 * @return suma totala a comenzii in cazul in care produsele exista si stocul este suficient, sau -1, in caz contrar
	 */
	private double processOrder(Client client, ArrayList<Product> products) {
		double orderPrice = 0;
		for(Product p: products) {
			Product productDB = productBLL.findByName(p.getName());
			if(productDB == null) {
				printer.createPdfOrderMessage(client.getName(), "Product " + p.getName() + " doesn't exist!");
				return -1;
			}
			
			if(productDB.getQuantity() >= p.getQuantity()) {
				p.setIdProduct(productDB.getIdProduct());
				p.setPrice(productDB.getPrice());
				orderPrice += productDB.getPrice() * p.getQuantity();
			}
			else {
				String message = "Client " + client.getName() + " wants to buy " + p.getQuantity() + " " + p.getName() + ". There are not enough " + p.getName() + ". In stock there are only " + productDB.getQuantity() + ".";
				printer.createPdfOrderMessage(client.getName(), message);
				return -1;
			}
		}
		return orderPrice;
	}
	
	/**
	 * <p>Creeaza o comanda aferenta clientului care va contine produsele introduse si care are pretul total introdus.
	 * Se vor insera informatiile corespunzatoare in baza de date.</p>
	 * @param client clientul care efectueaza comanda
	 * @param products produsele dorite
	 * @param orderPrice pretul comenzii
	 */
	private void placeOrder(Client client, ArrayList<Product> products, double orderPrice) {
		
		OrderClient order = new OrderClient(client.getIdClient(), client.getName(), orderPrice);
		int idOrder = orderBLL.insertOrder(order);
		order.setIdOrder(idOrder);
		
		for(Product p: products) {
			productBLL.updateCantity(p.getName(), -p.getQuantity());
			OrderItem orderItem = new OrderItem(idOrder, p.getIdProduct(), p.getName(), p.getQuantity(), p.getQuantity()*p.getPrice());
			orderItemBLL.insertOrderItem(orderItem);
		}

		printer.createPdfOrderClient(order);
	}
}
