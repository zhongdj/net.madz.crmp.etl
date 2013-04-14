package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLDecimal extends MySQLFloatTypeBase {

    public static final String name = "DECIMAL";

    public MySQLDecimal(Integer length, Integer decimals, Boolean isUnsigned, Boolean isZeroFill) {
        super(length, decimals, isUnsigned, isZeroFill);
    }

    public MySQLDecimal(Integer length, Integer decimals) {
        super(length, decimals);
    }

    @Override
    public String getName() {
        return name;
    }
}
