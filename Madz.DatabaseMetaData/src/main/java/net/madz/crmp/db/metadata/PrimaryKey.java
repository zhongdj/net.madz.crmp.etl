package net.madz.crmp.db.metadata;

import java.util.LinkedList;
import java.util.List;

public class PrimaryKey {

    private Table t;
    private List<PrimaryKeyItem> keys = new LinkedList<PrimaryKeyItem>();

    public PrimaryKey(Table t) {
        this.t = t;
    }

    public Table getTable() {
        return t;
    }

    public List<PrimaryKeyItem> getKeys() {
        return keys;
    }

    public void addKey(PrimaryKeyItem key) {
        this.keys.add(key);
    }

    @Override
    public String toString() {
        return "PrimaryKey [table=" + t.getName() + "]";
    }
}
