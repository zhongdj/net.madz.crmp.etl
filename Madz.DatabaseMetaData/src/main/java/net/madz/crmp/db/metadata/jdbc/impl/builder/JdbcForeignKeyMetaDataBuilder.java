package net.madz.crmp.db.metadata.jdbc.impl.builder;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcForeignKeyMetaDataImpl;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.crmp.db.metadata.jdbc.impl.enums.JdbcImportKeyDbMetaDataEnum;
import net.madz.crmp.db.metadata.jdbc.type.JdbcCascadeRule;
import net.madz.crmp.db.metadata.jdbc.type.JdbcKeyDeferrability;

public class JdbcForeignKeyMetaDataBuilder implements JdbcForeignKeyMetaData {

    private List<Entry> entryList;
    private JdbcCascadeRule updateRule, deleteRule;
    private JdbcKeyDeferrability deferrability;
    private JdbcTableMetaDataBuilder pkTable, fkTable;
    private JdbcIndexMetaDataBuilder pkIndex, fkIndex;
    private String fkName;

    public class Entry implements JdbcForeignKeyMetaData.Entry {

        private JdbcColumnMetaDataBuilder fkColumn, pkColumn;
        private JdbcForeignKeyMetaDataBuilder key;
        private int seq;

        public Entry(JdbcForeignKeyMetaDataBuilder fkMetaData, JdbcColumnMetaDataBuilder fkColumn, JdbcColumnMetaDataBuilder pkColumn, short keySeq) {
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

    public JdbcForeignKeyMetaDataBuilder(JdbcSchemaMetaDataBuilder schema, JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rsFk) throws SQLException {
        System.out.println("construct jdbc foreign key metadata");
        this.entryList = new LinkedList<Entry>();
        this.updateRule = JdbcCascadeRule.getImportedKeyRule(rsFk.getInt(JdbcImportKeyDbMetaDataEnum.UPDATE_RULE));
        this.deleteRule = JdbcCascadeRule.getImportedKeyRule(rsFk.getInt(JdbcImportKeyDbMetaDataEnum.DELETE_RULE));
        this.deferrability = JdbcKeyDeferrability.getImportedDeferrability(rsFk.getInt(JdbcImportKeyDbMetaDataEnum.DEFERRABILITY));
        this.fkTable = schema.getTable(rsFk.get(JdbcImportKeyDbMetaDataEnum.FKTABLE_NAME));
        this.pkTable = schema.getTable(rsFk.get(JdbcImportKeyDbMetaDataEnum.PKTABLE_NAME));
        this.fkName = rsFk.get(JdbcImportKeyDbMetaDataEnum.FK_NAME);
        String fkName = rsFk.get(JdbcImportKeyDbMetaDataEnum.FK_NAME);
        if ( null != fkName ) {
            this.fkIndex = fkTable.getIndex(fkName);
        }
        String pkName = rsFk.get(JdbcImportKeyDbMetaDataEnum.PK_NAME);
        if ( null != pkName ) {
            this.pkIndex = pkTable.getIndex(pkName);
        }
        this.fkTable.addForeignKey(this);
    }

    public JdbcForeignKeyMetaData build() throws SQLException {
        System.out.println("Jdbc foreign key metadata builder");
        return new JdbcForeignKeyMetaDataImpl(this);
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

    public static DottedPath getKey(JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rsFk) throws SQLException {
        return rsFk.getDottedPath(JdbcImportKeyDbMetaDataEnum.FKTABLE_CAT, JdbcImportKeyDbMetaDataEnum.FKTABLE_SCHEM, JdbcImportKeyDbMetaDataEnum.FKTABLE_NAME,
                JdbcImportKeyDbMetaDataEnum.FK_NAME);
    }

    public void addEntry(JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rsFk) throws SQLException {
        String fkColumnName = rsFk.get(JdbcImportKeyDbMetaDataEnum.FKCOLUMN_NAME);
        JdbcColumnMetaDataBuilder fkColumn = fkTable.getColumn(fkColumnName);
        String pkColumnName = rsFk.get(JdbcImportKeyDbMetaDataEnum.PKCOLUMN_NAME);
        JdbcColumnMetaDataBuilder pkColumn = pkTable.getColumn(pkColumnName);
        short keySeq = rsFk.getShort(JdbcImportKeyDbMetaDataEnum.KEY_SEQ);
        Entry entry = new Entry(this, fkColumn, pkColumn, keySeq);
        fkColumn.getFkList().add(entry);
        this.entryList.add(entry);
    }
}
