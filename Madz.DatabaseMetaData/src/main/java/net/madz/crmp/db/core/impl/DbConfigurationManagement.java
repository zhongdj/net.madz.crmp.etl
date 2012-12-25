package net.madz.crmp.db.core.impl;

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

import net.madz.crmp.db.metadata.configuration.Database;
import net.madz.crmp.db.metadata.configuration.DatabaseConfig;
import net.madz.crmp.db.metadata.configuration.DatabaseCopiesServer;
import net.madz.crmp.db.metadata.configuration.Sku;
import net.madz.crmp.db.metadata.configuration.SkuConf;

public class DbConfigurationManagement {

    private static final String NET_MADZ_DB_CONFIGURATION = "net.madz.db.configuration";
    private static final String CONFIGURATION_DATA_SOURCES = "/Databases.xml";
    private static final Map<String, Database> sourceDatabaseCache = new HashMap<String, Database>();
    private static final Map<String, Database> databaseCopiesCache = new HashMap<String, Database>();
    private static final Map<Sku, SkuConf> skuConfs = new HashMap<Sku, SkuConf>();
    private static DatabaseCopiesServer databaseCopiesServer = null;
    private static DatabaseConfig databaseconfig;
    static {
        loadDatabaseConfiguration();
    }

    private static void loadDatabaseConfiguration() {
        InputStream resource = null;
        try {
            final JAXBContext context = JAXBContext.newInstance(DatabaseConfig.class);
            final Unmarshaller shaller = context.createUnmarshaller();
            final String configurationFile = System.getProperty(DbConfigurationManagement.NET_MADZ_DB_CONFIGURATION, CONFIGURATION_DATA_SOURCES);
            resource = DbConfigurationManagement.class.getResourceAsStream(configurationFile);
            databaseconfig = (DatabaseConfig) shaller.unmarshal(resource);
            final List<Database> sourceDatabases = databaseconfig.getSourceDatabases().getDatabase();
            final List<Database> databaseCopies = databaseconfig.getDatabaseCopies().getDatabase();
            final List<SkuConf> skuResult = databaseconfig.getSkuConf();
            databaseCopiesServer = databaseconfig.getDatabaseCopiesServer();
            for ( Database item : sourceDatabases ) {
                sourceDatabaseCache.put(item.getName(), item);
            }
            for ( Database item : databaseCopies ) {
                databaseCopiesCache.put(item.getName(), item);
            }
            for ( SkuConf item : skuResult ) {
                skuConfs.put(item.getSku(), item);
            }
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        } finally {
            if ( null != resource ) {
                try {
                    resource.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static Connection createConnection(String dbName) {
        Database datasource = sourceDatabaseCache.get(dbName);
        Connection connection = null;
        try {
            Class.forName(skuConfs.get(datasource.getSku()).getDriverClass());
            connection = DriverManager.getConnection(datasource.getUrl(), datasource.getUser(), datasource.getPassword());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return connection;
    }
}
