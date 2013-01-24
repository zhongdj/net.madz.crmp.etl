package net.madz.db.core.meta.immutable.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import net.madz.db.core.meta.DottedPath;

public class MetaDataResultSet<O> {

    private final HashMap<String, O> enumMap;
    private final HashMap<O, Integer> columnMap;
    // TODO [Jan 22, 2013][barry] What's the relationship between this class and
    // the following field?
    private final ResultSet rs;

    public MetaDataResultSet(ResultSet rs, O[] values) throws SQLException {
        this.rs = rs;
        this.enumMap = new HashMap<String, O>(values.length, 1.0f);
        for ( O value : values ) {
            this.enumMap.put(value.toString().toUpperCase(), value);
        }
        // 1. Determine which JDBC meta-data definitions our database supports
        ResultSetMetaData rsMetaData = rs.getMetaData();
        final int colCount = rsMetaData.getColumnCount();
        this.columnMap = new HashMap<O, Integer>(colCount, 1.0f);
        for ( int col = 1; col <= colCount; col++ ) {
            O metaData = enumMap.get(rsMetaData.getColumnName(col).toUpperCase());
            if ( null != metaData ) {
                columnMap.put(metaData, Integer.valueOf(col));
            }
        }
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    public void close() throws SQLException {
        rs.close();
    }

    public Short getShort(O metaData) throws SQLException {
        Integer colId = columnMap.get(metaData);
        if ( null != colId ) {
            Short value = rs.getShort(colId.intValue());
            if ( !rs.wasNull() ) {
                return value;
            }
        }
        return null;
    }

    public Integer getInt(O metaData) throws SQLException {
        // TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable
        // variables
        final Integer colId = columnMap.get(metaData);
        if ( null != colId ) {
            final Integer value = rs.getInt(colId.intValue());
            if ( !rs.wasNull() ) {
                return value;
            }
        }
        return null;
    }

    public Boolean getBoolean(O metaData) throws SQLException {
        Integer colId = columnMap.get(metaData);
        if ( null != colId ) {
            Boolean value = rs.getBoolean(colId.intValue());
            if ( !rs.wasNull() ) {
                return value;
            }
        }
        return null;
    }

    public String get(O metaData) throws SQLException {
        Integer colId = columnMap.get(metaData);
        if ( null != colId ) {
            String result = rs.getString(colId.intValue());
            if ( result == null || 0 >= result.trim().length() ) {
                return null;
            }
            return result.trim();
        }
        return null;
    }

    public DottedPath getDottedPath(O... metaDataParts) throws SQLException {
        DottedPath path = null;
        for ( O metaData : metaDataParts ) {
            String str = get(metaData);
            if ( null != str ) {
                path = DottedPath.append(path, str);
            }
        }
        return path;
    }
}
