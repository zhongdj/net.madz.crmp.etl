package net.madz.db.metadata.jdbc;

import java.util.Collection;

import net.madz.db.metadata.DottedPath;

public interface JdbcSchemaMetaData extends JdbcMetaData {

    /**
     * Dotted path of the schema (catalog.schema)
     * 
     * @return DottedPath of catalogName.schemaName, or just schemaName
     *         depending on the database implementation
     */
    DottedPath getSchemaPath();

    Collection<? extends JdbcTableMetaData> getTables();

    /**
     * Return the meta-definition of the specified table
     * 
     * @return JdbcTableMetaData definition of table, or null if the table does
     *         not exist
     */
    <DB_DEPENDENT_TABLE_META extends JdbcTableMetaData> DB_DEPENDENT_TABLE_META getTable(String name);
}
