package net.madz.db.core.impl.validation.mysql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.utils.Utilities;

public class MySQLTableMetaDataValidator {

    public static void validate(MySQLTableMetaData one, MySQLTableMetaData other, List<ErrorEntry> errorSet) {
        if ( one == other ) {
            return;
        }
        // Fields
        Utilities.verifyFields(one.getClass(), one.getTablePath(), one, other, errorSet);
        // Columns
        if ( null == one.getColumns() ) {
            if ( null != other.getColumns() ) {
                errorSet.add(new ErrorEntry(one.getTablePath(), "columnSize", ErrorCodeTypes.TABLE_COLUMNS_NUMBER_DIFFERENT, 0, other.getColumns().size()));
            }
        } else {
            if ( null == other.getColumns() ) {
                errorSet.add(new ErrorEntry(one.getTablePath(), "columnSize", ErrorCodeTypes.TABLE_COLUMNS_NUMBER_DIFFERENT, one.getColumns().size(), 0));
            } else {
                if ( one.getColumns().size() != other.getColumns().size() ) {
                    String expectedColumnNames = getColumnNames(one);
                    String actualColumnNames = getColumnNames(other);
                    errorSet.add(new ErrorEntry(one.getTablePath(), "columnSize", ErrorCodeTypes.TABLE_COLUMNS_NUMBER_DIFFERENT, expectedColumnNames,
                            actualColumnNames));
                } else {
                    for ( int i = 0; i < one.getColumns().size(); i++ ) {
                        MySQLColumnMetaDataValidator.validate(one.getColumns().get(i), other.getColumns().get(i), errorSet);
                    }
                }
            }
        }
        // Pk
        if ( null == one.getPrimaryKey() ) {
            if ( null != other.getPrimaryKey() ) {
                errorSet.add(new ErrorEntry(one.getTablePath(), "primaryKey", ErrorCodeTypes.TABLE_PRIMARY_KEY_NOT_MATCHED, 0, 1));
            }
        } else {
            if ( null == other.getPrimaryKey() ) {
                errorSet.add(new ErrorEntry(one.getTablePath(), "primaryKey", ErrorCodeTypes.TABLE_PRIMARY_KEY_NOT_MATCHED, 1, 0));
            } else {
                MySQLIndexMetaDataValidator.validate(one.getPrimaryKey(), other.getPrimaryKey(), errorSet);
            }
        }
        // Index
        if ( null == one.getIndexSet() ) {
            if ( null != other.getIndexSet() ) {
                errorSet.add(new ErrorEntry(one.getTablePath(), "indexesNumber", ErrorCodeTypes.TABLE_INDEXES_NUMBER_NOT_MATCHED, 0, other.getIndexSet().size()));
            }
        } else {
            if ( null == other.getIndexSet() ) {
                errorSet.add(new ErrorEntry(one.getTablePath(), "indexesNumber", ErrorCodeTypes.TABLE_INDEXES_NUMBER_NOT_MATCHED, one.getIndexSet().size(), 0));
            } else {
                if ( one.getIndexSet().size() != other.getIndexSet().size() ) {
                    errorSet.add(new ErrorEntry(one.getTablePath(), "indexesNumber", ErrorCodeTypes.TABLE_INDEXES_NUMBER_NOT_MATCHED, one.getIndexSet().size(),
                            other.getIndexSet().size()));
                } else {
                    final Collection<MySQLIndexMetaData> indexSet = one.getIndexSet();
                    for ( MySQLIndexMetaData index : indexSet ) {
                        if ( null == other.getIndex(index.getIndexName()) ) {
                            errorSet.add(new ErrorEntry(one.getTablePath(), index.getIndexName(), ErrorCodeTypes.INDEX_NOT_FOUND, index.getIndexName(), null));
                        } else {
                            MySQLIndexMetaDataValidator.validate(index, other.getIndex(index.getIndexName()), errorSet);
                        }
                    }
                }
            }
        }
        // Foreign Key
        if ( null == one.getForeignKeySet() ) {
            if ( null != other.getForeignKeySet() ) {
                errorSet.add(new ErrorEntry(one.getTablePath(), "foreignKeysNumber", ErrorCodeTypes.FOREIGN_KEYS_NUMBER_DIFFERENT, 0, other.getForeignKeySet()
                        .size()));
            }
        } else {
            if ( null == other.getForeignKeySet() ) {
                errorSet.add(new ErrorEntry(one.getTablePath(), "foreignKeysNumber", ErrorCodeTypes.FOREIGN_KEYS_NUMBER_DIFFERENT, one.getForeignKeySet()
                        .size(), 0));
            } else {
                if ( one.getForeignKeySet().size() != other.getForeignKeySet().size() ) {
                    errorSet.add(new ErrorEntry(one.getTablePath(), "foreignKeysNumber", ErrorCodeTypes.FOREIGN_KEYS_NUMBER_DIFFERENT, one.getForeignKeySet()
                            .size(), other.getForeignKeySet().size()));
                } else {
                    final Collection<MySQLForeignKeyMetaData> foreignKeySet = one.getForeignKeySet();
                    final HashMap<String, MySQLForeignKeyMetaData> fkMap = new HashMap<String, MySQLForeignKeyMetaData>();
                    for ( MySQLForeignKeyMetaData fk : foreignKeySet ) {
                        fkMap.put(fk.getForeignKeyName(), fk);
                    }
                    final Collection<MySQLForeignKeyMetaData> foreignKeySet2 = other.getForeignKeySet();
                    final HashMap<String, MySQLForeignKeyMetaData> fkMap2 = new HashMap<String, MySQLForeignKeyMetaData>();
                    for ( MySQLForeignKeyMetaData fk : foreignKeySet2 ) {
                        fkMap2.put(fk.getForeignKeyName(), fk);
                    }
                    for ( String fkName : fkMap.keySet() ) {
                        if ( !fkMap2.containsKey(fkName) ) {
                            errorSet.add(new ErrorEntry(one.getTablePath(), "findForegineKey", ErrorCodeTypes.FOREIGN_KEY_NAME_NOT_MATCHED, fkName, ""));
                        } else {
                            MySQLForeignKeyMetaDataValidator.validator(fkMap.get(fkName), fkMap2.get(fkName), errorSet);
                        }
                    }
                }
            }
        }
    }

    public static String getColumnNames(MySQLTableMetaData one) {
        final StringBuilder columnNames = new StringBuilder();
        for ( MySQLColumnMetaData column : one.getColumns() ) {
            columnNames.append(column.getColumnName());
            columnNames.append(",");
        }
        columnNames.deleteCharAt(columnNames.length() - 1);
        return columnNames.toString();
    }
}
