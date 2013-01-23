package net.madz.db.core.meta.immutable;

import java.util.Collection;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.type.TableType;

public interface TableMetaData <
SMD extends SchemaMetaData<?, TMD, CMD, FMD, IMD>,
TMD extends TableMetaData<SMD, ?, CMD, FMD, IMD>,
CMD extends ColumnMetaData<SMD, TMD, ?, FMD, IMD>,
FMD extends ForeignKeyMetaData<SMD, TMD, CMD, ?, IMD>,
IMD extends IndexMetaData<SMD, TMD, CMD, FMD, ?>
> {

    /** DottedPath of this table (catalog.schema.table) */
    DottedPath getTablePath();

    /** Table name */
    String getTableName();

    TableType getType();

    /** Remarks for table */
    String getRemarks();

    /** Column self_referencing_col_name */
    String getIdCol();

    /** Column ref_generation */
    String getIdGeneration();

    /** Primary key */
    IMD getPrimaryKey();

    CMD getColumn(String columnName);

    List<CMD> getColumns();

    Collection<FMD> getForeignKeySet();

    Collection<IMD> getIndexSet();

    IMD getIndex(String indexName);
}
