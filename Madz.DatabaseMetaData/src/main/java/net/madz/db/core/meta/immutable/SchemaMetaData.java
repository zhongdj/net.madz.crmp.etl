package net.madz.db.core.meta.immutable;

import java.util.Collection;

import net.madz.db.core.meta.DottedPath;

public interface SchemaMetaData
                 <
                  SMD extends SchemaMetaData<?, TMD, CMD, FMD, IMD>,
                  TMD extends TableMetaData<SMD, ?, CMD, FMD, IMD>,
                  CMD extends ColumnMetaData<SMD, TMD, ?, FMD, IMD>,
                  FMD extends ForeignKeyMetaData<SMD, TMD, CMD, ?, IMD>,
                  IMD extends IndexMetaData<SMD, TMD, CMD, FMD, ?>
                 > {
    /**
     * Dotted path of the schema (catalog.schema)
     * 
     * @return DottedPath of catalogName.schemaName, or just schemaName
     *         depending on the database implementation
     */
    DottedPath getSchemaPath();

    Collection<TMD> getTables();

    /**
     * Return the meta-definition of the specified table
     * 
     * @return TableMetaData definition of table, or null if the table does
     *         not exist
     */
    TMD getTable(String name);
}
