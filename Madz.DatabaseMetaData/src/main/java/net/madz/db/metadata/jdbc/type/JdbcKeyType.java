package net.madz.db.metadata.jdbc.type;

public enum JdbcKeyType {
    primaryKey,
    uniqueKey,
    index, ;

    public boolean isUnique() {
        return this != index;
    }
}
