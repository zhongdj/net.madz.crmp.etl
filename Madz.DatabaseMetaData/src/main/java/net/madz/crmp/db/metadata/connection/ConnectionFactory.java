package net.madz.crmp.db.metadata.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.madz.crmp.db.metadata.configuration.Datasource;
import net.madz.crmp.db.metadata.configuration.Datasources;

public class ConnectionFactory {
	private static final String NET_MADZ_DB_CONFIGURATION = "net.madz.db.configuration";
	private static final String CONFIGURATION_DATA_SOURCES = "/DataSources.xml";
	private static final Map<String, Datasource> dataSourceCache = new HashMap<String, Datasource>();
	private static Datasources datasources;

	static {
		InputStream resource = null;
		try {
			final JAXBContext context = JAXBContext
					.newInstance(Datasources.class);
			final Unmarshaller shaller = context.createUnmarshaller();
			final String configurationFile = System.getProperty(
					NET_MADZ_DB_CONFIGURATION, CONFIGURATION_DATA_SOURCES);
			resource = ConnectionFactory.class
					.getResourceAsStream(configurationFile);
			datasources = (Datasources) shaller.unmarshal(resource);
			final List<Datasource> result = datasources.getDatasource();
			for (Datasource item : result) {
				dataSourceCache.put(item.getName(), item);
			}
		} catch (JAXBException e) {
			throw new IllegalStateException(e);
		} finally {
			if (null != resource) {
				try {
					resource.close();
				} catch (IOException ignored) {
				}
			}
		}

	}

	public static Connection createConnection(String dbName) {
		Datasource datasource = dataSourceCache.get(dbName);
		Connection connection = null;
		try {
			Class.forName(datasource.getDriver());
			connection = DriverManager.getConnection(datasource.getUrl(),
					datasource.getUser(), datasource.getPassword());
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
		return connection;
	}

	public static void main(String[] args) {
		Connection connection = ConnectionFactory.createConnection("T1");
		try {
			Statement st = connection.createStatement();
			st.execute("create table we(a int)");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
