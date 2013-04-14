package net.madz.db.core.meta.immutable.types;

import java.sql.DatabaseMetaData;
import java.util.NoSuchElementException;

public enum IndexTypeEnum {
    statistic,
    clustered,
    hashed,
    other;

    public final static IndexTypeEnum getIndexTypeFromJdbcType(Short jdbcValue) {
        if ( null != jdbcValue ) {
            switch (jdbcValue.shortValue()) {
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
        throw new NoSuchElementException(IndexTypeEnum.class.getSimpleName() + "[" + jdbcValue + "]");
    }
}
