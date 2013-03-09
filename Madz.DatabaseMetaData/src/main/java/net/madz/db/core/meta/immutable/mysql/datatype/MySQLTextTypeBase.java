package net.madz.db.core.meta.immutable.mysql.datatype;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public abstract class MySQLTextTypeBase implements DataType {

    private boolean isBinary;
    private String charsetName;
    private String collationName;

    public boolean isBinary() {
        return isBinary;
    }

    public void setBinary(boolean isBinary) {
        this.isBinary = isBinary;
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
    public void build(MySQLColumnMetaDataBuilder builder) {
        builder.setSqlTypeName(getName());
        builder.setCharacterSet(charsetName);
        builder.setCollationName(collationName);
        builder.setCollationWithBin(isBinary);
        final StringBuilder result = new StringBuilder();
        result.append(getName());
        if ( null != charsetName ) {
            result.append(" CHARACTER SET ");
            result.append(charsetName);
        }
        if ( isBinary ) {
            result.append(" BINARY ");
        }
        if ( null != collationName ) {
            result.append(collationName);
        }
        builder.setColumnType(result.toString());
    }
}