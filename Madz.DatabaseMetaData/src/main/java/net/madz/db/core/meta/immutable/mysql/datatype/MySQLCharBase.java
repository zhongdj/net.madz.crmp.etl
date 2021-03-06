package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.utils.Utilities;

public abstract class MySQLCharBase implements DataType {

    private final Long length;
    private final String charsetName;
    private final String collationName;

    public MySQLCharBase(Long length) {
        this(length, null, null);
    }

    public MySQLCharBase(Long length, String charsetName, String collationName) {
        super();
        Utilities.validateLength(length);
        this.length = length;
        this.charsetName = charsetName;
        this.collationName = collationName;
    }

    public Long getLength() {
        return length;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public String getCollationName() {
        return collationName;
    }

    @Override
    public void build(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(getName());
        builder.setCharacterMaximumLength(getLength());
        builder.setCharacterSet(this.charsetName);
        builder.setCollationName(this.collationName);
        final StringBuilder result = new StringBuilder();
        result.append(getName());
        if ( 0 < getLength() ) {
            result.append("(");
            result.append(getLength());
            result.append(")");
        }
        if ( null != charsetName && 0 < charsetName.length() ) {
            result.append(" CHARACTER SET ");
            result.append(charsetName);
        }
        if ( null != collationName && 0 < collationName.length() ) {
            result.append(" COLLATE ");
            result.append(collationName);
        }
        builder.setColumnType(result.toString());
    }
}
