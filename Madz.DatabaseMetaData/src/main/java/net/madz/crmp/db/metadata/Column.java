package net.madz.crmp.db.metadata;

public class Column {

    private String name = null;
    private int dataType = 0;
    private int columnSize = 0;
    private int nullable = 0;
    private int sqlDataType = 0;
    private String typeName;
    private String defaultValue;
    private String marks;
    private boolean isAutoIncrement;

    public void setName(String name) {
        this.name = name;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    public void setSqlDataType(int sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setRemarks(String marks) {
        this.marks = marks;
    }

    public void setAutoIncrement(String value) {
        if ( "Yes" == value ) {
            this.isAutoIncrement = true;
        } else {
            this.isAutoIncrement = false;
        }
    }

    public String getName() {
        return name;
    }

    public int getDataType() {
        return dataType;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getNullable() {
        return nullable;
    }

    public int getSqlDataType() {
        return sqlDataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getMarks() {
        return marks;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    @Override
    public String toString() {
        return "Column [name=" + name + ", dataType=" + dataType + ", columnSize=" + columnSize + ", nullable=" + nullable + ", sqlDataType=" + sqlDataType
                + ", typeName=" + typeName + ", defaultValue=" + defaultValue + ", marks=" + marks + ", isAutoIncrement=" + isAutoIncrement + "]";
    }
}
