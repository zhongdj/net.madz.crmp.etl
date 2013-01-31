package net.madz.db.core.meta.immutable.jdbc.impl;

import java.util.Collection;

import net.madz.db.core.meta.DottedPath;
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

    @Override
    public DottedPath getSchemaPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<JdbcTableMetaData> getTables() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JdbcTableMetaData getTable(String name) {
        // TODO Auto-generated method stub
        return null;
    }
}
