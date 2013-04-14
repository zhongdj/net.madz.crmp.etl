package net.madz.db.core.meta.immutable.mysql.datatype;


public class MySQLTinyInt extends MySQLIntTypeBase {

    public static final String name = "TINYINT";

    public MySQLTinyInt() {
        super();
    }

    public MySQLTinyInt(Short displayLength, Boolean isUnsigned, Boolean isZeroFill) {
        super(displayLength, isUnsigned, isZeroFill);
    }

    public MySQLTinyInt(Short displayLength) {
        super(displayLength);
    }

    public MySQLTinyInt(Short displayLength, Boolean isUnsigned) {
        super(displayLength, isUnsigned);
    }

    @Override
    public String getName() {
        return name;
    }
}
