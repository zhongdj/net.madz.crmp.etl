package net.madz.db.core.meta.immutable.mysql.datatype;


public class MySQLMediumBlob extends MySQLBlobTypeBase {

    public static final String name = "MEDIUMBLOB";

    @Override
    public String getName() {
        return name;
    }
}
