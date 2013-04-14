package net.madz.db.core.meta.immutable.mysql.datatype;

public class MySQLNumeric extends MySQLFloatTypeBase {

    public static final String name = "NUMERIC";

    public MySQLNumeric(Integer length, Integer decimals, Boolean isUnsigned, Boolean isZeroFill) {
        super(length, decimals, isUnsigned, isZeroFill);
    }

    public MySQLNumeric(Integer length, Integer decimals) {
        super(length, decimals);
    }

    @Override
    public String getName() {
        return name;
    }
}
