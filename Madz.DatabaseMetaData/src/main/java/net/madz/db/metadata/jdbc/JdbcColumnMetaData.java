package net.madz.db.metadata.jdbc;

import java.util.Collection;
import java.util.Comparator;

import net.madz.db.metadata.DottedPath;

/**
 * JDBC DatabaseMetaData definition of a column
 */
public interface JdbcColumnMetaData extends JdbcMetaData {

    /** Dotted path of this column (catalog.schema.table.column) */
    DottedPath getColumnPath();

    /** parent object */
    JdbcTableMetaData getTableMetaData();

    String getColumnName();

    /** Sql Type from java.sql.Types */
    Integer getSqlType();

    /**
     * Data source dependent type name, for a UDT the type name is fully
     * qualified
     */
    String getSqlTypeName();

    boolean isNullable();

    /**
     * Primary key definition utilizing this column
     * 
     * @return JdbcIndexMetaData.Entry primary key entry utilizing this column,
     *         or null if this column is not part of the primary key
     */
    JdbcIndexMetaData.Entry getPrimaryKey();

    /** Does the table's primary key include this column? */
    boolean isMemberOfPrimaryKey();

    /** Is this column a member of any index on the table */
    boolean isMemberOfIndex();

    boolean isMemberOfForeignKey(JdbcForeignKeyMetaData fk);

    /** All unique indices that include this column */
    Collection<? extends JdbcIndexMetaData.Entry> getUniqueIndexSet();

    /** Is this column a member of a unique index? */
    boolean isMemberOfUniqueIndex();

    /** All non-unique indices that include this column */
    Collection<? extends JdbcIndexMetaData.Entry> getNonUniqueIndexSet();

    /** Radix (typically either 10 or 2) */
    Integer getRadix();

    boolean isAutoIncremented();

    Integer getSize();

    /** Position of this column in physical table layout (first column=1, ...) */
    Short getOrdinalPosition();

    boolean hasDefaultValue();

    /**
     * Sorts JdbcColumnMetaData values by ordinal position
     * 
     * @see #getOrdinalPosition()
     */
    public final static Comparator<JdbcColumnMetaData> ORDINAL_COMPARATOR = new Comparator<JdbcColumnMetaData>() {

        @Override
        public int compare(JdbcColumnMetaData o1, JdbcColumnMetaData o2) {
            return o1.getOrdinalPosition().compareTo(o2.getOrdinalPosition());
        }
    };

    Integer getCharOctetLength();

    String getRemarks();

    String getDefaultValue();
}
