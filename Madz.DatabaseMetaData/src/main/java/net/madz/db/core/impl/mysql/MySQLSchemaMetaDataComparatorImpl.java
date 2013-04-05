package net.madz.db.core.impl.mysql;

import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.SchemaMetaDataComparator;
import net.madz.db.core.impl.validation.mysql.ErrorEntry;
import net.madz.db.core.impl.validation.mysql.MySQLSchemaMetaDataValidator;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;

public class MySQLSchemaMetaDataComparatorImpl implements
        SchemaMetaDataComparator<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    private List<ErrorEntry> errorSet = new LinkedList<ErrorEntry>();

    @Override
    public List<ErrorEntry> compare(MySQLSchemaMetaData one, MySQLSchemaMetaData other) {
        return MySQLSchemaMetaDataValidator.validate(one, other, errorSet);
    }
}
