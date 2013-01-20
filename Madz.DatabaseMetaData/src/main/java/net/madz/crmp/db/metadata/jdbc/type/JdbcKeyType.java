package net.madz.crmp.db.metadata.jdbc.type;

public enum JdbcKeyType {
    primaryKey,
    uniqueKey,
    index, ;

    public boolean isUnique() {
        return this != index;
    }
}
