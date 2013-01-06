package net.madz.crmp.db.metadata;

public enum JdbcKeyType {
    primarykey,
    uniqueKey,
    index, ;

    public boolean isUnique() {
        return this != index;
    }
}
