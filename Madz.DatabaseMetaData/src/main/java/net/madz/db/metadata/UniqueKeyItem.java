package net.madz.db.metadata;

public class UniqueKeyItem implements Comparable {

    private Column column;
    private short seq;

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public short getSeq() {
        return seq;
    }

    public void setSeq(short seq) {
        this.seq = seq;
    }

    @Override
    public int compareTo(Object o) {
        return this.seq - ( (UniqueKeyItem) o ).seq;
    }

    @Override
    public String toString() {
        return "UniqueKeyItem [column=" + column.getName() + ", seq=" + seq + "]";
    }
}
