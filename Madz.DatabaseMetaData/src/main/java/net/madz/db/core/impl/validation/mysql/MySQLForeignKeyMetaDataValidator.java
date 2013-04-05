package net.madz.db.core.impl.validation.mysql;

import java.util.HashMap;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ForeignKeyEntry;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.utils.Utilities;

public class MySQLForeignKeyMetaDataValidator {

    public static void validator(MySQLForeignKeyMetaData one, MySQLForeignKeyMetaData other, List<ErrorEntry> errorSet) {
        DottedPath fkPath = one.getForeignKeyTable().getTablePath().append(one.getForeignKeyName());
        Utilities.verifyFields(MySQLForeignKeyMetaData.class, fkPath, one, other, errorSet);
        if ( null == one.getEntrySet() ) {
            if ( null != other.getEntrySet() ) {
                errorSet.add(new ErrorEntry(fkPath, "ForeignkeyEntriesSize", ErrorCodeTypes.FOREIGN_KEY_ENTRIES_NUMBER_DIFFERENT, 0, other.getEntrySet().size()));
            }
        } else {
            if ( null == other.getEntrySet() ) {
                errorSet.add(new ErrorEntry(fkPath, "ForeignkeyEntriesSize", ErrorCodeTypes.FOREIGN_KEY_ENTRIES_NUMBER_DIFFERENT, one.getEntrySet().size(), 0));
            } else {
                if ( one.getEntrySet().size() != other.getEntrySet().size() ) {
                    errorSet.add(new ErrorEntry(fkPath, "ForeignkeyEntriesSize", ErrorCodeTypes.FOREIGN_KEY_ENTRIES_NUMBER_DIFFERENT, one.getEntrySet().size(),
                            other.getEntrySet().size()));
                } else {
                    final List<ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entrySet = one
                            .getEntrySet();
                    final List<ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entrySet2 = other
                            .getEntrySet();
                    final HashMap<Short, ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> fkEntriesMap = new HashMap<Short, ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>>();
                    final HashMap<Short, ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> fkEntriesMap2 = new HashMap<Short, ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>>();
                    for ( ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : entrySet ) {
                        fkEntriesMap.put(entry.getSeq(), entry);
                    }
                    for ( ForeignKeyEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : entrySet2 ) {
                        fkEntriesMap2.put(entry.getSeq(), entry);
                    }
                    for ( Short position : fkEntriesMap.keySet() ) {
                        MySQLForeignKeyEntryValidator.validator(fkEntriesMap.get(position), fkEntriesMap2.get(position), errorSet);
                    }
                }
            }
        }
    }
}
