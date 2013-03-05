package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.utils.Utilities;

public class MySQLBinary extends MySQLBinaryTypeBase {

    public static final String name = "BINARY";
    /** same with CHARACTER_MAXIMUM_LENGTH, CHARACTER_OCTET_LENGTH */
    private final long length;

    public MySQLBinary() {
        length = 1;
    }

    public MySQLBinary(long length) {
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
