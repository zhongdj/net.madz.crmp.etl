package net.madz.crmp.db.metadata;

import java.sql.DatabaseMetaData;
import java.util.NoSuchElementException;

public enum JdbcIndexType {
    statistic,
    clustered,
    hashed,
    other;

    public final static JdbcIndexType getIndexTypeFromJdbcType(Integer jdbcValue) {
        if ( null != jdbcValue ) {
            switch (jdbcValue.intValue()) {
            case DatabaseMetaData.tableIndexStatistic:
                return statistic;
            case DatabaseMetaData.tableIndexClustered:
                return clustered;
            case DatabaseMetaData.tableIndexHashed:
                return hashed;
            case DatabaseMetaData.tableIndexOther:
                return other;
            }
        }
        throw new NoSuchElementException(JdbcIndexType.class.getSimpleName() + "[" + jdbcValue + "]");
    }
}
