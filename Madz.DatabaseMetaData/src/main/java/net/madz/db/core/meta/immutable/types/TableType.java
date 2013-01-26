package net.madz.db.core.meta.immutable.types;

import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;

public enum TableType {
    table,
    view,
    system_table,
    global_temporary,
    local_temporary,
    alias,
    synonym;

    private final String jdbcValue;

    private TableType() {
        this.jdbcValue = this.name().replaceAll("_", " ").toUpperCase();
    }

    public String getJdbcValue() {
        return this.jdbcValue;
    }

    public final static TableType getTableType(String jdbcValue) {
        if ( null == jdbcValue ) {
            return null;
        }
        return valueOf(jdbcValue.replaceAll(" ", "_").toLowerCase());
    }

    public final static TableType convertTableType(MySQLTableTypeEnum jdbcValue) {
        TableType result = null;
        switch (jdbcValue) {
        case base_table:
            result = TableType.table;
            break;
        case system_table:
            result = TableType.table;
            break;
        case view:
            result = TableType.view;
            break;
        }
        return result;
    }
}
