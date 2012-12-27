package net.madz.crmp.db.metadata;

public class PrimaryKeyItem implements Comparable {

    private Table t;
    private Column column;
    private short seq;

    public PrimaryKeyItem(Table t, Column column, short seq) {
        this.t = t;
        this.column = column;
        this.seq = seq;
    }

    public Column getColumn() {
        return column;
    }

    public short getSeq() {
        return seq;
    }

    @Override
    public String toString() {
        return "PrimaryKeyItem [column=" + column.getName() + ", seq=" + seq + "]";
    }

    @Override
    public int compareTo(Object o) {
        return this.seq - ( (PrimaryKeyItem) o ).seq;
    }
}
