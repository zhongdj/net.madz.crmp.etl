package net.madz.db.core.meta.immutable.mysql.impl;

import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.immutable.impl.TableMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;

public final class MySQLTableMetaDataImpl extends
        TableMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLTableMetaData {

    private final MySQLEngineEnum engine;
    private final String characterSet;
    private final String collation;

    public MySQLTableMetaDataImpl(MySQLSchemaMetaData parent, MySQLTableMetaData metaData) {
        super(parent, metaData);
        this.engine = metaData.getEngine();
        this.characterSet = metaData.getCharacterSet();
        this.collation = metaData.getCollation();
    }

    @Override
    public MySQLEngineEnum getEngine() {
        return this.engine;
    }

    @Override
    public String getCharacterSet() {
        return this.characterSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }

    public void addAllColumns(LinkedList<MySQLColumnMetaDataImpl> columns) {
        for ( MySQLColumnMetaDataImpl column : columns ) {
            this.orderedColumns.add(column);
            this.columnMap.put(column.getColumnName(), column);
        }
    }

    public void addAllIndexes(List<MySQLIndexMetaData> indexes) {
        for ( MySQLIndexMetaData index : indexes ) {
            this.indexMap.put(index.getIndexName(), index);
        }
    }

    public void addAllFks(List<MySQLForeignKeyMetaData> fks) {
        for ( MySQLForeignKeyMetaData fk : fks ) {
            this.fkList.add(fk);
        }
    }

    public void setPrimaryKey(MySQLIndexMetaData pk) {
        this.primaryKey = pk;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( characterSet == null ) ? 0 : characterSet.hashCode() );
        result = prime * result + ( ( collation == null ) ? 0 : collation.hashCode() );
        result = prime * result + ( ( engine == null ) ? 0 : engine.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( !super.equals(obj) ) return false;
        if ( getClass() != obj.getClass() ) return false;
        MySQLTableMetaDataImpl other = (MySQLTableMetaDataImpl) obj;
        if ( characterSet == null ) {
            if ( other.characterSet != null ) return false;
        } else if ( !characterSet.equals(other.characterSet) ) return false;
        if ( collation == null ) {
            if ( other.collation != null ) return false;
        } else if ( !collation.equals(other.collation) ) return false;
        if ( engine != other.engine ) return false;
        return true;
    }
}
