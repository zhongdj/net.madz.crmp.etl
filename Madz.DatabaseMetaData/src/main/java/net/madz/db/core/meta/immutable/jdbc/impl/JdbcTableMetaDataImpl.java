package net.madz.db.core.meta.immutable.jdbc.impl;

import java.util.Collection;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.impl.TableMetaDataImpl;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.immutable.types.TableType;

public final class JdbcTableMetaDataImpl extends
        TableMetaDataImpl<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> implements JdbcTableMetaData {

    public JdbcTableMetaDataImpl(JdbcTableMetaData tableMetaData) {
        super(tableMetaData);
    }

    @Override
    public DottedPath getTablePath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TableType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemarks() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getIdCol() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getIdGeneration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JdbcIndexMetaData getPrimaryKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JdbcColumnMetaData getColumn(String columnName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<JdbcColumnMetaData> getColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<JdbcForeignKeyMetaData> getForeignKeySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<JdbcIndexMetaData> getIndexSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JdbcIndexMetaData getIndex(String indexName) {
        // TODO Auto-generated method stub
        return null;
    }
}
