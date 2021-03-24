package Start;

import Presentation.Controller;

/**
* Clasa Start realizeaza pornirea aplicatiei
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/
public class Start {
	
	/**
	 * <p>Metoda main a aplicatiei prelucreaza argumentele si creeaza un obiect de tipul Controller</p>
	 * @param args argumentele cu care se executa programul
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Number of arguments is incorrect!");
		}
		else {
			try {
				new Controller(args[0]);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("This already exists! Please delete it and run again!");
			}
		}
	}
}
