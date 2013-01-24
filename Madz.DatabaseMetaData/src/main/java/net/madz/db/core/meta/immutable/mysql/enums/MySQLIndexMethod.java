package net.madz.db.core.meta.immutable.mysql.enums;

public enum MySQLIndexMethod {
    btree,
    fulltext,
    hash,
    rtree;

    public static MySQLIndexMethod getIndexMethod(String index) {
        return valueOf(index.toLowerCase());
    }
}
