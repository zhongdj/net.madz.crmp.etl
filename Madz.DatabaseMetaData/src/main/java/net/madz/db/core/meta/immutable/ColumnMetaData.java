package net.madz.db.core.meta.immutable;

import java.util.Collection;
import java.util.Comparator;

import net.madz.db.core.meta.DottedPath;

public interface ColumnMetaData<
SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>,
TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>,
CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>,
FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>,
IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>
>  {
    /** Dotted path of this column (catalog.schema.table.column) */
    DottedPath getColumnPath();

    /** parent object */
    TMD getTableMetaData();

    String getColumnName();

    /** Sql Type from java.sql.Types */
    Integer getSqlType();

    /**
     * Data source dependent type name, for a UDT the type name is fully
     * qualified
     */
    String getSqlTypeName();

    /** Column size */
    Integer getSize();

    /** Radix (typically either 10 or 2) */
    Integer getRadix();

    String getRemarks();

    boolean hasDefaultValue();

    String getDefaultValue();

    Integer getCharOctetLength();

    /** Position of this column in physical table layout (first column=1, ...) */
    Short getOrdinalPosition();

    boolean isNullable();

    boolean isAutoIncremented();

    /**
     * Primary key definition utilizing this column
     * 
     * @return IndexMetaData.Entry primary key entry utilizing this column,
     *         or null if this column is not part of the primary key
     */
    IMD.Entry<IMD, CMD> getPrimaryKey();

    /** Does the table's primary key include this column? */
    boolean isMemberOfPrimaryKey();

    /** Is this column a member of any index on the table */
    boolean isMemberOfIndex();

    boolean isMemberOfForeignKey(FMD fk);

    /** All unique indices that include this column */
    Collection<IMD.Entry<IMD, CMD>> getUniqueIndexSet();

    /** Is this column a member of a unique index? */
    boolean isMemberOfUniqueIndex();

    /** All non-unique indices that include this column */
    Collection<IMD.Entry<IMD, CMD>> getNonUniqueIndexSet();

    /**
     * Sorts ColumnMetaData values by ordinal position
     * 
     * @see #getOrdinalPosition()
     */
    @SuppressWarnings("rawtypes")
	public final static Comparator<ColumnMetaData> ORDINAL_COMPARATOR = new Comparator<ColumnMetaData>() {

        @Override
        public int compare(ColumnMetaData o1, ColumnMetaData o2) {
            return o1.getOrdinalPosition().compareTo(o2.getOrdinalPosition());
        }
    };
}
