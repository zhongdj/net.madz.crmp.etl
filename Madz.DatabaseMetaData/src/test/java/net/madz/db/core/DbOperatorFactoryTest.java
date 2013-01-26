package net.madz.db.core;

import static org.junit.Assert.fail;

import org.junit.Test;

public abstract class DbOperatorFactoryTest {

    protected abstract DbOperatorFactory create();

    @Test
    public void testCreateSchemaParser() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateDatabaseGenerator() {
        fail("Not yet implemented");
    }
}
