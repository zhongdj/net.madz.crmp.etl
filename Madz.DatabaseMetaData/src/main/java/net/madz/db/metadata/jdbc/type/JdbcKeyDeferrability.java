package net.madz.db.metadata.jdbc.type;

import java.sql.DatabaseMetaData;
import java.util.NoSuchElementException;

/**
 * This controls whether the constraint can be deferred to the end of the
 * transaction. If SET CONSTRAINTS ALL DEFERRED is used or the constraint is set
 * to INITIALLY DEFERRED, this will cause the foreign key to be checked only at
 * the end of the transaction.
 * 
 * importedKeyInitiallyDeferred - see SQL92 for definition
 * importedKeyInitiallyImmediate - see SQL92 for definition
 * importedKeyNotDeferrable - see SQL92 for definition
 */
public enum JdbcKeyDeferrability {
    initiallyDeferred,
    initiallyImmediate,
    notDeferrable, ;

    /**
     * Convert the DatabaseMetaData JDBC value to a CascadeRule
     * 
     * @param jdbcValue
     *            Value from querying ImportKeys from DatabaseMetaData
     * @return Delete or update cascade rule for the foreign key
     * @throw NoSuchElementException if jdbcValue is null or not recognized
     */
    public final static JdbcKeyDeferrability getImportedDeferrability(Integer jdbcValue) {
        if ( null != jdbcValue ) {
            switch (jdbcValue) {
            case DatabaseMetaData.importedKeyInitiallyDeferred:
                return initiallyDeferred;
            case DatabaseMetaData.importedKeyInitiallyImmediate:
                return initiallyImmediate;
            case DatabaseMetaData.importedKeyNotDeferrable:
                return notDeferrable;
            }
        }
        throw new NoSuchElementException(JdbcKeyDeferrability.class.getSimpleName() + "[" + jdbcValue + "]");
    }
}
