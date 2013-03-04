package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public enum MySQL_CharTypeEnum implements DataType {
    CHAR,
    VARCHAR;

    private long length;
    private String charsetName;
    private String collationName;

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

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
    public void setColumnBuilder(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(this.name());
        builder.setCharacterMaximumLength(length);
        builder.setCharacterSet(this.charsetName);
        builder.setCollationName(this.collationName);
        final StringBuilder result = new StringBuilder();
        result.append(this.name());
        result.append("(");
        result.append(length);
        result.append(")");
        result.append(" CHARACTER SET ");
        result.append(charsetName);
        result.append(" COLLATE ");
        result.append(collationName);
        builder.setColumnType(result.toString());
    }
}
