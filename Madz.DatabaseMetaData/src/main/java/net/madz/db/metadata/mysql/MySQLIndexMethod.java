package net.madz.db.metadata.mysql;

public enum MySQLIndexMethod {
    btree,
    fulltext,
    hash,
    rtree;

    public static MySQLIndexMethod getIndexMethod(String index) {
        return valueOf(index.toLowerCase());
    }
}
