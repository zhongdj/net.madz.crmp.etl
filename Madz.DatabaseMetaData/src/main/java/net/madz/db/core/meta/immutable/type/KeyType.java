package net.madz.db.core.meta.immutable.type;

public enum KeyType {
    primaryKey,
    uniqueKey,
    index, ;

    public boolean isUnique() {
        return this != index;
    }
}
