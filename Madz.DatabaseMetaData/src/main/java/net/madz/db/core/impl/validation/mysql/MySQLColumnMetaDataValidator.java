package net.madz.db.core.impl.validation.mysql;

import java.util.List;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.utils.Utilities;

public class MySQLColumnMetaDataValidator {

    public static void validate(MySQLColumnMetaData one, MySQLColumnMetaData other, List<ErrorEntry> errorSet) {
        Utilities.verifyFields(one.getClass(), one.getColumnPath(), one, other, errorSet);
    }
}
