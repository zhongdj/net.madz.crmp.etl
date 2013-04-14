package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.utils.Utilities;

public class MySQLBinary extends MySQLBinaryTypeBase {

    public static final String name = "BINARY";
    /** same with CHARACTER_MAXIMUM_LENGTH, CHARACTER_OCTET_LENGTH */
    private final Long length;

    public MySQLBinary() {
        length = 1L;
    }

    public MySQLBinary(Long length) {
        super();
        Utilities.validateLength(length);
        this.length = length;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getLength() {
        return length;
    }
}
