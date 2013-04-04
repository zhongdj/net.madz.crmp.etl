package net.madz.db.core.impl.validation.mysql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.IndexEntry;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.utils.Utilities;

public class MySQLIndexMetaDataValidator {

    public static void validate(MySQLIndexMetaData one, MySQLIndexMetaData other, List<ErrorEntry> errorSet) {
        if ( one == other ) {
            return;
        }
        final DottedPath indexPath = one.getTable().getTablePath().append(one.getIndexName());
        Utilities.verifyFields(MySQLIndexMetaData.class, indexPath, one, other, errorSet);
        if ( null == one.getEntrySet() ) {
            if ( null != other.getEntrySet() ) {
                errorSet.add(new ErrorEntry(indexPath, "IndexEntriesSize", ErrorCodeTypes.INDEX_ENTRIES_NUMBER_NOT_MATCHED, 0, other.getEntrySet().size()));
            }
        } else {
            if ( null == other.getEntrySet() ) {
                errorSet.add(new ErrorEntry(indexPath, "IndexEntriesSize", ErrorCodeTypes.INDEX_ENTRIES_NUMBER_NOT_MATCHED, one.getEntrySet().size(), 0));
            } else {
                if ( one.getEntrySet().size() != other.getEntrySet().size() ) {
                    errorSet.add(new ErrorEntry(indexPath, "IndexEntriesSize", ErrorCodeTypes.INDEX_ENTRIES_NUMBER_NOT_MATCHED, one.getEntrySet().size(), other
                            .getEntrySet().size()));
                } else {
                    final Collection<IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entrySet = one
                            .getEntrySet();
                    final HashMap<Integer, IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entriesMap = new HashMap<Integer, IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>>();
                    final HashMap<Integer, IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entriesMap2 = new HashMap<Integer, IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>>();
                    Collection<IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>> entrySet2 = other
                            .getEntrySet();
                    for ( final IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : entrySet ) {
                        entriesMap.put(Integer.valueOf(entry.getPosition()), entry);
                    }
                    for ( final IndexEntry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry : entrySet2 ) {
                        entriesMap2.put(Integer.valueOf(entry.getPosition()), entry);
                    }
                    for ( Integer position : entriesMap.keySet() ) {
                        MySQLIndexEntryValidator.validate(entriesMap.get(position), entriesMap2.get(position), errorSet);
                    }
                }
            }
        }
    }
}
