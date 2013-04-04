package net.madz.db.core.impl.validation.mysql;

import java.util.List;

import net.madz.db.core.meta.immutable.ForeignKeyEntry;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.utils.Utilities;

public class MySQLForeignKeyEntryValidator {

    public static void validator(
            ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> one,
            ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> other,
            List<ErrorEntry> errorSet) {
        Utilities.verifyFields(ForeignKeyEntry.class, one.getKey().getForeignKeyTable().getTablePath().append(one.getKey().getForeignKeyName()), one, other,
                errorSet);
    }
}
