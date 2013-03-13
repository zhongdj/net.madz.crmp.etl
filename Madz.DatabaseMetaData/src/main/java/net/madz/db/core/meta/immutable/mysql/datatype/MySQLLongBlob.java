package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLLongBlob extends MySQLBlobTypeBase {

    public static final String name = "LONGBLOB";

    @Override
    public String getName() {
        return name;
    }
}
