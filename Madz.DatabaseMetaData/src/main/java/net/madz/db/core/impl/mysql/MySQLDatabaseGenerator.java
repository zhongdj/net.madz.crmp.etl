package net.madz.db.core.impl.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.AbsDatabaseGenerator;
import net.madz.db.core.meta.immutable.IndexMetaData.Entry;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.utils.MessageConsts;

public class MySQLDatabaseGenerator extends
        AbsDatabaseGenerator<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    public MySQLDatabaseGenerator() {
    }

    @Override
    public String generateDatabase(final MySQLSchemaMetaData metaData, final Connection conn, final String targetDatabaseName) throws SQLException {
        if ( null == targetDatabaseName || 0 >= targetDatabaseName.length() ) {
            throw new IllegalArgumentException("Target " + MessageConsts.DATABASE_NAME_SHOULD_NOT_BE_NULL);
        }
        GenerateDatabase(metaData, conn, targetDatabaseName);
        // Generate tables
        GenerateTables(metaData, conn, targetDatabaseName);
        return targetDatabaseName;
    }

    private void GenerateDatabase(final MySQLSchemaMetaData metaData, final Connection conn, final String targetDatabaseName) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE IF NOT EXISTS " + targetDatabaseName + " CHARACTER SET = '" + metaData.getCharSet() + "' COLLATE = '"
                + metaData.getCollation() + "';");
    }

    /**
     * @param metaData
     * @param conn
     * @param targetDatabaseName
     * @throws SQLException
     */
    private void GenerateTables(final MySQLSchemaMetaData metaData, final Connection conn, final String targetDatabaseName) throws SQLException {
        final Statement stmt = conn.createStatement();
        conn.setAutoCommit(false);
        stmt.executeUpdate("USE `" + targetDatabaseName + "`");
        for ( final MySQLTableMetaData table : metaData.getTables() ) {
            final StringBuilder result = new StringBuilder();
            result.append("CREATE TABLE IF NOT EXISTS`");
            result.append(table.getTableName());
            result.append("` (");
            for ( final MySQLColumnMetaData column : table.getColumns() ) {
                appendBackQuotation(result);
                result.append(column.getColumnName());
                appendBackQuotation(result);
                appendSpace(result);
                result.append(column.getSqlTypeName());
                appendSpace(result);
                if ( column.isNullable() ) {
                    result.append(" NULL ");
                } else {
                    result.append(" NOT NULL ");
                }
                if ( column.hasDefaultValue() ) {
                    result.append(" DEFAULT ");
                    result.append(column.getDefaultValue());
                }
                if ( column.isAutoIncremented() ) {
                    result.append(" AUTO_INCREMENT ");
                }
                if ( column.isMemberOfUniqueIndex() ) {
                    result.append(" UNIQUE ");
                }
                if ( null != column.getRemarks() && 0 > column.getRemarks().length() ) {
                    result.append(" COMMENT ");
                    result.append(column.getRemarks());
                    appendSpace(result);
                }
                if ( null != column.getCharacterSet() ) {
                    result.append(" CHARACTER SET ");
                    result.append(column.getCharacterSet());
                    appendSpace(result);
                }
                if ( null != column.getCollationName() ) {
                    result.append(" COLLATE ");
                    result.append(column.getCollationName());
                }
                result.append(",");
            }
            result.deleteCharAt(result.length() - 1);
            // Append primary keys
            final MySQLIndexMetaData pk = table.getPrimaryKey();
            if ( null != pk ) {
                Collection<Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entrySet = pk
                        .getEntrySet();
                if ( entrySet.size() > 0 ) {
                    result.append(", PRIMARY KEY(");
                    for ( Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : entrySet ) {
                        MySQLColumnMetaData column = entry.getColumn();
                        appendBackQuotation(result);
                        result.append(column.getColumnName());
                        appendBackQuotation(result);
                        result.append(",");
                    }
                    result.deleteCharAt(result.length() - 1);
                    result.append(")");
                }
            }
            result.append(") ");
            if ( null != table.getEngine() && 0 >= table.getEngine().name().length() ) {
                result.append("ENGINE='");
                result.append(table.getEngine());
                appendBackQuotation(result);
                appendSpace(result);
            }
            if ( null != table.getCharacterSet() ) {
                result.append("CHARACTER SET = `");
                result.append(table.getCharacterSet());
                appendBackQuotation(result);
                appendSpace(result);
            }
            if ( null != table.getCollation() ) {
                result.append("COLLATE = `");
                result.append(table.getCollation());
                appendBackQuotation(result);
                appendSpace(result);
            }
            System.out.println(result.toString());
            stmt.addBatch(result.toString());
        }
        stmt.executeBatch();
        conn.commit();
    }

    public void appendSpace(final StringBuilder result) {
        result.append(" ");
    }

    public void appendBackQuotation(final StringBuilder result) {
        result.append("`");
    }
}
