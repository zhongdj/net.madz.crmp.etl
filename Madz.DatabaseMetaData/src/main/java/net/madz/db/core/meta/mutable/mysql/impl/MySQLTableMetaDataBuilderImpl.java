package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.IndexMetaData.Entry;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;
import net.madz.db.core.meta.immutable.types.TableType;
import net.madz.db.core.meta.mutable.impl.BaseTableMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public class MySQLTableMetaDataBuilderImpl
        extends
        BaseTableMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLTableMetaDataBuilder {

    private MySQLEngineEnum engine;
    private String characterSet;
    private String collation;
    private final String tableName;
    private DottedPath tablePath;

    public MySQLTableMetaDataBuilderImpl(MySQLSchemaMetaDataBuilder schema, String tableName) {
        super(schema);
        this.tableName = tableName;
        tablePath = schema.getSchemaPath().append(tableName);
    }

    public MySQLTableMetaDataBuilder build(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        try {
            final String schemaName = super.schema.getSchemaPath().getName();
            rs = stmt.executeQuery("SELECT * FROM tables INNER JOIN character_sets ON default_collate_name = table_collation WHERE schema_name = '"
                    + schemaName + "' AND table_name='" + this.tableName + "';");
            while ( rs.next() ) {
                this.remarks = rs.getString(MySQLTableDbMetaDataEnum.TABLE_COMMENT.name());
                this.type = TableType.convertTableType(MySQLTableTypeEnum.getType(rs.getString(MySQLTableDbMetaDataEnum.TABLE_TYPE.name())));
                this.idCol = null;
                this.idGeneration = null;
                this.collation = rs.getString(MySQLTableDbMetaDataEnum.TABLE_COLLATION.name());
                this.engine = MySQLEngineEnum.valueOf(rs.getString(MySQLTableDbMetaDataEnum.ENGINE.name()));
                this.characterSet = rs.getString(MySQLTableDbMetaDataEnum.CHARACTER_SET_NAME.name());
            }
            // Parse Columns
            rs = stmt.executeQuery("SELECT * FROM columns WHERE table_schema='" + schemaName + "' AND table_name='" + this.tableName + "';");
            List<String> colNames = new LinkedList<String>();
            while ( rs.next() ) {
                colNames.add(rs.getString("column_name"));
            }
            for ( String colName : colNames ) {
                MySQLColumnMetaDataBuilder columnBuilder = new MySQLColumnMetaDataBuilderImpl(this, this.tablePath.append(colName)).build(conn);
                appendColumnMetaDataBuilder(columnBuilder);
            }
            // Parse Index
            rs = stmt.executeQuery("SELECT * FROM statistics WHERE table_schema='" + schemaName + "' AND table_name='" + this.tableName + "';");
            List<String> indexNames = new LinkedList<String>();
            while ( rs.next() ) {
                indexNames.add(rs.getString("index_name"));
            }
            for ( String indexName : indexNames ) {
                MySQLIndexMetaDataBuilder indexBuilder = new MySQLIndexMetaDataBuilderImpl(this, this.tablePath.append(indexName)).build(conn);
                appendIndexMetaDataBuilder(indexBuilder);
            }
            // Parse Primary Key
            MySQLIndexMetaDataBuilder pk = this.indexMap.get(( "PRIMARY" ));
            if ( null != pk ) {
                Collection<Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entrySet = pk
                        .getEntrySet();
                for ( Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : entrySet ) {
                    MySQLColumnMetaDataBuilder columnBuilder = this.columnMap.get(entry.getColumn().getColumnName());
                    columnBuilder.setPrimaryKey(entry);
                }
            }
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MySQLIndexMetaDataBuilder getIndexBuilder(String indexName) {
        return this.indexMap.get(indexName);
    }
}
