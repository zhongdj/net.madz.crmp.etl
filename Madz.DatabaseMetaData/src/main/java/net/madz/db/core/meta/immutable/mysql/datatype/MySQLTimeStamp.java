package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLTimeStamp extends MySQLTimeTypeBase {

    public static final String name = "TIMESTAMP";

    @Override
    public String getName() {
        return name;
    }
}
