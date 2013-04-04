package net.madz.db.core.impl.validation.mysql;

import java.util.List;

import net.madz.db.core.meta.immutable.IndexEntry;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.utils.Utilities;

public class MySQLIndexEntryValidator {

    public static void validate(IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> one,
            IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> other,
            List<ErrorEntry> errorSet) {
        Utilities.verifyFields(IndexEntry.class, one.getKey().getTable().getTablePath().append(one.getKey().getIndexName()), one, other, errorSet);
    }
}
