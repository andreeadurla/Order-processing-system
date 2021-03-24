package DAO;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import Connection.ConnectionFactory;

/**
* Clasa AbstractDAO defineste principalele operatii de manipulare a bazei de date: SELECT, INSERT, UPDATE, DELETE 
* 
* @author Andreea Durla
* @since 09 Aprilie 2020
*/

public class AbstractDAO<T> {
	private static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
	private final Class<T> type;

	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * <p> Creeaza un Select query folosind numele clasei tipului generic T si un set de perechi (key, value).</p>
	 * @param <E> descrie tipul parametrului
	 * @param columnValueForCondition un set de perechi(key, value), unde "key" reprezinta numele coloanei unui tabel, 
	 * iar "value" reprezinta valoarea asociata coloanei respective. In functie de perechile (key, value) se va face selectia
	 * @return Select query-ul corespunzator
	 */
	private <E> String createSelectQuery(Map<String, E> columnValueForCondition) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * ");
		sb.append("FROM " + type.getSimpleName());

		if(columnValueForCondition != null) {
			sb.append(" WHERE ");

			for(Map.Entry<String, E> entry : columnValueForCondition.entrySet()) {
				sb.append(entry.getKey() + "=" + "\"" + String.valueOf(entry.getValue()) + "\"");
				sb.append(" AND ");
			}
			sb = new StringBuilder(sb.subSequence(0, sb.length() - 5));
		}
		return sb.toString();
	}
	
	/**
	 * <p> Metoda este utilizata pentru a obtine, din tabelul asociat clasei tipului generic T, date in functie de criteriile introduse.</p>
	 * @param <E> descrie tipul parametrului
	 * @param columnValueForCondition un set de perechi (key, value), unde "key" reprezinta numele coloanei unui tabel, 
	 * iar "value" reprezinta valoarea asociata coloanei respective. In functie de perechile (key, value) se va face selectia
	 * @return datele din tabel corespunzatoare criteriilor introduse, sau null in caz contrar 
	 */
	public <E> List<T> find(Map<String, E> columnValueForCondition) {
		Connection dbConnection = null;
		PreparedStatement findStatement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery(columnValueForCondition);

		try {
			dbConnection = ConnectionFactory.getConnection();
			findStatement = dbConnection.prepareStatement(query);
			resultSet = findStatement.executeQuery();

			List<T> list = createObjects(resultSet);
			if(list.isEmpty() == false)
				return list;
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:find " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(findStatement);
			ConnectionFactory.close(dbConnection);
		}
		return null;
	}

	/**
	 * <p> Metoda este utilizata pentru a crea o lista de obiecte de tipul T corespunzatoare unui ResultSet</p>
	 * @param resultSet rezultatul obtinut prin executarea unui query
	 * @return lista de obiecte de tipul T 
	 */
	private List<T> createObjects(ResultSet resultSet) {
		List<T> list = new ArrayList<T>();

		try {
			while (resultSet.next()) {
				T instance = type.getDeclaredConstructor().newInstance();
				for (Field field : type.getDeclaredFields()) {
					Object value = resultSet.getObject(field.getName());
					if(value == null)
						value = 0;
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getWriteMethod();
					method.invoke(instance, value);
				}
				list.add(instance);
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:createObjects " + e.getMessage());
		} 
		return list;
	}

	/**
	 * <p> Creeaza un Insert query folosind numele clasei tipului generic T si atributele acesteia</p>
	 * @param fields atributele clasei T
	 * @return Insert query-ul corespunzator
	 */
	private String createInsertQuery(Field[] fields) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(type.getSimpleName() + " (");
		for(int i = 1; i < fields.length; i++)
			sb.append(fields[i].getName() + ",");

		sb = new StringBuilder(sb.subSequence(0, sb.length() - 1));
		sb.append(") ");
		sb.append("VALUES (");

		for(int i = 1; i < fields.length; i++)
			sb.append("?,");

		sb = new StringBuilder(sb.subSequence(0, sb.length() - 1));
		sb.append(") ");
		return sb.toString();
	}
	
	/**
	 * <p>Metoda este utilizata pentru a insera, in tabelul asociat clasei tipului generic T, un obiect de tipul T.</p>
	 * @param t un obiect de tipul generic T
	 * @return id-ul unic corespunzator obiectului inserat daca operatia s-a realizat cu succes, sau 0 in caz contrar
	 */
	public int insert(T t) {
		Connection dbConnection = null;
		PreparedStatement insertStatement = null;
		ResultSet resultSet = null;
		Field[] fields = t.getClass().getDeclaredFields();
		String query = createInsertQuery(fields);
		int insertedId = 0;

		try {
			dbConnection = ConnectionFactory.getConnection();
			insertStatement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			for(int i = 1; i < fields.length; i++) {
				fields[i].setAccessible(true); 
				Object value = fields[i].get(t);

				if(fields[i].getType() == int.class) 
					insertStatement.setInt(i, ((Integer) value).intValue());
				else if(fields[i].getType() == double.class)
					insertStatement.setDouble(i, ((Double) value).doubleValue());
				else if(fields[i].getType() == String.class)
					insertStatement.setString(i, (String) value);
			}
			insertStatement.executeUpdate();
			resultSet = insertStatement.getGeneratedKeys();
			if (resultSet.next())
				insertedId = resultSet.getInt(1);
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(insertStatement);
			ConnectionFactory.close(dbConnection);
		}
		return insertedId;
	}
	
	/**
	 * <p>Creeaza un Delete query folosind numele clasei tipului generic T si un set de perechi (key, value).</p>
	 * @param <E> descrie tipul parametrului
	 * @param columnValueForCondition un set de perechi (key, value), unde "key" reprezinta numele coloanei unui tabel, 
	 * iar "value" reprezinta valoarea asociata coloanei respective. In functie perechile (key, value) se va face stergerea
	 * @return Delete query-ul corespunzator
	 */
	private <E> String createDeleteQuery(Map<String, E> columnValueForCondition) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(type.getSimpleName());

		if(columnValueForCondition != null) {
			sb.append(" WHERE ");

			for(Map.Entry<String, E> entry : columnValueForCondition.entrySet()) {
				sb.append(entry.getKey() + "=" + "\"" + entry.getValue() + "\"");
				sb.append(" AND ");
			}
			sb = new StringBuilder(sb.subSequence(0, sb.length() - 5));
		}
		return sb.toString();
	}

	/**
	 * <p> Metoda este utilizata pentru a sterge datele, din tabelul asociat clasei tipului generic T, in functie de criteriile introduse.</p>
	 * @param <E> descrie tipul parametrului
	 * @param columnValueForCondition un set de perechi (key, value), unde "key" reprezinta numele coloanei unui tabel, 
	 * iar "value" reprezinta valoarea asociata coloanei respective. In functie perechile (key, value) se va face stergerea
	 * @return numarul de randuri sterse in cazul in care operatia s-a realizat cu succes, sau 0 in caz contrar
	 */
	public <E> int delete(Map<String, E> columnValueForCondition) {
		int toReturn = 0;
		Connection dbConnection = null;
		PreparedStatement deleteStatement = null;
		String query = createDeleteQuery(columnValueForCondition);

		try {
			dbConnection = ConnectionFactory.getConnection();
			deleteStatement = dbConnection.prepareStatement(query);
			toReturn = deleteStatement.executeUpdate();
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
		} finally {
			ConnectionFactory.close(deleteStatement);
			ConnectionFactory.close(dbConnection);
		}
		return toReturn;
	}
	
	/**
	 * <p> Creeaza un Update query folosind numele clasei tipului generic T si doua seturi de perechi (key, value).</p>
	 * @param <E> descrie tipul parametrului
	 * @param <F> descrie tipul parametrului
	 * @param columnValueForSet un set de perechi (key, value), unde "key" reprezinta numele coloanei unui tabel, 
	 * iar "value" reprezinta valoarea asociata coloanei respective. Se va actualiza campul "key" cu valoarea "value"
	 * @param columnValueForCondition un set de perechi (key, value), unde "key" reprezinta numele coloanei unui tabel, 
	 * iar "value" reprezinta valoarea asociata coloanei respective. In functie de perechile (key, value) se va face actualizarea
	 * @return Update query-ul corespunzator
	 */
	private <E, F> String createUpdateQuery(Map<String, E> columnValueForSet, Map<String, F> columnValueForCondition) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(type.getSimpleName() + " ");
		sb.append("SET ");

		for(Map.Entry<String, E> entry : columnValueForSet.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append(", ");
		}
		sb = new StringBuilder(sb.subSequence(0, sb.length() - 2));
		
		if(columnValueForCondition != null) {
			sb.append(" WHERE ");
			for(Map.Entry<String, F> entry : columnValueForCondition.entrySet()) {
				sb.append(entry.getKey() + "=" + "\"" + String.valueOf(entry.getValue()) + "\"");
				sb.append(" AND ");
			}
			sb = new StringBuilder(sb.subSequence(0, sb.length() - 5));
		}
		return sb.toString();
	}

	/**
	 * <p> Metoda este utilizata pentru a actualiza datele, din tabelul asociat clasei tipului generic T, in functie de criteriile introduse.</p>
	 * @param <E> descrie tipul parametrului
	 * @param <F> descrie tipul parametrului
	 * @param columnValueForSet un set de perechi (key, value), unde "key" reprezinta numele coloanei unui tabel, 
	 * iar "value" reprezinta valoarea asociata coloanei respective. Se va actualiza campul "key" cu valoarea "value"
	 * @param columnValueForCondition un set de perechi (key, value), unde "key" reprezinta numele coloanei unui tabel, 
	 * iar "value" reprezinta valoarea asociata coloanei respective. In functie de perechile (key, value) se va face actualizarea
	 * @return numarul de randuri actualizate in cazul in care operatia s-a efectuat cu succes, sau 0 in caz contrar
	 */
	public <E, F> int update(Map<String, E> columnValueForSet, Map<String, F> columnValueForCondition) {
		if(columnValueForSet == null)
			return 0;

		int toReturn = 0;
		Connection dbConnection = null;
		PreparedStatement updateStatement = null;
		String query = createUpdateQuery(columnValueForSet, columnValueForCondition);

		try {
			dbConnection = ConnectionFactory.getConnection();
			updateStatement = dbConnection.prepareStatement(query);
			toReturn = updateStatement.executeUpdate();
		} catch(Exception e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
		} finally {
			ConnectionFactory.close(updateStatement);
			ConnectionFactory.close(dbConnection);
		}
		return toReturn;
	}
}
