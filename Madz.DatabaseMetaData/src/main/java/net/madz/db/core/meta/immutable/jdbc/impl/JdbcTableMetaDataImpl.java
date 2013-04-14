package net.madz.db.core.meta.immutable.jdbc.impl;

import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.immutable.impl.TableMetaDataImpl;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;

public final class JdbcTableMetaDataImpl extends
        TableMetaDataImpl<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> implements JdbcTableMetaData {

    public JdbcTableMetaDataImpl(JdbcSchemaMetaData schema, JdbcTableMetaData tableMetaData) {
        super(schema, tableMetaData);
    }

    public void addAllFks(List<JdbcForeignKeyMetaData> fks) {
        for ( JdbcForeignKeyMetaData fk : fks ) {
            this.fkList.add(fk);
        }
    }

    public void setPrimaryKey(JdbcIndexMetaData indexMetaData) {
        this.primaryKey = indexMetaData;
    }

    public void addAllColumns(LinkedList<JdbcColumnMetaDataImpl> columns) {
        for ( JdbcColumnMetaData column : columns ) {
            this.columnMap.put(column.getColumnName(), column);
            this.orderedColumns.add(column);
        }
    }

    public void addAllIndexes(List<JdbcIndexMetaData> indexes) {
        for ( JdbcIndexMetaData index : indexes ) {
            this.indexMap.put(index.getIndexName(), index);
        }
    }
}
