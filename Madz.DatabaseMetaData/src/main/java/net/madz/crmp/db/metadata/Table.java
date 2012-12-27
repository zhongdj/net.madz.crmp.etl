package net.madz.crmp.db.metadata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Table {

    private final String name;
    private List<Column> columns = new LinkedList<Column>();
    private PrimaryKey pk = null;
    private Map<String, List<ForeignKeyItem>> fks = new HashMap<String, List<ForeignKeyItem>>();
    private List<UniqueKey> uks = new LinkedList<UniqueKey>();

    public Table(String name) {
        this.name = name;
    }

    public void addColumn(Column column) {
        if ( null == column ) {
            return;
        }
        this.columns.add(column);
    }

    public void setPrimaryKey(PrimaryKey pk) {
        this.pk = pk;
    }

    public void addForeignKey(String keyName, ForeignKeyItem fk) {
        if ( null == keyName || null == fk ) {
            return;
        }
        if ( !fks.containsKey(keyName) ) {
            LinkedList<ForeignKeyItem> result = new LinkedList<ForeignKeyItem>();
            result.push(fk);
            this.fks.put(keyName, result);
        } else {
            fks.get(keyName).add(fk);
        }
    }

    public Map<String, List<ForeignKeyItem>> getFks() {
        return fks;
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Column getColumn(String cName) {
        if ( null == this.columns ) {
            return null;
        } else {
            for ( Column c : columns ) {
                if ( cName.equalsIgnoreCase(c.getName()) ) {
                    return c;
                }
            }
        }
        return null;
    }

    public PrimaryKey getPk() {
        return pk;
    }

    public void addUniqueKeys(UniqueKey uk) {
        this.uks.add(uk);
    }

    public List<UniqueKey> getUks() {
        return uks;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Table [name=");
        result.append(name);
        result.append(", columns=");
        result.append(columns);
        result.append(", pk=");
        result.append(pk);
        result.append(",fks=");
        Set<String> keySet = fks.keySet();
        for ( String key : keySet ) {
            result.append(key);
            result.append(",items=[");
            List<ForeignKeyItem> items = fks.get(key);
            result.append(items.toString());
            result.append("],");
        }
        result.append("uks=");
        result.append(uks);
        return "Table [name=" + name + ", columns=" + columns + ", pk=" + pk + ", fks=" + fks + ", uks=" + uks + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        Table other = (Table) obj;
        if ( name == null ) {
            if ( other.name != null ) return false;
        } else if ( !name.equals(other.name) ) return false;
        return true;
    }
}
