package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLSmallInt extends MySQLIntTypeBase {

    public static final String name = "SMALLINT";

    public MySQLSmallInt(Short displayLength, boolean isUnsigned, boolean isZeroFill) {
        super(displayLength, isUnsigned, isZeroFill);
    }

    public MySQLSmallInt(Short displayLength) {
        super(displayLength);
    }

    public MySQLSmallInt(Short displayLength, boolean isUnsigned) {
        super(displayLength, isUnsigned);
    }

    @Override
    public String getName() {
        return name;
    }
}
