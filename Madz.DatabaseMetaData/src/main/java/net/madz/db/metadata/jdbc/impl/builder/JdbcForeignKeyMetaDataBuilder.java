package net.madz.db.metadata.jdbc.impl.builder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.impl.JdbcForeignKeyMetaDataImpl;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.enums.JdbcImportKeyDbMetaDataEnum;
import net.madz.db.metadata.jdbc.type.JdbcCascadeRule;
import net.madz.db.metadata.jdbc.type.JdbcKeyDeferrability;

public class JdbcForeignKeyMetaDataBuilder implements JdbcForeignKeyMetaData {
    // TODO [Jan 22, 2013][barry] Do all of the following fields protected? Do they all open for access with sub classes? 
    protected List<Entry> entryList;
    protected JdbcCascadeRule updateRule, deleteRule;
    protected JdbcKeyDeferrability deferrability;
    protected JdbcTableMetaDataBuilder pkTable, fkTable;
    protected JdbcIndexMetaDataBuilder pkIndex, fkIndex;
    protected String fkName;
    private JdbcSchemaMetaDataBuilder schema;
    private JdbcMetaDataResultSet<JdbcImportKeyDbMetaDataEnum> rsFk;

    // TODO [Jan 22, 2013][barry] What's the relationship between this inner class and the outer class? 
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
        this.schema = schema;
        this.rsFk = rsFk;
    }

    @Override
    public String getForeignKeyName() {
        return this.fkName;
    }

    public void build(Connection connection) throws SQLException {
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

    public JdbcForeignKeyMetaData getCopy() throws SQLException {
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
    	// TODO [Jan 22, 2013][barry][Done] Use modifier final with immutable variables
        final String fkColumnName = rsFk.get(JdbcImportKeyDbMetaDataEnum.FKCOLUMN_NAME);
        final JdbcColumnMetaDataBuilder fkColumn = fkTable.getColumn(fkColumnName);
        final String pkColumnName = rsFk.get(JdbcImportKeyDbMetaDataEnum.PKCOLUMN_NAME);
        final JdbcColumnMetaDataBuilder pkColumn = pkTable.getColumn(pkColumnName);
        final short keySeq = rsFk.getShort(JdbcImportKeyDbMetaDataEnum.KEY_SEQ);
        final Entry entry = new Entry(this, fkColumn, pkColumn, keySeq);
        fkColumn.getFkList().add(entry);
        this.entryList.add(entry);
    }
}
