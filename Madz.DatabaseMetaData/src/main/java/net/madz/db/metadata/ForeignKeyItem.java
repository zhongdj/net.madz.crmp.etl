package net.madz.db.metadata;

public class ForeignKeyItem implements Comparable {

    private Table pkTable;
    private Column pkColumn;
    private Table fkTable;
    private Column fkColumn;
    private String fkName;
    private int seq;

    public ForeignKeyItem(Table pkTable, Column pkColumn, Table fkTable, Column fkColumn, String fkName, int seq) {
        this.pkTable = pkTable;
        this.pkColumn = pkColumn;
        this.fkTable = fkTable;
        this.fkColumn = fkColumn;
        this.fkName = fkName;
        this.seq = seq;
    }

    public Table getPkTable() {
        return pkTable;
    }

    public Column getPkColumn() {
        return pkColumn;
    }

    public Table getFkTable() {
        return fkTable;
    }

    public Column getFkColumn() {
        return fkColumn;
    }

    public String getFkName() {
        return fkName;
    }

    public int getSeq() {
        return seq;
    }

    @Override
    public int compareTo(Object o) {
        return seq - ( (ForeignKeyItem) o ).seq;
    }

    @Override
    public String toString() {
        return "ForeignKeyItem [pkTable=" + pkTable.getName() + ", pkColumn=" + pkColumn + ", fkTable=" + fkTable.getName() + ", fkColumn=" + fkColumn
                + ", fkName=" + fkName + ", seq=" + seq + "]";
    }
}
