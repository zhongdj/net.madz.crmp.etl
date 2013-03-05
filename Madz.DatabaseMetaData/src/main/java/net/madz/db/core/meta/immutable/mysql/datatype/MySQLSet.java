package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLSet extends MySQLCollectionTypeBase {

    public static final String name = "SET";

    @Override
    public String getName() {
        return name;
    }
}
