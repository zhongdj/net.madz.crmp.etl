package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLSmallInt extends MySQLIntTypeBase {

    public static final String name = "SMALLINT";

    public MySQLSmallInt() {
    }

    public MySQLSmallInt(Short displayLength, Boolean isUnsigned, Boolean isZeroFill) {
        super(displayLength, isUnsigned, isZeroFill);
    }

    public MySQLSmallInt(Short displayLength) {
        super(displayLength);
    }

    public MySQLSmallInt(Short displayLength, Boolean isUnsigned) {
        super(displayLength, isUnsigned);
    }

    @Override
    public String getName() {
        return name;
    }
}
