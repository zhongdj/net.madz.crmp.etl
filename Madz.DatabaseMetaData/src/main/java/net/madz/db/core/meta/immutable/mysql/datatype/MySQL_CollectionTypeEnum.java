package net.madz.db.core.meta.immutable.mysql.datatype;

import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;

public enum MySQL_CollectionTypeEnum implements DataType {
    ENUM,
    SET;

    private List<String> values;
    private String charsetName;
    private String collationName;

    public List<String> getValues() {
        return values;
    }

    public void addValue(String value) {
        if ( null == value ) {
            return;
        }
        if ( null == values ) {
            values = new LinkedList<String>();
        }
        values.add(value);
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
        final StringBuilder result = new StringBuilder();
        result.append(this.name());
        result.append("(");
        for ( String value : values ) {
            builder.addTypeValue(value);
            result.append(value);
            result.append(",");
        }
        result.deleteCharAt(result.length() - 1);
        result.append(")");
        builder.setColumnType(result.toString());
    }
}
