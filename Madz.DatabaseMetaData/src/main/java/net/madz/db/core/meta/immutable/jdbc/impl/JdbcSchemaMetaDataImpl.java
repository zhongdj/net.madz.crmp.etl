package net.madz.db.core.meta.immutable.jdbc.impl;

import java.util.List;

import net.madz.db.core.meta.immutable.impl.SchemaMetaDataImpl;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;

public final class JdbcSchemaMetaDataImpl extends
        SchemaMetaDataImpl<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> implements JdbcSchemaMetaData {

    public JdbcSchemaMetaDataImpl(JdbcSchemaMetaData metaData) {
        super(metaData);
    }

    public void addAllTables(List<JdbcTableMetaData> tables) {
        for ( JdbcTableMetaData table : tables ) {
            appendTable(table);
        }
    }
}
