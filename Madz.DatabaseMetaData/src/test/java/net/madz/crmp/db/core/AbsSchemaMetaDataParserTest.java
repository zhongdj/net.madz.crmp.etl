package net.madz.crmp.db.core;

import net.madz.crmp.db.metadata.SchemaMetaData;

import org.junit.Test;

public abstract class AbsSchemaMetaDataParserTest {

    protected abstract AbsSchemaMetaDataParser create();

    @Test
    public void testParseSchemaMetaData() {
        final AbsSchemaMetaDataParser parser = create();
        final SchemaMetaData schemaMetaData = parser.parseSchemaMetaData();
    }
}
