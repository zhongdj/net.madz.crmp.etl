package net.madz.db.core;

import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBException;

import net.madz.db.configuration.Database;
import net.madz.db.core.impl.DatabaseSchemaUtilsImpl;
import net.madz.db.core.impl.DbConfigurationManagement;
import net.madz.db.core.impl.validation.mysql.ErrorEntry;
import net.madz.db.utils.LogUtils;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class DatabaseSchemaUtilsImplTest {

    @Test
    @Ignore
    public void testDatabaseExists() {
        fail("Not yet implemented");
    }

    @Test
    public void testCloneDatabaseSchema() {
        DatabaseSchemaUtilsImpl dbSchemaUtils = new DatabaseSchemaUtilsImpl();
        try {
            String dbName = dbSchemaUtils.cloneDatabaseSchema("fortest", "fortest");
            Assert.assertEquals("fortest", dbName);
            final Database testDb = DbConfigurationManagement.findDatabaseInformation("fortest", true);
            Assert.assertEquals("MySQL", testDb.getSku().value());
            Assert.assertEquals("jdbc:mysql://dbserver:3307/fortest", testDb.getUrl());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } catch (IllegalOperationException e) {
            Assert.fail(e.getMessage());
        } catch (JAXBException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCompareDatabaseSchema() {
        DatabaseSchemaUtilsImpl dbSchemaUtils = new DatabaseSchemaUtilsImpl();
        List<ErrorEntry> result;
        try {
            result = dbSchemaUtils.compareDatabaseSchema("fortest", "fortest");
            for ( ErrorEntry entry : result ) {
                System.out.println(entry.toString());
            }
            if ( result.size() > 0 ) {
                fail("The schema metadata does not match.");
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testDropDatabase() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testMigrateDatabaseSchema() {
        DatabaseSchemaUtilsImpl dbSchemaUtils = new DatabaseSchemaUtilsImpl();
        try {
            dbSchemaUtils.migrateDatabaseSchema("Search", "Search");
        } catch (Exception e) {
            LogUtils.debug(e.getClass(),e.getMessage());
            fail(e.getMessage());
        } 
    }
}
