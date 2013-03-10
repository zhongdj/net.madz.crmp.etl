package net.madz.db.core;

import static org.junit.Assert.fail;

import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import net.madz.db.configuration.Database;
import net.madz.db.core.impl.DatabaseSchemaUtilsImpl;
import net.madz.db.core.impl.DbConfigurationManagement;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseSchemaUtilsImplTest {

    @Test
    public void testDatabaseExists() {
        fail("Not yet implemented");
    }

    @Test
    public void testCompareDatabaseSchema() {
        DatabaseSchemaUtilsImpl dbSchemaUtils = new DatabaseSchemaUtilsImpl();
        try {
            Assert.assertTrue(dbSchemaUtils.compareDatabaseSchema("fortest", "fortest"));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCloneDatabaseSchema() {
        DatabaseSchemaUtilsImpl dbSchemaUtils = new DatabaseSchemaUtilsImpl();
        try {
            String dbName = dbSchemaUtils.cloneDatabaseSchema("fortest", "fortest");
            Assert.assertEquals("fortest", dbName);
            final Database testDb = DbConfigurationManagement.findDatabaseInformation("fortest", true);
            Assert.assertEquals("MySQL", testDb.getSku().value());
            Assert.assertEquals("jdbc:mysql://localhost:3307/fortest", testDb.getUrl());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } catch (IllegalOperationException e) {
            Assert.fail(e.getMessage());
        } catch (JAXBException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testDropDatabase() {
        fail("Not yet implemented");
    }
}
