package net.madz.db.core.meta.mutable.jdbc.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.impl.enums.AccessForeignKeyDbMetaDataEnum;
import net.madz.db.core.meta.immutable.impl.enums.TableDbMetaDataEnum;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.immutable.jdbc.impl.JdbcSchemaMetaDataImpl;
import net.madz.db.core.meta.immutable.jdbc.impl.JdbcTableMetaDataImpl;
import net.madz.db.core.meta.mutable.impl.BasedSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcTableMetaDataBuilder;
import net.madz.db.utils.ResourceManagementUtils;

public class JdbcSchemaMetaDataBuilderImpl
        extends
        BasedSchemaMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData>
        implements JdbcSchemaMetaDataBuilder {

    public JdbcSchemaMetaDataBuilderImpl(String databaseName) throws SQLException {
        super(databaseName);
    }

    @Override
    public JdbcSchemaMetaDataBuilder build(Connection conn) throws SQLException {
        final DatabaseMetaData metaData = conn.getMetaData();
        // Tables
        ResultSet jTableRS = null;
        try {
            jTableRS = metaData.getTables(null, null, null, new String[] { "TABLE" });
            final MetaDataResultSet<TableDbMetaDataEnum> tableRS = new MetaDataResultSet<TableDbMetaDataEnum>(jTableRS, TableDbMetaDataEnum.values());
            while ( tableRS.next() ) {
                final JdbcTableMetaDataBuilder jTableBuilder = new JdbcTableMetaDataBuilderImpl(metaData, this, tableRS.get(TableDbMetaDataEnum.table_name))
                        .build(conn);
                appendTableMetaDataBuilder(jTableBuilder);
            }
        } finally {
            ResourceManagementUtils.closeResultSet(jTableRS);
        }
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for ( JdbcTableMetaDataBuilder jTableBuilder : this.tableList ) {
                final ResultSet rawForeignKeys = stmt.executeQuery("SELECT * FROM MSysRelationships WHERE szObject='" + jTableBuilder.getTableName() + "'");
                final MetaDataResultSet<AccessForeignKeyDbMetaDataEnum> foreignKeys = new MetaDataResultSet<AccessForeignKeyDbMetaDataEnum>(rawForeignKeys,
                        AccessForeignKeyDbMetaDataEnum.values());
                while ( foreignKeys.next() ) {
                    String fkName = foreignKeys.get(AccessForeignKeyDbMetaDataEnum.SZRELATIONSHIP);// FK_NAME
                    JdbcForeignKeyMetaDataBuilder jFKBuilder = jTableBuilder.getForeignKeyBuilder(fkName);
                    if ( null == jFKBuilder ) {
                        jFKBuilder = new JdbcForeignKeyMetaDataBuilderImpl(foreignKeys, jTableBuilder, fkName);
                        jTableBuilder.appendForeignKeyMetaDataBuilder(jFKBuilder);
                    }
                    jFKBuilder.addEntry(foreignKeys);
                }
            }
        } finally {
            stmt.close();
        }
        return this;
    }

    @Override
    protected JdbcSchemaMetaData createMetaData() {
        final JdbcSchemaMetaDataImpl result = new JdbcSchemaMetaDataImpl(this);
        this.constructedMetaData = result;
        if ( 0 >= this.tableList.size() ) {
            return this.constructedMetaData;
        }
        final List<JdbcTableMetaData> tables = new LinkedList<JdbcTableMetaData>();
        for ( JdbcTableMetaDataBuilder tableBuilder : this.tableList ) {
            tables.add(tableBuilder.getMetaData());
        }
        // Bind relations
        result.addAllTables(tables);
        for ( JdbcTableMetaDataBuilder tableBuilder : this.tableList ) {
            final List<JdbcForeignKeyMetaData> fks = new LinkedList<JdbcForeignKeyMetaData>();
            final Collection<JdbcForeignKeyMetaDataBuilder> foreignKeyBuilderSet = tableBuilder.getForeignKeyBuilderSet();
            final JdbcTableMetaDataImpl table = (JdbcTableMetaDataImpl) result.getTable(tableBuilder.getTableName());
            for ( JdbcForeignKeyMetaDataBuilder fkBuilder : foreignKeyBuilderSet ) {
                fks.add(fkBuilder.getMetaData());
            }
            table.addAllFks(fks);
        }
        return constructedMetaData;
    }

    @Override
    public JdbcTableMetaDataBuilder getTableBuilder(String name) {
        return this.tableBuilderMap.get(name);
    }
}
