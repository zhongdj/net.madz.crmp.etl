package net.madz.db.core.meta.immutable.impl;

import java.sql.SQLException;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.impl.enums.ImportKeyDbMetaDataEnum;
import net.madz.db.core.meta.immutable.type.CascadeRule;
import net.madz.db.core.meta.immutable.type.KeyDeferrability;

public class ForeignKeyMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final List<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> entryList;
    protected final CascadeRule updateRule, deleteRule;
    protected final KeyDeferrability deferrability;
    protected final TMD pkTable, fkTable;
    protected final IMD pkIndex, fkIndex;
    protected final String fkName;

    public ForeignKeyMetaDataImpl(FMD metaData) throws SQLException {
        this.entryList =  metaData.getEntrySet();
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
    public IMD getForeignKeyIndex() {
        return this.fkIndex;
    }

    @Override
    public TMD getForeignKeyTable() {
        return this.fkTable;
    }

    @Override
    public IMD getPrimaryKeyIndex() {
        return this.pkIndex;
    }

    @Override
    public TMD getPrimaryKeyTable() {
        return this.pkTable;
    }

    @Override
    public CascadeRule getDeleteCascadeRule() {
        return this.deleteRule;
    }

    @Override
    public CascadeRule getUpdateCascadeRule() {
        return this.updateRule;
    }

    @Override
    public KeyDeferrability getKeyDeferrability() {
        return this.deferrability;
    }

    @Override
    public List<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> getEntrySet() {
        return this.entryList;
    }

    @Override
    public Integer size() {
        return this.entryList.size();
    }

    public static DottedPath getKey(MetaDataResultSet<ImportKeyDbMetaDataEnum> rs) throws SQLException {
        return rs.getDottedPath(ImportKeyDbMetaDataEnum.FKTABLE_CAT, ImportKeyDbMetaDataEnum.FKTABLE_SCHEM, ImportKeyDbMetaDataEnum.FKTABLE_NAME,
                ImportKeyDbMetaDataEnum.FK_NAME);
    }

    @SuppressWarnings("unchecked")
	public void addEntry(MetaDataResultSet<ImportKeyDbMetaDataEnum> rsFk) throws SQLException {
        final String fkColumnName = rsFk.get(ImportKeyDbMetaDataEnum.FKCOLUMN_NAME);
        final CMD fkColumn = fkTable.getColumn(fkColumnName);
        final String pkColumnName = rsFk.get(ImportKeyDbMetaDataEnum.PKCOLUMN_NAME);
        final CMD pkColumn = pkTable.getColumn(pkColumnName);
        final short keySeq = rsFk.getShort(ImportKeyDbMetaDataEnum.KEY_SEQ);
        final Entry entry = new Entry((FMD)this, fkColumn, pkColumn, keySeq);
        this.entryList.add(entry);
    }

    public class Entry implements ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD> {

        private CMD fkColumn, pkColumn;
        private FMD key;
        private int seq;

        public Entry(FMD fkMetaData, CMD fkColumn, CMD pkColumn, short keySeq) {
            this.fkColumn = fkColumn;
            this.pkColumn = pkColumn;
            this.key = fkMetaData;
            this.seq = keySeq;
        }

        @Override
        public CMD getForeignKeyColumn() {
            return fkColumn;
        }

        @Override
        public CMD getPrimaryKeyColumn() {
            return pkColumn;
        }

        @Override
        public FMD getKey() {
            return key;
        }
    }
}
