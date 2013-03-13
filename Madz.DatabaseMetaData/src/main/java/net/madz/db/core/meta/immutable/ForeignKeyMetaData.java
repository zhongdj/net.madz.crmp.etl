package net.madz.db.core.meta.immutable;

import java.util.List;

import net.madz.db.core.meta.immutable.types.CascadeRule;
import net.madz.db.core.meta.immutable.types.KeyDeferrability;

public interface ForeignKeyMetaData<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        extends MetaData {

    /** The name of foreign key */
    String getForeignKeyName();

    /**
     * Foreign key index
     * 
     * This is the index that is referring to another table
     * 
     * @return SqlIndexMetaData or null if there is no foreign key index; MySQL
     *         appears to always give us a value
     */
    IMD getForeignKeyIndex();

    TMD getForeignKeyTable();

    /**
     * primary key index
     * 
     * This is the index that being referred to
     * 
     * @return SqlIndexMetaData or null if there is no primary key index; MySQL
     *         appears to always give us null
     */
    IMD getPrimaryKeyIndex();

    TMD getPrimaryKeyTable();

    /**
     * Defines what is to happen to this record when the referenced record is
     * deleted.
     */
    CascadeRule getDeleteCascadeRule();

    /**
     * Defines what is to happen to this record when the referenced record's
     * primary key is updated.
     */
    CascadeRule getUpdateCascadeRule();

    /**
     * Deferrability rule on this key
     */
    KeyDeferrability getKeyDeferrability();

    List<Entry<SMD, TMD, CMD, FMD, IMD>> getEntrySet();

    /**
     * Number of relationships in the key
     */
    Integer size();

    public interface Entry<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>> {

        CMD getForeignKeyColumn();

        CMD getPrimaryKeyColumn();

        FMD getKey();

        Short getSeq();
    }
}
