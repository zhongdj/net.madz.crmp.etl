package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public abstract class MySQLCharBase implements DataType {

    private String charsetName;
    private String collationName;

    public abstract long getLength();

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getCollationName() {
        return collationName;
    }

    public void setCollationName(String collationName) {
        this.collationName = collationName;
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
        result.append(" CHARACTER SET ");
        result.append(charsetName);
        result.append(" COLLATE ");
        result.append(collationName);
        builder.setColumnType(result.toString());
    }
}
