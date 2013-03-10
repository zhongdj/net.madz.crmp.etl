package net.madz.db.core.meta.immutable.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;
import net.madz.db.core.meta.immutable.impl.enums.ImportKeyDbMetaDataEnum;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.types.CascadeRule;
import net.madz.db.core.meta.immutable.types.KeyDeferrability;

public class ForeignKeyMetaDataImpl<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>>
        implements ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD> {

    protected final List<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> entryList = new LinkedList<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>>();
    protected final CascadeRule updateRule, deleteRule;
    protected final KeyDeferrability deferrability;
    protected TMD pkTable;
    protected final TMD fkTable;
    protected IMD pkIndex, fkIndex;
    protected final String fkName;

    public ForeignKeyMetaDataImpl(TMD parent, FMD metaData) {
        this.updateRule = metaData.getUpdateCascadeRule();
        this.deleteRule = metaData.getDeleteCascadeRule();
        this.deferrability = metaData.getKeyDeferrability();
        this.fkTable = parent.getParent().getTable(metaData.getForeignKeyTable().getTableName());
        this.pkTable = parent.getParent().getTable(metaData.getPrimaryKeyTable().getTableName());
        this.fkName = metaData.getForeignKeyName();
        if ( null != metaData.getForeignKeyIndex() ) {
            this.fkIndex = this.fkTable.getIndex(metaData.getForeignKeyIndex().getIndexName());
        }
        if ( null != metaData.getPrimaryKeyIndex() ) {
            this.pkIndex = this.pkTable.getIndex(metaData.getPrimaryKeyIndex().getIndexName());
        }
        List<ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD>> entrySet = metaData.getEntrySet();
        for ( ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD> entry : entrySet ) {
            final String fkColName = entry.getForeignKeyColumn().getColumnName();
            final String pkColName = entry.getPrimaryKeyColumn().getColumnName();
            Short seq = entry.getSeq();
            CMD fkColumn = this.fkTable.getColumn(fkColName);
            CMD pkColumn = this.pkTable.getColumn(pkColName);
            Entry item = new ForeignKeyMetaDataImpl.Entry(this, fkColumn, pkColumn, seq);
            this.entryList.add(item);
        }
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
        final Entry entry = new Entry((FMD) this, fkColumn, pkColumn, keySeq);
        this.entryList.add(entry);
    }

    public class Entry implements ForeignKeyMetaData.Entry<SMD, TMD, CMD, FMD, IMD> {

        private CMD fkColumn, pkColumn;
        private FMD key;
        private Short seq;

        public Entry(FMD fkMetaData, CMD fkColumn, CMD pkColumn, Short seq) {
            this.fkColumn = fkColumn;
            this.pkColumn = pkColumn;
            this.key = fkMetaData;
            this.seq = seq;
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

        @Override
        public Short getSeq() {
            return seq;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ( ( key == null ) ? 0 : key.hashCode() );
            result = prime * result + ( ( seq == null ) ? 0 : seq.hashCode() );
            result = prime * result + ( ( fkColumn == null ) ? 0 : fkColumn.hashCode() );
            result = prime * result + ( ( pkColumn == null ) ? 0 : pkColumn.hashCode() );
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if ( this == obj ) return true;
            if ( obj == null ) return false;
            if ( getClass() != obj.getClass() ) return false;
            Entry other = (Entry) obj;
            if ( !getOuterType().equals(other.getOuterType()) ) return false;
            if ( key == null ) {
                if ( other.key != null ) return false;
            } else if ( !key.equals(other.key) ) return false;
            if ( seq == null ) {
                if ( other.seq != null ) return false;
            } else if ( !seq.equals(other.seq) ) return false;
            if ( fkColumn == null ) {
                if ( other.fkColumn != null ) return false;
            } else if ( !fkColumn.equals(other.fkColumn) ) return false;
            if ( pkColumn == null ) {
                if ( other.pkColumn != null ) return false;
            } else if ( !pkColumn.equals(other.pkColumn) ) return false;
            return true;
        }

        private ForeignKeyMetaDataImpl getOuterType() {
            return ForeignKeyMetaDataImpl.this;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( fkName == null ) ? 0 : fkName.hashCode() );
        result = prime * result + ( ( deferrability == null ) ? 0 : deferrability.hashCode() );
        result = prime * result + ( ( deleteRule == null ) ? 0 : deleteRule.hashCode() );
        result = prime * result + ( ( updateRule == null ) ? 0 : updateRule.hashCode() );
        result = prime * result + ( ( fkIndex == null ) ? 0 : fkIndex.hashCode() );
        result = prime * result + ( ( fkTable.getTableName() == null ) ? 0 : fkTable.getTableName().hashCode() );
        result = prime * result + ( ( pkIndex == null ) ? 0 : pkIndex.hashCode() );
        result = prime * result + ( ( pkTable == null ) ? 0 : pkTable.hashCode() );
        result = prime * result + ( ( entryList == null ) ? 0 : entryList.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        ForeignKeyMetaDataImpl other = (ForeignKeyMetaDataImpl) obj;
        if ( fkName == null ) {
            if ( other.fkName != null ) return false;
        } else if ( !fkName.equals(other.fkName) ) return false;
        if ( deferrability != other.deferrability ) return false;
        if ( deleteRule != other.deleteRule ) return false;
        if ( updateRule != other.updateRule ) return false;
        if ( fkIndex == null ) {
            if ( other.fkIndex != null ) return false;
        } else if ( !fkIndex.equals(other.fkIndex) ) return false;
        if ( fkTable.getTableName() == null ) {
            if ( other.fkTable.getTableName() != null ) return false;
        } else if ( !fkTable.getTableName().equals(other.fkTable.getTableName()) ) return false;
        if ( pkIndex == null ) {
            if ( other.pkIndex != null ) return false;
        } else if ( !pkIndex.equals(other.pkIndex) ) return false;
        if ( pkTable == null ) {
            if ( other.pkTable != null ) return false;
        } else if ( !pkTable.equals(other.pkTable) ) return false;
        if ( entryList == null ) {
            if ( other.entryList != null ) return false;
        } else if ( !entryList.equals(other.entryList) ) return false;
        return true;
    }
}
