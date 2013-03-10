package net.madz.db.core.meta.immutable.mysql.impl;

import java.util.List;
import java.util.TreeMap;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.impl.SchemaMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;

public final class MySQLSchemaMetaDataImpl extends
        SchemaMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLSchemaMetaData {

    // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable fields
    private final String charSet;
    private final String collation;
    private Integer lowerCaseTableNames = 2; // Default value is 2 under mac.

    public MySQLSchemaMetaDataImpl(MySQLSchemaMetaData metaData) {
        super(metaData);
        this.charSet = metaData.getCharSet();
        this.collation = metaData.getCollation();
        this.lowerCaseTableNames = metaData.getLowerCaseTableNames();
        if ( lowerCaseTableNames == 0 ) {
            this.tablesMap = new TreeMap<String, MySQLTableMetaData>();
        } else {
            this.tablesMap = new TreeMap<String, MySQLTableMetaData>(String.CASE_INSENSITIVE_ORDER);
        }
    }

    public MySQLSchemaMetaDataImpl(DottedPath name, String charSet, String collation) {
        super(name);
        this.charSet = charSet;
        this.collation = collation;
    }

    @Override
    public String getCharSet() {
        return charSet;
    }

    @Override
    public String getCollation() {
        return collation;
    }

    public void addAllTables(List<MySQLTableMetaData> tables) {
        for ( MySQLTableMetaData table : tables ) {
            // this.orderedTables.add(table);
            this.tablesMap.put(table.getTableName(), table);
        }
    }

    @Override
    public Integer getLowerCaseTableNames() {
        return this.lowerCaseTableNames;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( charSet == null ) ? 0 : charSet.hashCode() );
        result = prime * result + ( ( collation == null ) ? 0 : collation.hashCode() );
        result = prime * result + ( ( lowerCaseTableNames == null ) ? 0 : lowerCaseTableNames.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( !super.equals(obj) ) return false;
        if ( getClass() != obj.getClass() ) return false;
        MySQLSchemaMetaDataImpl other = (MySQLSchemaMetaDataImpl) obj;
        if ( charSet == null ) {
            if ( other.charSet != null ) return false;
        } else if ( !charSet.equals(other.charSet) ) return false;
        if ( collation == null ) {
            if ( other.collation != null ) return false;
        } else if ( !collation.equals(other.collation) ) return false;
        if ( lowerCaseTableNames == null ) {
            if ( other.lowerCaseTableNames != null ) return false;
        } else if ( !lowerCaseTableNames.equals(other.lowerCaseTableNames) ) return false;
        return true;
    }
}
