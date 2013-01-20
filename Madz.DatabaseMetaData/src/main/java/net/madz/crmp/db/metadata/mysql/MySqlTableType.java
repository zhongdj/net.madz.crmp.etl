package net.madz.crmp.db.metadata.mysql;

/** The table types that MySql supports */
public enum MySqlTableType {
    SystemTable,
    BaseTable,
    View;

    public static MySqlTableType getType(String typeName) {
        String result = typeName.replaceAll(" ", "");
        MySqlTableType type = MySqlTableType.valueOf(result);
        if ( null == type ) {
            throw new IllegalArgumentException("type " + typeName + "is not supported");
        }
        return type;
    }
}
