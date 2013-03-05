package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLTinyBlob extends MySQLBlobTypeBase {

    public static final String name = "TINYBLOB";

    @Override
    public String getName() {
        return name;
    }
}
