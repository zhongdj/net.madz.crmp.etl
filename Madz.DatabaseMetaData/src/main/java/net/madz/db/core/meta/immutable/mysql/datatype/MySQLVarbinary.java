package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.utils.Utilities;

public class MySQLVarbinary extends MySQLBinaryTypeBase {

    public static final String name = "VARBINARY";
    /** same with CHARACTER_MAXIMUM_LENGTH, CHARACTER_OCTET_LENGTH */
    private final Long length;

    public MySQLVarbinary(Long length) {
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
