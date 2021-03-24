package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* Clasa ConnectionFactory realizeaza conexiunea la baza de date 
* 
* @author https://bitbucket.org/utcn_dsrl/pt-reflection-example/src/master/src/main/java/connection/ConnectionFactory.java
*/
public class ConnectionFactory {

	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DBURL = "jdbc:mysql://localhost:3306/warehouse";
	private static final String USER = "root";
	private static final String PASS = "root";

	private static ConnectionFactory instance = new ConnectionFactory();

	private ConnectionFactory() {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Creeaza o conexiune la baza de date.</p>
	 * @return conexiunea realizata
	 */
	private Connection createConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DBURL, USER, PASS);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * <p>Obtine conexiunea la baza de date.</p>
	 * @return conexiunea obtinuta
	 */
	public static Connection getConnection() {
		return instance.createConnection();
	}

	/**
	 * <p>Inchide o conexiune la baza de date.</p>
	 * @param connection conexiunea care trebuie inchisa
	 */
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
			}
		}
	}

	/**
	 * <p>Inchide un Statement.</p>
	 * @param statement Statement-ul care trebuie inchis
	 */
	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
			}
		}
	}

	/**
	 * <p>Inchide un ResultSet.</p>
	 * @param resultSet ResultSet-ul care trebuie inchis
	 */
	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
			}
		}
	}
}