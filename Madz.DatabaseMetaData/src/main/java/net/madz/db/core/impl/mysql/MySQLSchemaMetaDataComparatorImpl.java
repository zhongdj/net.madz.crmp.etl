package net.madz.db.core.impl.mysql;

import java.util.Collection;

import net.madz.db.core.SchemaMetaDataComparator;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;

public class MySQLSchemaMetaDataComparatorImpl implements
        SchemaMetaDataComparator<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    @Override
    public boolean compare(MySQLSchemaMetaData one, MySQLSchemaMetaData other) {
        if ( !compareBaseInfo(one, other) ) {
            return false;
        }
        if ( !compareTables(one.getTables(), other.getTables()) ) {
            return false;
        }
        return true;
    }

    private boolean compareBaseInfo(MySQLSchemaMetaData one, MySQLSchemaMetaData other) {
        if ( null == one || null == other ) {
            return false;
        }
        if ( null == one.getCharSet() || null == other.getCharSet() ) {
            return false;
        }
        if ( one.getCharSet() != other.getCharSet() ) {
            return false;
        }
        if ( null == one.getCollation() || null == other.getCollation() ) {
            return false;
        }
        if ( one.getCollation() != other.getCollation() ) {
            return false;
        }
        return true;
    }

    private boolean compareTables(Collection<MySQLTableMetaData> one, Collection<MySQLTableMetaData> other) {
        
        return false;
    }
}
