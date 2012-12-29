package net.madz.crmp.db.core.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.madz.crmp.db.core.AbsSchemaMetaDataParser;
import net.madz.crmp.db.metadata.Column;
import net.madz.crmp.db.metadata.ForeignKeyItem;
import net.madz.crmp.db.metadata.PrimaryKey;
import net.madz.crmp.db.metadata.PrimaryKeyItem;
import net.madz.crmp.db.metadata.SchemaMetaData;
import net.madz.crmp.db.metadata.Table;
import net.madz.crmp.db.metadata.UniqueKey;
import net.madz.crmp.db.metadata.UniqueKeyItem;

public class MySQLSchemaMetaDataParser extends AbsSchemaMetaDataParser {

    private SchemaMetaData schemaMetaData;

    public MySQLSchemaMetaDataParser(String databaseName, Connection conn) {
        super(databaseName, conn);
    }

    @Override
    public SchemaMetaData parseSchemaMetaData() {
        schemaMetaData = new SchemaMetaData(databaseName);
        try {
            parseMetadata(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schemaMetaData;
    }

    private void parseMetadata(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        String types[] = { "TABLE" };
        // Tables
        //
        ResultSet rs = metaData.getTables(databaseName, null, null, types);
        List<String> tableNames = new ArrayList<String>();
        while ( rs.next() ) {
            tableNames.add(rs.getString("TABLE_NAME"));
        }
        for ( String tName : tableNames ) {
            Table t = new Table(tName);
            schemaMetaData.addTable(t);
        }
        // Columns
        //
        for ( Table t : schemaMetaData.getTables() ) {
            ResultSet clrs = metaData.getColumns(databaseName, null, t.getName(), null);
            // Columns
            //
            while ( clrs.next() ) {
                Column cl = new Column();
                cl.setName(clrs.getString("COLUMN_NAME"));
                cl.setDataType(clrs.getInt("DATA_TYPE"));
                cl.setTypeName(clrs.getString("TYPE_NAME"));
                cl.setColumnSize(clrs.getInt("COLUMN_SIZE"));
                cl.setNullable(clrs.getInt("NULLABLE"));
                cl.setDefaultValue(clrs.getString("COLUMN_DEF"));
                cl.setSqlDataType(clrs.getInt("SQL_DATA_TYPE"));
                cl.setRemarks(clrs.getString("REMARKS"));
                cl.setAutoIncrement(clrs.getString("IS_AUTOINCREMENT"));
                t.addColumn(cl);
            }
            // Primary keys
            //
            ResultSet pkrs = metaData.getPrimaryKeys(databaseName, null, t.getName());
            if ( pkrs.isBeforeFirst() ) {
                PrimaryKey pk = new PrimaryKey(t);
                while ( pkrs.next() ) {
                    Column column = t.getColumn(pkrs.getString("COLUMN_NAME"));
                    PrimaryKeyItem item = new PrimaryKeyItem(t, column, pkrs.getShort("KEY_SEQ"));
                    pk.addKey(item);
                }
                t.setPrimaryKey(pk);
            }
            // Unique Keys
            //
            ResultSet indexrs = metaData.getIndexInfo(databaseName, null, t.getName(), true, true);
            if ( indexrs.isBeforeFirst() ) {
                List<UniqueKeyItem> items = new LinkedList<UniqueKeyItem>();
                while ( indexrs.next() ) {
                    if ( "PRIMARY".equalsIgnoreCase(indexrs.getString("INDEX_NAME")) ) {
                        continue;
                    }
                    UniqueKeyItem ukItem = new UniqueKeyItem();
                    ukItem.setColumn(t.getColumn(indexrs.getString("COLUMN_NAME")));
                    ukItem.setSeq(indexrs.getShort("ORDINAL_POSITION"));
                    items.add(ukItem);
                }
                if ( items.size() > 0 ) {
                    UniqueKey uk = new UniqueKey();
                    uk.addItem(items);
                    uk.setTable(t);
                    t.addUniqueKeys(uk);
                }
            }
        }
        for ( Table t : schemaMetaData.getTables() ) {
            // Foreign keys
            //
            ResultSet fkrs = metaData.getImportedKeys(databaseName, null, t.getName());
            while ( fkrs.next() ) {
                String pkColumnName = fkrs.getString("PKCOLUMN_NAME");
                String pkTableName = fkrs.getString("PKTABLE_NAME");
                String fkColumnName = fkrs.getString("FKCOLUMN_NAME");
                String fkName = fkrs.getString("FK_NAME");
                int keySeq = fkrs.getInt("KEY_SEQ");
                Table pkTable = schemaMetaData.getTable(pkTableName);
                ForeignKeyItem fk = new ForeignKeyItem(pkTable, pkTable.getColumn(pkColumnName), t, t.getColumn(fkColumnName), fkName, keySeq);
                t.addForeignKey(fkName, fk);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( schemaMetaData == null ) ? 0 : schemaMetaData.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        MySQLSchemaMetaDataParser other = (MySQLSchemaMetaDataParser) obj;
        if ( schemaMetaData == null ) {
            if ( other.schemaMetaData != null ) return false;
        } else if ( !schemaMetaData.equals(other.schemaMetaData) ) return false;
        return true;
    }
}
