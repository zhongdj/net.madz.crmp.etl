package net.madz.db.core.meta.immutable.types;

import java.sql.DatabaseMetaData;
import java.util.NoSuchElementException;

public enum IndexType {
    statistic,
    clustered,
    hashed,
    other;

    public final static IndexType getIndexTypeFromJdbcType(Integer jdbcValue) {
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
        throw new NoSuchElementException(IndexType.class.getSimpleName() + "[" + jdbcValue + "]");
    }
}
