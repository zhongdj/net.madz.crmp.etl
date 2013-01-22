package net.madz.db.metadata.mysql;

/** The table types that MySql supports */
public enum MySQLTableTypeEnum {
    system_table,
    base_table,
    view;

    public final static MySQLTableTypeEnum getType(String typeName) {
        if ( null == typeName ) {
            return null;
        }
        String result = typeName.replaceAll(" ", "_").toLowerCase();
        return valueOf(result);
    }
    
}
