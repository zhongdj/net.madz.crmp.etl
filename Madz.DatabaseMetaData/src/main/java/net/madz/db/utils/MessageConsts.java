package net.madz.db.utils;

public class MessageConsts {

    public static final String ARGUMENT_SHOULD_NOT_BE_NULL = "Argument should not be null, please check.";
    public static final String CONFIGURE_DATABASE_INFO = "Please make sure configure souce database information.";
    public static final String ONLY_ONE_SCHEMA_INFORMATION_IS_OK = "Only one schema information should be returned. Please check your SQL statement.";
    public static final String DATABASE_NAME_SHOULD_NOT_BE_NULL = "Database name should not be empty.";
    public static final String FK_INDEX_SHOULD_NOT_BE_NULL = "Foreign key index should not be null, please contact developer.";
    public static final String FK_INDEX_SHOULD_CONTAINS_ENTRIES = "Foreign key index should contain at least one entry, please check.";
    public static final String SQL_TYPE_NAME_IS_NULL = "SQL type name should not be null, please check.";
    public static final String LENGTH_MUST_BE_GREATER_THAN_ZERO = "For data type, length must be greater than 0.";
    public static final String COLLECTION_DATA_TYPE_SHOULD_NOT_BE_NULL = "For set or enum, data type values should not be null.";
    public static final String LOWER_CASE_TABLE_NAMES_MUST_BE_SAME = "System variable 'lower_case_table_names' of source server and target server must be the same, please configure correctly.";
    public static final String DATABASE_NOT_EXISTS_IN_DB_SERVER = "Database doesn't exists on server, please check!";
    public static final String THE_ERROR_CODE_INVALID = "The error code is invalid.";
    public static final String THE_DATA_TYPE_NOT_SUPPORT = "The data type of jdbc is not supported to converted to mysql data type, the data type is:";
    public static final String DATA_TYPE_NEEDS_DEFAULT_NO_ARGUMENT_CONSTRUCTOR = "The data type needs default no argument constructor, the data type is:";
}
