package net.madz.db.core.meta.immutable;

import java.util.Collection;

import net.madz.db.core.meta.immutable.type.IndexType;
import net.madz.db.core.meta.immutable.type.KeyType;
import net.madz.db.core.meta.immutable.type.SortDirection;

public interface IndexMetaData<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>> 
extends MetaData {

    /** Name of the index */
    String getIndexName();

    /** Is this a unique index */
    boolean isUnique();

    /** KeyType of index */
    KeyType getKeyType();

    /** Type of the index */
    IndexType getIndexType();

    /** Ascending/Descending order */
    SortDirection getSortDirection();

    /** Index cardinality, if known */
    Integer getCardinality();

    /** Number of pages used by the index, if known */
    Integer getPageCount();

    /** Does this index use the specified column? */
    boolean containsColumn(CMD column);

    /** All columns in index */
    Collection<Entry<SMD, TMD, CMD, FMD, IMD>> getEntrySet();

    TMD getTable();

    public interface Entry<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>> {

        /** Index this entry belongs to */
        public IMD getKey();

        /** Column definition */
        public CMD getColumn();

        /** Column position in index */
        public Integer getPosition();
    }
}
