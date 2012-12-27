package net.madz.crmp.db.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MySqlResolver {

    private static final String DBNAME = "crmp";
    private Map<String, Table> tables = new HashMap<String, Table>();

    public static void main(String[] args) {
        MySqlResolver resolver = new MySqlResolver();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection sourceDBConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBNAME + "?tinyInt1isBit=false", "root", "1q2w3e4r5t");
            resolver.parseMetadata(sourceDBConn);
            List<String> sqlBatch = resolver.generateCreateSQL();
            resolver.submitSqlBatch(sqlBatch);
            List<String> modifyTablesBatch = resolver.generateModifySQL();
            resolver.submitSqlBatch(modifyTablesBatch);
            Connection targetDBConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crmp2" + "?tinyInt1isBit=false", "root", "1q2w3e4r5t");
            validate(sourceDBConn.getMetaData(), targetDBConn.getMetaData());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void validate(DatabaseMetaData sourceDB, DatabaseMetaData targetDB) {
    }

    private List<String> generateModifySQL() {
        List<String> stmtBatch = new LinkedList<String>();
        for ( Table table : tables.values() ) {
            Map<String, List<ForeignKeyItem>> fks = table.getFks();
            Set<Entry<String, List<ForeignKeyItem>>> entrySet = fks.entrySet();
            for ( Entry<String, List<ForeignKeyItem>> fk : entrySet ) {
                StringBuilder result = new StringBuilder();
                result.append("alter table ");
                result.append(table.getName());
                result.append(" add foreign key ");
                List<ForeignKeyItem> items = fk.getValue();
                result.append(fk.getKey());
                result.append("(");
                Table pkTable = null;
                for ( ForeignKeyItem item : items ) {
                    result.append(item.getFkColumn().getName());
                    result.append(",");
                    pkTable = item.getPkTable();
                }
                result.deleteCharAt(result.length() - 1);
                result.append(")");
                result.append(" references ");
                result.append(pkTable.getName());
                result.append("(");
                for ( ForeignKeyItem item : items ) {
                    result.append(item.getPkColumn().getName());
                    result.append(",");
                }
                result.deleteCharAt(result.length() - 1);
                result.append(");\n");
                stmtBatch.add(result.toString());
            }
        }
        return stmtBatch;
    }

    private void submitSqlBatch(List<String> sqlBatch) {
        Connection targetDBConn = null;
        try {
            targetDBConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crmp2" + "?tinyInt1isBit=false", "root", "1q2w3e4r5t");
            Statement cs = targetDBConn.createStatement();
            for ( String stmt : sqlBatch ) {
                System.out.println("===" + stmt);
                cs.addBatch(stmt);
            }
            cs.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                targetDBConn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> generateCreateSQL() {
        List<String> sqlBatch = new LinkedList<String>();
        for ( Table table : tables.values() ) {
            StringBuilder createSqlStmt = new StringBuilder();
            boolean autoIncrement = false;
            createSqlStmt.append("create table ");
            createSqlStmt.append(table.getName());
            createSqlStmt.append("(");
            for ( Column cl : table.getColumns() ) {
                createSqlStmt.append(cl.getName());
                createSqlStmt.append(" ");
                createSqlStmt.append(cl.getTypeName());
                if ( "varchar".equalsIgnoreCase(cl.getTypeName()) ) {
                    createSqlStmt.append("(");
                    createSqlStmt.append(cl.getColumnSize());
                    createSqlStmt.append(")");
                }
                if ( "tinyint".equalsIgnoreCase(cl.getTypeName()) ) {
                    createSqlStmt.append("(");
                    createSqlStmt.append(cl.getColumnSize());
                    createSqlStmt.append(")");
                }
                if ( cl.getNullable() > 0 ) {
                    createSqlStmt.append(" null");
                } else {
                    createSqlStmt.append(" not null");
                }
                if ( cl.isAutoIncrement() ) {
                    createSqlStmt.append(" primary key ");
                    createSqlStmt.append(" auto_increment ");
                    autoIncrement = true;
                }
                createSqlStmt.append(",");
            }
            createSqlStmt.deleteCharAt(createSqlStmt.length() - 1);
            createSqlStmt.append(");\n");
            sqlBatch.add(createSqlStmt.toString());
            // Primary keys
            //
            if ( false == autoIncrement ) {
                if ( null != table.getPk() ) {
                    StringBuilder alterTableStmt = new StringBuilder();
                    alterTableStmt.append("alter table ");
                    alterTableStmt.append(table.getName());
                    alterTableStmt.append(" add primary key (");
                    Collections.sort(table.getPk().getKeys());
                    for ( PrimaryKeyItem item : table.getPk().getKeys() ) {
                        alterTableStmt.append(item.getColumn().getName());
                        alterTableStmt.append(",");
                    }
                    alterTableStmt.deleteCharAt(alterTableStmt.length() - 1);
                    alterTableStmt.append(");\n");
                    sqlBatch.add(alterTableStmt.toString());
                }
                if ( table.getUks().size() > 0 ) {
                    StringBuilder alterTableStmt = new StringBuilder();
                }
            }
            List<UniqueKey> uks = table.getUks();
            if ( uks.size() > 0 ) {
                for ( UniqueKey uk : uks ) {
                    List<UniqueKeyItem> items = uk.getItems();
                    Collections.sort(items);
                    StringBuilder stmt = new StringBuilder();
                    stmt.append("alter table ");
                    stmt.append(table.getName());
                    stmt.append(" add unique(");
                    for ( UniqueKeyItem item : items ) {
                        stmt.append(item.getColumn().getName());
                        stmt.append(",");
                    }
                    stmt.deleteCharAt(stmt.length() - 1);
                    stmt.append(");\n");
                    sqlBatch.add(stmt.toString());
                }
            }
        }
        return sqlBatch;
    }

    private void parseMetadata(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet catalogs = metaData.getCatalogs();
        ResultSet schemas = metaData.getSchemas();
        String types[] = { "TABLE" };
        // Tables
        //
        ResultSet rs = metaData.getTables(DBNAME, null, null, types);
        ResultSetMetaData rsmd = rs.getMetaData();
        int size = rsmd.getColumnCount();
        List<String> tableNames = new ArrayList<String>();
        while ( rs.next() ) {
            tableNames.add(rs.getString("TABLE_NAME"));
        }
        for ( String tName : tableNames ) {
            Table t = new Table(tName);
            tables.put(tName.toUpperCase(), t);
        }
        // Columns
        //
        for ( Table t : tables.values() ) {
            System.out.println("##################Start#####################################");
            ResultSet clrs = metaData.getColumns(DBNAME, null, t.getName(), null);
            ResultSetMetaData clrsmd = clrs.getMetaData();
            int columnCount = clrsmd.getColumnCount();
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
            ResultSet pkrs = metaData.getPrimaryKeys(DBNAME, null, t.getName());
            ResultSetMetaData pkrsmd = pkrs.getMetaData();
            String pkName = "";
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
            ResultSet indexrs = metaData.getIndexInfo(DBNAME, null, t.getName(), true, true);
            ResultSetMetaData indexmd = indexrs.getMetaData();
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
        for ( Table t : tables.values() ) {
            // Foreign keys
            //
            ResultSet fkrs = metaData.getImportedKeys(DBNAME, null, t.getName());
            ResultSetMetaData fkmd = fkrs.getMetaData();
            while ( fkrs.next() ) {
                String pkTableName = fkrs.getString("PKTABLE_NAME");
                String pkColumnName = fkrs.getString("PKCOLUMN_NAME");
                String fkTableName = fkrs.getString("FKTABLE_NAME");
                String fkColumnName = fkrs.getString("FKCOLUMN_NAME");
                String fkName = fkrs.getString("FK_NAME");
                int keySeq = fkrs.getInt("KEY_SEQ");
                Table pkTable = tables.get(pkTableName.toUpperCase());
                ForeignKeyItem fk = new ForeignKeyItem(pkTable, pkTable.getColumn(pkColumnName), t, t.getColumn(fkColumnName), fkName, keySeq);
                t.addForeignKey(fkName, fk);
            }
            System.out.println(t);
        }
    }
}
