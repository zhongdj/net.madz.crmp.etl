package net.madz.db.core.meta.immutable.types;

public enum KeyTypeEnum {
    primaryKey,
    uniqueKey,
    index, ;

    public boolean isUnique() {
        return this != index;
    }
}
