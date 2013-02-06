package net.madz.db.core.impl.mysql;

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
        // TODO Auto-generated method stub
        return true;
    }
}
