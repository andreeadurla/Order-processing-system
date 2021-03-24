package Presentation;

import java.util.regex.Pattern;

/**
 * Clasa Validator implementeaza mai multe metode de validare a parametrilor unor comenzi
 * 
 * @author Andreea Durla
 * @since 09 Aprilie 2020
 */
public class Validator {

	/**
	 * <p> Verifica daca un String reprezinta un numar intreg</p>
	 * @param s String-ul care trebuie verificat
	 * @return true daca s este un numar intreg, sau false in caz contrar
	 */
	private boolean isInt(String s) {
		Pattern pattern = Pattern.compile("-?\\d+");
		if (s == null) {
			return false; 
		}
		return pattern.matcher(s).matches();
	}

	/**
	 * <p> Verifica daca un String reprezinta un numar real</p>
	 * @param s String-ul care trebuie verificat
	 * @return true daca s este un numar real, sau false in caz contrar
	 */
	private boolean isDouble(String s) {
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		if (s == null) {
			return false; 
		}
		return pattern.matcher(s).matches();
	}

	/**
	 * <p> Verifica daca un sir de String-uri reprezinta parametrii valizi ai comenzii Insert client.</p>
	 * @param parameters parametrii comenzii Insert client
	 * @return true in cazul in care parametrii sunt valizi, sau false in caz contrar
	 */
	public boolean validateInsertClient(String[] parameters) {
		if(parameters.length != 2)
			return false;

		for(String p: parameters)
			if(p.getClass() != String.class)
				return false;

		return true;
	}

	/**
	 * <p> Verifica daca un sir de String-uri reprezinta parametrii valizi ai comenzii Delete client.</p>
	 * @param parameters parametrii comenzii Delete client
	 * @return true in cazul in care parametrii sunt valizi, sau false in caz contrar
	 */
	public boolean validateDeleteClient(String[] parameters) {
		if(parameters.length != 2)
			return false;

		for(String p: parameters)
			if(p.getClass() != String.class)
				return false;

		return true;
	}

	/**
	 * <p> Verifica daca un sir de String-uri reprezinta parametrii valizi ai comenzii Insert product.</p>
	 * @param parameters parametrii comenzii Insert product
	 * @return true in cazul in care parametrii sunt valizi, sau false in caz contrar
	 */
	public boolean validateInsertProduct(String[] parameters) {
		if(parameters.length != 3)
			return false;

		if(parameters[0].getClass() != String.class)
			return false;

		if(isInt(parameters[1]) == false)
			return false;
		else {
			int x = Integer.parseInt(parameters[1]);
			if(x <= 0)
				return false;
		}
		
		if(isDouble(parameters[2]) == false)
			return false;
		else {
			double x = Double.parseDouble(parameters[2]);
			if(x <= 0)
				return false;
		}

		return true;
	}

	/**
	 * <p> Verifica daca un sir de String-uri reprezinta parametrii valizi ai comenzii Delete product</p>
	 * @param parameters parametrii comenzii Delete product
	 * @return true in cazul in care parametrii sunt valizi, sau false in caz contrar
	 */
	public boolean validateDeleteProduct(String[] parameters) {
		if(parameters.length != 1)
			return false;

		if(parameters[0].getClass() != String.class)
			return false;

		return true;
	}

	/**
	 * <p> Verifica daca un sir de String-uri reprezinta parametrii valizi ai comenzii Order.</p>
	 * @param parameters parametrii comenzii Order
	 * @return true in cazul in care parametrii sunt valizi, sau false in caz contrar
	 */
	public boolean validateOrder(String[] parameters) {
		if(parameters.length < 3 || (parameters.length-1) % 2 != 0)
			return false;

		if(parameters[0].getClass() != String.class)
			return false;

		for(int i = 1; i < parameters.length; i += 2) {
			if(parameters[i].getClass() != String.class)
				return false;

			if(isInt(parameters[i+1]) == false)
				return false;
			else {
				int x = Integer.parseInt(parameters[i+1]);
				if(x <= 0)
					return false;
			}
		}
		return true;
	}
}
