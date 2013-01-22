package net.madz.db.metadata.mysql;

public enum MySQLColumnTypeEnum {
    BIT,
    BOOL,
    TINYINT,
    TINYINT_UNSIGNED,
    BIGINT,
    BIGINT_UNSIGNED,
    LONG_VARBINARY,
    MEDIUMBLOB,
    LONGBLOB,
    BLOB,
    TINYBLOB,
    VARBINARY,
    BINARY,
    LONG_VARCHAR,
    MEDIUMTEXT,
    LONGTEXT,
    TEXT,
    TINYTEXT,
    CHAR,
    NUMERIC,
    DECIMAL,
    INTEGER,
    INTEGER_UNSIGNED,
    INT,
    INT_UNSIGNED,
    MEDIUMINT,
    MEDIUMINT_UNSIGNED,
    SMALLINT,
    SMALLINT_UNSIGNED,
    FLOAT,
    DOUBLE,
    DOUBLE_PRECISION,
    REAL,
    VARCHAR,
    ENUM,
    SET,
    DATE,
    TIME,
    DATETIME,
    TIMESTAMP;

    public static MySQLColumnTypeEnum getMySQLColumnType(String type) {
        if ( null == type ) {
            throw new IllegalArgumentException("The type " + type + "is not supported.");
        }
        return valueOf(type.toUpperCase());
    }
}
