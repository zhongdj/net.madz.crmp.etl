package net.madz.crmp.db.core;

import net.madz.crmp.db.metadata.SchemaMetaData;

import org.junit.Test;

public abstract class AbsDatabaseGeneratorTest {

    protected abstract AbsDatabaseGenerator create();

    @Test
    public void testGenerateDatabase() {
        final AbsDatabaseGenerator generator = create();
        SchemaMetaData metadata = null;
        generator.generateDatabase(metadata,"");
    }
}
