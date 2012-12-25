package net.madz.crmp.db.metadata.business.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.madz.crmp.db.metadata.business.DBSchemaConstructor;
import net.madz.crmp.db.metadata.business.DBSchemaResolver;
import net.madz.crmp.db.metadata.business.DbOperatorFactory;
import net.madz.crmp.db.metadata.configuration.Database;
import net.madz.crmp.db.metadata.configuration.Databases;
import net.madz.crmp.db.metadata.configuration.Sku;
import net.madz.crmp.db.metadata.configuration.SkuConf;

public class DbOperatorFactoryImpl implements DbOperatorFactory {
	private static final String NET_MADZ_DB_CONFIGURATION = "net.madz.db.configuration";
	private static final String CONFIGURATION_DATA_SOURCES = "/Databases.xml";
	private static final Map<String, Database> dataSourceCache = new HashMap<String, Database>();
	private static final Map<Sku, SkuConf> skuConfs = new HashMap<Sku, SkuConf>();
	private static Databases databases;

	static {
		InputStream resource = null;
		try {
			final JAXBContext context = JAXBContext
					.newInstance(Databases.class);
			final Unmarshaller shaller = context.createUnmarshaller();
			final String configurationFile = System.getProperty(
					NET_MADZ_DB_CONFIGURATION, CONFIGURATION_DATA_SOURCES);
			resource = DbOperatorFactoryImpl.class
					.getResourceAsStream(configurationFile);
			databases = (Databases) shaller.unmarshal(resource);
			final List<Database> result = databases.getDatabase();
			final List<SkuConf> skuResult = databases.getSkuConf();
			for (Database item : result) {
				dataSourceCache.put(item.getName(), item);
			}
			for (SkuConf item : skuResult) {
				skuConfs.put(item.getSku(), item);
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

	private Connection createConnection(String dbName) {
		Database datasource = dataSourceCache.get(dbName);
		Connection connection = null;
		try {
			Class.forName(skuConfs.get(datasource.getSku()).getDriverClass());
			connection = DriverManager.getConnection(datasource.getUrl(),
					datasource.getUser(), datasource.getPassword());
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
		return connection;
	}

	@Override
	public DBSchemaResolver createDBSchemaResolver(String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBSchemaConstructor createDBSchemaConstructor(String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

}
