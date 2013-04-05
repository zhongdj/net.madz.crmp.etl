package net.madz.db.core.impl.validation.mysql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.utils.Utilities;

public class MySQLSchemaMetaDataValidator {

    public static List<ErrorEntry> validate(MySQLSchemaMetaData one, MySQLSchemaMetaData other, List<ErrorEntry> errorSet) {
        Utilities.verifyFields(one.getClass(), one.getSchemaPath(), one, other, errorSet);
        if ( one.getTables() == null ) {
            if ( other.getTables() != null ) {
                errorSet.add(new ErrorEntry(one.getSchemaPath(), "tablesSize", ErrorCodeTypes.SCHEMA_TABLES_NUMBER_DIFFERENT, 0 + "", other.getTables().size()
                        + ""));
            }
        } else if ( one.getTables().size() != other.getTables().size() ) {
            errorSet.add(new ErrorEntry(one.getSchemaPath(), "tablesSize", ErrorCodeTypes.SCHEMA_TABLES_NUMBER_DIFFERENT, one.getTables().size() + "", other
                    .getTables().size() + ""));
        }
        final Collection<MySQLTableMetaData> actualTables = other.getTables();
        final HashMap<String, MySQLTableMetaData> actualTablesMap = new HashMap<String, MySQLTableMetaData>();
        for ( MySQLTableMetaData actualTable : actualTables ) {
            actualTablesMap.put(actualTable.getTableName(), actualTable);
        }
        for ( MySQLTableMetaData table : one.getTables() ) {
            if ( !actualTablesMap.containsKey(table.getTableName()) ) {
                errorSet.add(new ErrorEntry(one.getSchemaPath(), "findTable", ErrorCodeTypes.TABLE_NOT_FOUND, table.getTableName(), ""));
            } else {
                MySQLTableMetaDataValidator.validate(table, actualTablesMap.get(table.getTableName()), errorSet);
            }
        }
        return errorSet;
    }
}
