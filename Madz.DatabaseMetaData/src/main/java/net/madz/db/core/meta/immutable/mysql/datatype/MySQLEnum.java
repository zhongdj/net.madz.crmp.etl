package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLEnum extends MySQLCollectionTypeBase {

    public static final String name = "ENUM";

    @Override
    public String getName() {
        return name;
    }
}
