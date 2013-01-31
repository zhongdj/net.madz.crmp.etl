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
}
