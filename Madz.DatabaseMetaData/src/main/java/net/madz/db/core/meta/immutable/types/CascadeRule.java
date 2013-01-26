package net.madz.db.core.meta.immutable.types;

import java.sql.DatabaseMetaData;
import java.util.NoSuchElementException;

/**
 * importedKeyNoAction - do not allow delete of primary key if it has been
 * imported importedKeyCascade - delete rows that import a deleted key
 * importedKeySetNull - change imported key to NULL if its primary key has been
 * deleted importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x
 * compatibility) importedKeySetDefault - change imported key to default if its
 * primary key has been deleted
 */
public enum CascadeRule {
    /**
     * aka noAction: Do not allow delete of primary key if it has been imported
     * / do not allow update of primary key if it has been imported
     */
    restrict,
    /**
     * delete rows that import a deleted key / change imported key to agree with
     * primary key update
     */
    cascade,
    /**
     * change imported key to NULL if its primary key has been deleted, change
     * imported key to NULL if its primary key has been updated
     */
    setNull,
    /**
     * change imported key to default if its primary key has been deleted,
     * change imported key to default if its primary key has been deleted
     */
    setDefault, ;

    /**
     * Convert the DatabaseMetaData JDBC value to a CascadeRule
     * 
     * @param jdbcValue
     *            Value from querying ImportKeys from DatabaseMetaData
     * @return Delete or update cascade rule for the foreign key
     * @throw NoSuchElementException if jdbcValue is null or not recognized
     */
    public final static CascadeRule getImportedKeyRule(Integer jdbcValue) {
        if ( null != jdbcValue ) {
            switch (jdbcValue) {
            case DatabaseMetaData.importedKeyNoAction:
            case DatabaseMetaData.importedKeyRestrict:
                return restrict;
            case DatabaseMetaData.importedKeyCascade:
                return cascade;
            case DatabaseMetaData.importedKeySetDefault:
                return setDefault;
            case DatabaseMetaData.importedKeySetNull:
                return setNull;
            }
        }
        throw new NoSuchElementException(CascadeRule.class.getSimpleName() + "[" + jdbcValue + "]");
    }
}
