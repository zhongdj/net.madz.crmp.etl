package net.madz.crmp.db.metadata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.madz.crmp.db.metadata.comparators.TableComparator;

public class SchemaMetaData {

    private final String databaseName;
    List<Table> tables = new LinkedList<Table>();

    public SchemaMetaData(String databaseName) {
        this.databaseName = databaseName;
    }

    public void addTable(Table table) {
        this.tables.add(table);
    }

    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [tables=" + tables + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( tables == null ) ? 0 : tables.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        SchemaMetaData other = (SchemaMetaData) obj;
        if ( tables == null ) {
            if ( other.tables != null ) return false;
        } else {
            Collections.sort(tables, new TableComparator());
            Collections.sort(other.tables, new TableComparator());
            if ( !tables.equals(other.tables) ) return false;
        }
        return true;
    }
}
