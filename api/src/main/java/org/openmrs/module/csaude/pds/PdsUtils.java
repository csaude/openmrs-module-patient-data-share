package org.openmrs.module.csaude.pds;

import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.SessionFactoryUtils;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains utility methods
 */
public class PdsUtils {
	
	private static final Logger log = LoggerFactory.getLogger(PdsUtils.class);
	
	/**
	 * Executes the specified query
	 * 
	 * @param query the query to execute
	 * @return results
	 * @throws SQLException
	 */
	public static List<List<Object>> executeQuery(String query) {
		List<List<Object>> results = new ArrayList();
		
		try (Connection conn = getDataSource().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			try (ResultSet resultSet = stmt.executeQuery()) {
				ResultSetMetaData rmd = resultSet.getMetaData();
				int columnCount = rmd.getColumnCount();
				
				while (resultSet.next()) {
					List<Object> rowObjects = new ArrayList<>();
					for (int x = 1; x <= columnCount; x++) {
						rowObjects.add(resultSet.getObject(x));
					}
					results.add(rowObjects);
				}
			}
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}
		
		return results;
	}
	
	/**
	 * Gets the DataSource object
	 * 
	 * @return javax.sql.DataSource object
	 */
	private static DataSource getDataSource() {
		return SessionFactoryUtils.getDataSource(Context.getRegisteredComponents(SessionFactory.class).get(0));
	}
	
	/**
	 * @see Paths#get(String, String...)
	 */
	public static Path createPath(String parent, String... additionalPaths) {
		return Paths.get(parent, additionalPaths);
	}
	
}
