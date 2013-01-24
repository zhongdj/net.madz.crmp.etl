package net.madz.db.core.meta.immutable.impl.enums;


public enum ColumnDbMetaDataEnum {
    /** String => table catalog (may be null) */
    TABLE_CAT,
    /** String => table schema (may be null) */
    TABLE_SCHEM,
    /** String => table name */
    TABLE_NAME,
    /**  String => column name */
    COLUMN_NAME,
    /** int => SQL type from java.sql.Types */
    DATA_TYPE,
    /** String => Data source dependent type name, for a UDT the type name is fully qualified */
    TYPE_NAME,
    /**int => column size. */
    COLUMN_SIZE,
    /** is not used.*/
    BUFFER_LENGTH,
    /** int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable. */
    DECIMAL_DIGITS ,
    /** int => Radix (typically either 10 or 2) */
    NUM_PREC_RADIX ,
    /** int => is NULL allowed.
    columnNoNulls - might not allow NULL values
    columnNullable - definitely allows NULL values
    columnNullableUnknown - nullability unknown
     */
    NULLABLE ,
    /** String => comment describing column (may be null) */
    REMARKS,
    /**  String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null) */
    COLUMN_DEF,
    /** int => unused */
    SQL_DATA_TYPE ,
    /**int => unused */
    SQL_DATETIME_SUB, 
    /** int => for char types the maximum number of bytes in the column */
    CHAR_OCTET_LENGTH ,
    /** int => index of column in table (starting at 1) */
    ORDINAL_POSITION ,
    /** String => ISO rules are used to determine the nullability for a column.
    YES --- if the parameter can include NULLs
    NO --- if the parameter cannot include NULLs
    empty string --- if the nullability for the parameter is unknown*/
    IS_NULLABLE ,
    /** String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) */
    SCOPE_CATLOG ,
    /**String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF) */
    SCOPE_SCHEMA, 
    /** String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)*/
    SCOPE_TABLE,
     /** short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) */
    SOURCE_DATA_TYPE, 
    /**String => Indicates whether this column is auto incremented
    YES --- if the column is auto incremented
    NO --- if the column is not auto incremented
    empty string --- if it cannot be determined whether the column is auto incremented parameter is unknown */
    IS_AUTOINCREMENT  
   }
