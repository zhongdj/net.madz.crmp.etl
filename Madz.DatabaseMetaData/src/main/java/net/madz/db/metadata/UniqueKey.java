package net.madz.db.metadata;

import java.util.LinkedList;
import java.util.List;

public class UniqueKey {

    private Table t;
    private String keyName;
    private List<UniqueKeyItem> items = new LinkedList<UniqueKeyItem>();

    public Table getTable() {
        return t;
    }

    public void setTable(Table t) {
        this.t = t;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public List<UniqueKeyItem> getItems() {
        return items;
    }

    public void addItem(List<UniqueKeyItem> items) {
        if ( null == items ) {
            return;
        } else {
            this.items = items;
        }
    }

    @Override
    public String toString() {
        return "UniqueKey [t=" + t.getName() + ", keyName=" + keyName + ", items=" + items + "]";
    };
}
