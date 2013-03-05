package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.utils.Utilities;

public class MySQLChar extends MySQLCharBase {

    public static final String name = "CHAR";
    private final long length;

    public MySQLChar() {
        length = 1; // default value
    }

    public MySQLChar(long length) {
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
