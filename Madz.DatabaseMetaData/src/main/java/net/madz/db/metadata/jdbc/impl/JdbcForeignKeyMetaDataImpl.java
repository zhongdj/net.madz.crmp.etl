package net.madz.db.metadata.jdbc.impl;

import java.sql.SQLException;
import java.util.List;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.impl.enums.JdbcImportKeyDbMetaDataEnum;
import net.madz.db.metadata.jdbc.type.JdbcCascadeRule;
import net.madz.db.metadata.jdbc.type.JdbcKeyDeferrability;

public class JdbcForeignKeyMetaDataImpl implements JdbcForeignKeyMetaData {

    protected final List<Entry> entryList;
    protected final JdbcCascadeRule updateRule, deleteRule;
    protected final JdbcKeyDeferrability deferrability;
    protected final JdbcTableMetaData pkTable, fkTable;
    protected final JdbcIndexMetaData pkIndex, fkIndex;
    protected final String fkName;

    public JdbcForeignKeyMetaDataImpl(JdbcForeignKeyMetaData metaData) throws SQLException {
        this.entryList = (List<Entry>) metaData.getEntrySet();
        this.updateRule = metaData.getUpdateCascadeRule();
        this.deleteRule = metaData.getDeleteCascadeRule();
        this.deferrability = metaData.getKeyDeferrability();
        this.fkTable = metaData.getForeignKeyTable();
        this.pkTable = metaData.getPrimaryKeyTable();
        this.fkName = metaData.getForeignKeyName();
        this.fkIndex = metaData.getForeignKeyIndex();
        this.pkIndex = metaData.getPrimaryKeyIndex();
    }

    @Override
    public String getForeignKeyName() {
        return this.fkName;
    }

    @Override
    public JdbcIndexMetaData getForeignKeyIndex() {
        return this.fkIndex;
    }

    @Override
    public JdbcTableMetaData getForeignKeyTable() {
        return this.fkTable;
    }

    @Override
    public JdbcIndexMetaData getPrimaryKeyIndex() {
        return this.pkIndex;
    }

    @Override
    public JdbcTableMetaData getPrimaryKeyTable() {
        return this.pkTable;
    }

    @Override
    public JdbcCascadeRule getDeleteCascadeRule() {
        return this.deleteRule;
    }

    @Override
    public JdbcCascadeRule getUpdateCascadeRule() {
        return this.updateRule;
    }

    @Override
    public JdbcKeyDeferrability getKeyDeferrability() {
        return this.deferrability;
    }

    @Override
    public List<? extends Entry> getEntrySet() {
        return this.entryList;
    }

    @Override
    public Integer size() {
        return this.entryList.size();
    }

    public static DottedPath getKey(JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rs) throws SQLException {
        return rs.getDottedPath(JdbcImportKeyDbMetaDataEnum.FKTABLE_CAT, JdbcImportKeyDbMetaDataEnum.FKTABLE_SCHEM, JdbcImportKeyDbMetaDataEnum.FKTABLE_NAME,
                JdbcImportKeyDbMetaDataEnum.FK_NAME);
    }

    public void addEntry(JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rsFk) throws SQLException {
        String fkColumnName = rsFk.get(JdbcImportKeyDbMetaDataEnum.FKCOLUMN_NAME);
        JdbcColumnMetaData fkColumn = fkTable.getColumn(fkColumnName);
        String pkColumnName = rsFk.get(JdbcImportKeyDbMetaDataEnum.PKCOLUMN_NAME);
        JdbcColumnMetaData pkColumn = pkTable.getColumn(pkColumnName);
        short keySeq = rsFk.getShort(JdbcImportKeyDbMetaDataEnum.KEY_SEQ);
        Entry entry = new Entry(this, fkColumn, pkColumn, keySeq);
    }

    public class Entry implements JdbcForeignKeyMetaData.Entry {

        private JdbcColumnMetaData fkColumn, pkColumn;
        private JdbcForeignKeyMetaData key;
        private int seq;

        public Entry(JdbcForeignKeyMetaData fkMetaData, JdbcColumnMetaData fkColumn, JdbcColumnMetaData pkColumn, short keySeq) {
            this.fkColumn = fkColumn;
            this.pkColumn = pkColumn;
            this.key = fkMetaData;
            this.seq = keySeq;
        }

        @Override
        public JdbcColumnMetaData getForeignKeyColumn() {
            return fkColumn;
        }

        @Override
        public JdbcColumnMetaData getPrimaryKeyColumn() {
            return pkColumn;
        }

        @Override
        public JdbcForeignKeyMetaData getKey() {
            return key;
        }
    }
}
