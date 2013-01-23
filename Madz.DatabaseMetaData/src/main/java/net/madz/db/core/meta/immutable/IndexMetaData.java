package net.madz.db.core.meta.immutable;

import java.util.Collection;

import net.madz.db.core.meta.immutable.type.IndexType;
import net.madz.db.core.meta.immutable.type.KeyType;
import net.madz.db.core.meta.immutable.type.SortDirection;

public interface IndexMetaData <
SMD extends SchemaMetaData<?, TMD, CMD, FMD, IMD>,
TMD extends TableMetaData<SMD, ?, CMD, FMD, IMD>,
CMD extends ColumnMetaData<SMD, TMD, ?, FMD, IMD>,
FMD extends ForeignKeyMetaData<SMD, TMD, CMD, ?, IMD>,
IMD extends IndexMetaData<SMD, TMD, CMD, FMD, ?>
>  {

    public interface Entry<I, C> {

        /** Index this entry belongs to */
        public I getKey();

        /** Column definition */
        public C getColumn();

        /** Column position in index */
        public Integer getPosition();
    }

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
    Collection<IMD.Entry<IMD, CMD>> getEntrySet();

    TMD getTable();
}
