package net.madz.crmp.db.core.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.madz.crmp.db.core.AbsDatabaseGenerator;
import net.madz.crmp.db.metadata.Column;
import net.madz.crmp.db.metadata.PrimaryKeyItem;
import net.madz.crmp.db.metadata.SchemaMetaData;
import net.madz.crmp.db.metadata.Table;
import net.madz.crmp.db.metadata.UniqueKey;
import net.madz.crmp.db.metadata.UniqueKeyItem;

public class MySQLDatabaseGenerator extends AbsDatabaseGenerator {

    public MySQLDatabaseGenerator(Connection conn) {
        super(conn);
    }

    @Override
    public String generateDatabase(SchemaMetaData metadata, String targetDatabaseName) {
        this.schemaMetaData = metadata;
        createEmptyDatabase();
        List<String> sqlBatch = generateCreateSQL();
        submitSqlBatch(sqlBatch);
        return null;
    }

    private void createEmptyDatabase() {
    }

    private List<String> generateCreateSQL() {
        List<String> sqlBatch = new LinkedList<String>();
        for ( Table table : schemaMetaData.getTables() ) {
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

    private void submitSqlBatch(List<String> sqlBatch) {
        Connection targetDBConn = null;
        try {
            targetDBConn = DbConfigurationManagement.createConnection(schemaMetaData.getDatabaseName() + "_Copy", true);
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
}
