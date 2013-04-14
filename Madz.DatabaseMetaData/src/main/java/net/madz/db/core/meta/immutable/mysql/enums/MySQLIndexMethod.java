package net.madz.db.core.meta.immutable.mysql.enums;

import net.madz.db.core.meta.immutable.types.IndexTypeEnum;

public enum MySQLIndexMethod {
    btree,
    fulltext,
    hash,
    rtree;

    public static MySQLIndexMethod getIndexMethod(String index) {
        return valueOf(index.toLowerCase());
    }

    // [TODO] We assume that Access implement index via btree. Need to confirm.
    public static MySQLIndexMethod getIndexMethod(IndexTypeEnum indexType) {
        return btree;
    }
}
