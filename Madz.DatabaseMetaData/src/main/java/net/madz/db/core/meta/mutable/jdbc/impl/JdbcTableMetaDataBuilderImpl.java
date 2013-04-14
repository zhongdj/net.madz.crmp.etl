package net.madz.db.core.meta.mutable.jdbc.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.configuration.Database;
import net.madz.db.configuration.Table;
import net.madz.db.core.impl.DbConfigurationManagement;
import net.madz.db.core.meta.immutable.IndexEntry;
import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.impl.enums.ColumnDbMetaDataEnum;
import net.madz.db.core.meta.immutable.impl.enums.IndexDbMetaDataEnum;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.immutable.jdbc.impl.JdbcColumnMetaDataImpl;
import net.madz.db.core.meta.immutable.jdbc.impl.JdbcTableMetaDataImpl;
import net.madz.db.core.meta.mutable.impl.BaseTableMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcTableMetaDataBuilder;
import net.madz.db.utils.ResourceManagementUtils;

public class JdbcTableMetaDataBuilderImpl
        extends
        BaseTableMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData>
        implements JdbcTableMetaDataBuilder {

    public JdbcTableMetaDataBuilderImpl(JdbcSchemaMetaDataBuilder schema, String tableName) {
        super(schema, tableName);
    }

    public JdbcTableMetaDataBuilderImpl(DatabaseMetaData metaData, JdbcSchemaMetaDataBuilder schema, String tableName) throws SQLException {
        super(schema, tableName);
        // columns
        ResultSet jColumnRs = null;
        try {
            jColumnRs = metaData.getColumns(null, null, getTableName(), null);
            final MetaDataResultSet<ColumnDbMetaDataEnum> columnRs = new MetaDataResultSet<ColumnDbMetaDataEnum>(jColumnRs, ColumnDbMetaDataEnum.values());
            while ( columnRs.next() ) {
                final JdbcColumnMetaDataBuilderImpl jColumnBuilder = new JdbcColumnMetaDataBuilderImpl(this, columnRs);
                appendColumnMetaDataBuilder(jColumnBuilder);
            }
        } finally {
            ResourceManagementUtils.closeResultSet(jColumnRs);
        }
        // indexes
        ResultSet jIndexRs = null;
        try {
            jIndexRs = metaData.getIndexInfo(null, null, getTableName(), false, true);
            final MetaDataResultSet<IndexDbMetaDataEnum> indexRs = new MetaDataResultSet<IndexDbMetaDataEnum>(jIndexRs, IndexDbMetaDataEnum.values());
            while ( indexRs.next() ) {
                String indexName = indexRs.get(IndexDbMetaDataEnum.INDEX_NAME);
                if ( null == indexName || 0 >= indexName.length() ) {
                    continue;
                }
                JdbcIndexMetaDataBuilder indexBuilder = this.getIndexBuilder(indexName);
                if ( null == indexBuilder ) {
                    indexBuilder = new JdbcIndexMetaDataBuilderImpl(this, indexRs, indexName);
                }
                appendIndexMetaDataBuilder(indexBuilder);
                indexBuilder.addIndexEntry(indexRs);
            }
        } finally {
            ResourceManagementUtils.closeResultSet(jIndexRs);
        }
        String primaryKeyName = "PrimaryKey";
        final Database databaseInformation = DbConfigurationManagement.findDatabaseInformation(this.schema.getSchemaName(), false);
        final List<Table> tables = databaseInformation.getTable();
        for ( Table table : tables ) {
            if ( table.getName().equalsIgnoreCase(getTableName()) ) {
                primaryKeyName = table.getPrimaryKeyName();
            }
        }
        // pks
        for ( String indexName : indexMap.keySet() ) {
            if ( indexName.equalsIgnoreCase(primaryKeyName) ) {
                primaryKey = indexMap.get(primaryKeyName);
                primaryKey.setPrimaryKey();
            }
        }
    }

    @Override
    public JdbcIndexMetaDataBuilder getIndexBuilder(String indexName) {
        return this.indexMap.get(indexName);
    }

    @Override
    public JdbcTableMetaDataBuilder build(Connection conn) throws SQLException {
        return this;
    }

    @Override
    public JdbcSchemaMetaData getParent() {
        return this.schema;
    }

    @Override
    protected JdbcTableMetaData createMetaData() {
        JdbcTableMetaDataImpl result = new JdbcTableMetaDataImpl(this.schema.getMetaData(), this);
        this.constructedMetaData = result;
        final LinkedList<JdbcColumnMetaDataImpl> columns = new LinkedList<JdbcColumnMetaDataImpl>();
        for ( JdbcColumnMetaDataBuilder columnBuilder : this.orderedColumns ) {
            final JdbcColumnMetaDataImpl column = new JdbcColumnMetaDataImpl(this.getMetaData(), columnBuilder);
            columns.add(column);
        }
        result.addAllColumns(columns);
        final List<JdbcIndexMetaData> indexes = new LinkedList<JdbcIndexMetaData>();
        for ( JdbcIndexMetaDataBuilder indexBuilder : this.indexMap.values() ) {
            final JdbcIndexMetaData indexMetaData = indexBuilder.getMetaData();
            indexes.add(indexMetaData);
            final boolean isPrimary = indexMetaData.getIndexName().equalsIgnoreCase("PrimaryKey");
            if ( isPrimary ) {
                result.setPrimaryKey(indexMetaData);
            }
            final boolean isUnique = indexMetaData.isUnique();
            Collection<IndexEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData>> entrySet = indexMetaData
                    .getEntrySet();
            for ( IndexEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> entry : entrySet ) {
                final String columnName = entry.getColumn().getColumnName();
                for ( JdbcColumnMetaDataImpl column : columns ) {
                    if ( columnName.equalsIgnoreCase(column.getColumnName()) ) {
                        if ( isUnique ) {
                            column.addUniqueIndexEntry(entry);
                        } else {
                            column.addNonUniqueIndexEntry(entry);
                        }
                        if ( isPrimary ) {
                            column.setPrimaryKey(entry);
                        }
                    }
                }
            }
        }
        result.addAllIndexes(indexes);
        return constructedMetaData;
    }

    @Override
    public JdbcForeignKeyMetaDataBuilder getForeignKeyBuilder(String fkName) {
        if ( null == fkName || 0 >= fkName.length() ) {
            return null;
        }
        for ( JdbcForeignKeyMetaDataBuilder fkBuilder : this.fkList ) {
            if ( fkName.equals(fkBuilder.getForeignKeyName()) ) {
                return fkBuilder;
            }
        }
        return null;
    }

    @Override
    public Collection<JdbcForeignKeyMetaDataBuilder> getForeignKeyBuilderSet() {
        return this.fkList;
    }

    @Override
    public JdbcColumnMetaDataBuilder getColumnBuilder(String columnName) {
        return this.columnMap.get(columnName.toLowerCase());
    }
}
