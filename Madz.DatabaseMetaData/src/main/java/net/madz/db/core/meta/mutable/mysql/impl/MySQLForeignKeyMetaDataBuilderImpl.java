package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.madz.db.core.meta.immutable.ForeignKeyEntry;
import net.madz.db.core.meta.immutable.IndexEntry;
import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.impl.MySQLForeignKeyMetaDataImpl;
import net.madz.db.core.meta.immutable.types.CascadeRule;
import net.madz.db.core.meta.mutable.impl.BaseForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;
import net.madz.db.utils.MessageConsts;
import net.madz.db.utils.ResourceManagementUtils;

public class MySQLForeignKeyMetaDataBuilderImpl
        extends
        BaseForeignKeyMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLForeignKeyMetaDataBuilder {

    public MySQLForeignKeyMetaDataBuilderImpl(MySQLTableMetaDataBuilder table, String name) {
        this.fkTable = table;
        this.foreignKeyPath = table.getTablePath().append(name);
    }

    public MySQLForeignKeyMetaDataBuilderImpl(MySQLTableMetaDataBuilder tableBuilder, MetaDataResultSet<MySQLForeignKeyDbMetaDataEnum> rs) throws SQLException {
        this.fkTable = tableBuilder;
        this.foreignKeyPath = tableBuilder.getTablePath().append(rs.get(MySQLForeignKeyDbMetaDataEnum.CONSTRAINT_NAME));
        this.updateRule = CascadeRule.getRule(rs.get(MySQLForeignKeyDbMetaDataEnum.UPDATE_RULE));
        this.deleteRule = CascadeRule.getRule(rs.get(MySQLForeignKeyDbMetaDataEnum.DELETE_RULE));
        // [ToDo] [Tracy] about how to get pkTable
        // Below code suppose the referenced table is in the same
        // schema,
        // but actually, the referenced table could be in another
        // schema.
        this.pkTable = this.fkTable.getSchema().getTableBuilder(rs.get(MySQLForeignKeyDbMetaDataEnum.REFERENCED_TABLE_NAME));
        // Note: some times unique_constraint_name is null
        this.pkIndex = this.pkTable.getIndexBuilder(rs.get(MySQLForeignKeyDbMetaDataEnum.UNIQUE_CONSTRAINT_NAME));
        this.fkIndex = this.fkTable.getIndexBuilder(rs.get(MySQLForeignKeyDbMetaDataEnum.CONSTRAINT_NAME));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public MySQLForeignKeyMetaDataBuilder build(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM referential_constraints WHERE constraint_schema='" + this.fkTable.getTablePath().getParent().getName()
                    + "' AND constraint_name='" + this.foreignKeyPath.getName() + "';");
            while ( rs.next() ) {
                this.updateRule = CascadeRule.getRule(rs.getString("update_rule"));
                this.deleteRule = CascadeRule.getRule(rs.getString("delete_rule"));
                // [ToDo] [Tracy] about how to get pkTable
                // Below code suppose the referenced table is in the same
                // schema,
                // but actually, the referenced table could be in another
                // schema.
                this.pkTable = this.fkTable.getSchema().getTableBuilder(rs.getString("referenced_table_name"));
                // Note: some times unique_constraint_name is null
                this.pkIndex = this.pkTable.getIndexBuilder(rs.getString("unique_constraint_name"));
                this.fkIndex = this.fkTable.getIndexBuilder(rs.getString("constraint_name"));
            }
        } finally {
            ResourceManagementUtils.closeResultSet(rs);
        }
        try {
            rs = stmt.executeQuery("SELECT * FROM key_column_usage WHERE constraint_schema='" + this.fkTable.getSchema().getSchemaName()
                    + "' AND constraint_name = '" + this.foreignKeyPath.getName()
                    + "' AND referenced_table_name IS NOT NULL AND referenced_column_name IS NOT NULL;");
            while ( rs.next() ) {
                final String columnName = rs.getString("column_name");
                final String referencedColumnName = rs.getString("referenced_column_name");
                final MySQLColumnMetaData fkColumn = this.fkTable.getColumnBuilder(columnName);
                final MySQLColumnMetaData pkColumn = this.pkTable.getColumnBuilder(referencedColumnName);
                final Short seq = rs.getShort("ordinal_position");
                final BaseForeignKeyMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>.Entry entry = new BaseForeignKeyMetaDataBuilder.Entry(
                        fkColumn, pkColumn, this, seq);
                final MySQLColumnMetaDataBuilder columnBuilder = this.fkTable.getColumnBuilder(columnName);
                columnBuilder.appendForeignKeyEntry(entry);
                this.addEntry(entry);
            }
        } finally {
            ResourceManagementUtils.closeResultSet(rs);
        }
        // Handle the situation that fkIndex is null, which will be happend when
        // creating fk without a constraint name. The name of index
        // auto-generated doesn't match the constraint name.
        if ( null == this.fkIndex ) {
            final Map<Short, MySQLColumnMetaData> fkColumns = new HashMap<Short, MySQLColumnMetaData>();
            List<ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> fkEntrySet = this.entryList;
            for ( ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : fkEntrySet ) {
                fkColumns.put(entry.getSeq(), entry.getForeignKeyColumn());
            }
            Collection<MySQLIndexMetaDataBuilder> indexSet = this.fkTable.getIndexBuilderSet();
            Map<Short, MySQLColumnMetaData> indexColumns = null;
            for ( MySQLIndexMetaDataBuilder index : indexSet ) {
                indexColumns = new HashMap<Short, MySQLColumnMetaData>();
                final Collection<IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entrySet = index
                        .getEntrySet();
                for ( IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : entrySet ) {
                    indexColumns.put(entry.getPosition(), entry.getColumn());
                }
                // if ( fkColumns.size() != indexColumns.size() ) {
                // continue;
                // }
                boolean matched = true;
                for ( Short key : fkColumns.keySet() ) {
                    MySQLColumnMetaData pkColumn = fkColumns.get(key);
                    MySQLColumnMetaData indexColumn = indexColumns.get(key);
                    if ( !pkColumn.getColumnPath().equals(indexColumn.getColumnPath()) ) {
                        matched = false;
                        break;
                    }
                }
                if ( matched ) {
                    this.fkIndex = this.fkTable.getIndexBuilder(index.getIndexName());
                    break;
                }
            }
            // if (null == this.fkIndex) {
            // throw new
            // IllegalStateException(MessageConsts.FK_INDEX_SHOULD_NOT_BE_NULL);
            // }
        }
        return this;
    }

    @Override
    public MySQLForeignKeyMetaData createMetaData() {
        this.constructedMetaData = new MySQLForeignKeyMetaDataImpl(this.fkTable.getMetaData(), this);
        return constructedMetaData;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public MySQLForeignKeyMetaDataBuilder build(JdbcForeignKeyMetaData jFk) {
        if ( null == jFk ) {
            throw new IllegalArgumentException(MessageConsts.ARGUMENT_SHOULD_NOT_BE_NULL);
        }
        this.updateRule = jFk.getUpdateCascadeRule();
        this.deleteRule = jFk.getDeleteCascadeRule();
        // [ToDo] [Tracy] about how to get pkTable
        // Below code suppose the referenced table is in the same
        // schema,
        // but actually, the referenced table could be in another
        // schema.
        this.pkTable = this.fkTable.getSchema().getTableBuilder(jFk.getPrimaryKeyTable().getTableName());
        // Note: some times unique_constraint_name is null
        this.pkIndex = null;
        this.fkIndex = null;
        final List<ForeignKeyEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData>> entrySet = jFk
                .getEntrySet();
        for ( ForeignKeyEntry<JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData> entry : entrySet ) {
            final String columnName = entry.getForeignKeyColumnName();
            final String referencedColumnName = entry.getPrimaryKeyColumnName();
            final MySQLColumnMetaData fkColumn = this.fkTable.getColumnBuilder(columnName);
            final MySQLColumnMetaData pkColumn = this.pkTable.getColumnBuilder(referencedColumnName);
            final Short seq = entry.getSeq();
            final BaseForeignKeyMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>.Entry mysqlEntry = new BaseForeignKeyMetaDataBuilder.Entry(
                    fkColumn, pkColumn, this, seq);
            final MySQLColumnMetaDataBuilder columnBuilder = this.fkTable.getColumnBuilder(columnName);
            columnBuilder.appendForeignKeyEntry(mysqlEntry);
            this.addEntry(mysqlEntry);
        }
        return this;
    }

    @Override
    public String getForeignKeyIndexName() {
        return fkIndex.getIndexName();
    }

    @Override
    public String getForeignKeyTableName() {
        return fkTable.getTableName();
    }

    @Override
    public String getPrimaryKeyIndexName() {
        return pkIndex.getIndexName();
    }

    @Override
    public String getPrimaryKeyTableName() {
        return pkTable.getTableName();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addEntry(MetaDataResultSet<MySQLForeignKeyDbMetaDataEnum> rs) throws SQLException {
        final String columnName = rs.get(MySQLForeignKeyDbMetaDataEnum.COLUMN_NAME);
        final String referencedColumnName = rs.get(MySQLForeignKeyDbMetaDataEnum.REFERENCED_COLUMN_NAME);
        final MySQLColumnMetaData fkColumn = this.fkTable.getColumnBuilder(columnName);
        final MySQLColumnMetaData pkColumn = this.pkTable.getColumnBuilder(referencedColumnName);
        final Short seq = rs.getShort(MySQLForeignKeyDbMetaDataEnum.ORDINAL_POSITION);
        final BaseForeignKeyMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>.Entry entry = new BaseForeignKeyMetaDataBuilder.Entry(
                fkColumn, pkColumn, this, seq);
        final MySQLColumnMetaDataBuilder columnBuilder = this.fkTable.getColumnBuilder(columnName);
        columnBuilder.appendForeignKeyEntry(entry);
        this.addEntry(entry);
    }
}
