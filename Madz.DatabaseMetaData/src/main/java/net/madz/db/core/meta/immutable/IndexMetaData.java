package net.madz.db.core.meta.immutable;

import java.util.Collection;

import net.madz.db.core.meta.immutable.types.IndexTypeEnum;
import net.madz.db.core.meta.immutable.types.KeyTypeEnum;
import net.madz.db.core.meta.immutable.types.SortDirectionEnum;

public interface IndexMetaData<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        extends MetaData {

    /** Name of the index */
    String getIndexName();

    /** Is this a unique index */
    boolean isUnique();

    /** KeyType of index */
    KeyTypeEnum getKeyType();

    /** Type of the index */
    IndexTypeEnum getIndexType();

    /** Ascending/Descending order */
    SortDirectionEnum getSortDirection();

    /** Index cardinality, if known */
    Integer getCardinality();

    /** Number of pages used by the index, if known */
    Integer getPageCount();

    /** Does this index use the specified column? */
    boolean containsColumn(CMD column);

    /** All columns in index */
    Collection<IndexEntry<SMD, TMD, CMD, FMD, IMD>> getEntrySet();

    TMD getTable();
}
