package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.utils.Utilities;

public class MySQLVarchar extends MySQLCharBase {

    public static final String name = "VARCHAR";
    private final long length;

    public MySQLVarchar(long length) {
        super();
        Utilities.validateLength(length);
        this.length = length;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getLength() {
        return length;
    }
}
