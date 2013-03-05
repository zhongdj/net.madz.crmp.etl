package net.madz.db.core.meta.immutable.mysql.datatype;


public class MySQLBlob extends MySQLBlobTypeBase {

    public static final String name = "BLOB";

    @Override
    public String getName() {
        return name;
    }
}
