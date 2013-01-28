package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.immutable.IndexMetaData.Entry;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;
import net.madz.db.core.meta.immutable.mysql.impl.MySQLTableMetaDataImpl;
import net.madz.db.core.meta.immutable.types.TableType;
import net.madz.db.core.meta.mutable.impl.BaseTableMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;
import net.madz.db.utils.ResourceManagementUtils;

public class MySQLTableMetaDataBuilderImpl
        extends
        BaseTableMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLTableMetaDataBuilder {

    private MySQLEngineEnum engine;
    private String characterSet;
    private String collation;

    public MySQLTableMetaDataBuilderImpl(MySQLSchemaMetaDataBuilder schema, String tableName) {
        super(schema, tableName);
    }

    public MySQLTableMetaDataBuilder build(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        try {
            final String schemaName = super.schema.getSchemaPath().getName();
            stmt.executeQuery("use information_schema;");
            try {
                rs = stmt.executeQuery("SELECT * FROM tables INNER JOIN collations ON  table_collation = collation_name WHERE table_schema = '" + schemaName
                        + "' AND table_name='" + getTableName() + "';");
                while ( rs.next() ) {
                    this.remarks = rs.getString(MySQLTableDbMetaDataEnum.TABLE_COMMENT.name());
                    this.type = TableType.convertTableType(MySQLTableTypeEnum.getType(rs.getString(MySQLTableDbMetaDataEnum.TABLE_TYPE.name())));
                    this.idCol = null;
                    this.idGeneration = null;
                    this.collation = rs.getString(MySQLTableDbMetaDataEnum.TABLE_COLLATION.name());
                    this.engine = MySQLEngineEnum.valueOf(rs.getString(MySQLTableDbMetaDataEnum.ENGINE.name()));
                    this.characterSet = rs.getString(MySQLTableDbMetaDataEnum.CHARACTER_SET_NAME.name());
                }
            } finally {
                ResourceManagementUtils.closeResultSet(rs);
            }
            // Parse Columns
            final List<String> colNames = new LinkedList<String>();
            try {
                rs = stmt.executeQuery("SELECT * FROM columns WHERE table_schema='" + schemaName + "' AND table_name='" + getTableName() + "';");
                while ( rs.next() ) {
                    colNames.add(rs.getString("column_name"));
                }
            } finally {
                ResourceManagementUtils.closeResultSet(rs);
            }
            for ( String colName : colNames ) {
                MySQLColumnMetaDataBuilder columnBuilder = new MySQLColumnMetaDataBuilderImpl(this, this.tablePath.append(colName)).build(conn);
                appendColumnMetaDataBuilder(columnBuilder);
            }
            // Parse Index
            final List<String> indexNames = new LinkedList<String>();
            try {
                rs = stmt.executeQuery("SELECT * FROM statistics WHERE table_schema='" + schemaName + "' AND table_name='" + getTableName() + "';");
                while ( rs.next() ) {
                    indexNames.add(rs.getString("index_name"));
                }
            } finally {
                ResourceManagementUtils.closeResultSet(rs);
            }
            for ( String indexName : indexNames ) {
                MySQLIndexMetaDataBuilder indexBuilder = new MySQLIndexMetaDataBuilderImpl(this, this.tablePath.append(indexName)).build(conn);
                appendIndexMetaDataBuilder(indexBuilder);
            }
            // Parse Primary Key
            MySQLIndexMetaDataBuilder pk = this.indexMap.get("PRIMARY");
            if ( null != pk ) {
                Collection<Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entrySet = pk
                        .getEntrySet();
                for ( Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : entrySet ) {
                    MySQLColumnMetaDataBuilder columnBuilder = this.columnMap.get(entry.getColumn().getColumnName());
                    columnBuilder.setPrimaryKey(entry);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            rs.close();
        }
        return this;
    }

    @Override
    public MySQLEngineEnum getEngine() {
        return this.engine;
    }

    @Override
    public String getCharacterSet() {
        return this.characterSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }

    @Override
    public MySQLTableMetaData getMetaData() {
        // Get column metadata
        final LinkedList<MySQLColumnMetaData> columnMetaDatas = new LinkedList<MySQLColumnMetaData>();
        for ( MySQLColumnMetaDataBuilder cb : this.columnMap.values() ) {
            columnMetaDatas.add(cb.getMetaData());
        }
        final LinkedList<MySQLIndexMetaData> indexMetaDatas = new LinkedList<MySQLIndexMetaData>();
        // Get index metaData
        for ( MySQLIndexMetaDataBuilder indexBuilder : this.indexMap.values() ) {
            indexMetaDatas.add(indexBuilder.getMetaData());
        }
        // Get fk metaData
        final List<MySQLForeignKeyMetaData> fkMetaDatas = new LinkedList<MySQLForeignKeyMetaData>();
        for ( MySQLForeignKeyMetaDataBuilder fkBuilder : this.fkList ) {
            fkMetaDatas.add(fkBuilder.getMetaData());
        }
        return new MySQLTableMetaDataImpl(this,columnMetaDatas,indexMetaDatas,fkMetaDatas);
    }

    @Override
    public MySQLIndexMetaDataBuilder getIndexBuilder(String indexName) {
        return this.indexMap.get(indexName);
    }
}
