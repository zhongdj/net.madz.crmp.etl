package net.madz.db.metadata.jdbc.impl.builder;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.madz.db.metadata.DottedPath;
import net.madz.db.metadata.jdbc.JdbcColumnMetaData;
import net.madz.db.metadata.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData;
import net.madz.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.db.metadata.jdbc.JdbcIndexMetaData.Entry;
import net.madz.db.metadata.jdbc.impl.JdbcColumnMetaDataImpl;
import net.madz.db.metadata.jdbc.impl.JdbcMetaDataResultSet;
import net.madz.db.metadata.jdbc.impl.enums.JdbcColumnDbMetaDataEnum;

public class JdbcColumnMetaDataBuilder implements JdbcColumnMetaData {

    protected DottedPath name;
    protected JdbcTableMetaData table;
    protected Integer sqlType;
    protected String typeName;
    protected Integer size;
    protected boolean isNullable;
    protected boolean isAutoIncremented;
    protected Integer radix, charOctetLength;
    protected String remarks, defaultValue;
    protected Entry primaryKey;
    protected List<Entry> uniqueIndexList = new LinkedList<Entry>();
    protected List<Entry> nonUniqueIndexList = new LinkedList<Entry>();
    protected List<JdbcForeignKeyMetaDataBuilder.Entry> fkList = new LinkedList<JdbcForeignKeyMetaDataBuilder.Entry>();
    protected Short ordinalPosition;
    protected JdbcTableMetaDataBuilder jdbcTableMetaDataBuilder;
    protected JdbcMetaDataResultSet<JdbcColumnDbMetaDataEnum> colRs;

    public JdbcColumnMetaDataBuilder(JdbcTableMetaDataBuilder jdbcTableMetaDataBuilder, JdbcMetaDataResultSet<JdbcColumnDbMetaDataEnum> colRs)
            throws SQLException {
        this.jdbcTableMetaDataBuilder = jdbcTableMetaDataBuilder;
        this.colRs = colRs;
        this.name = jdbcTableMetaDataBuilder.getTablePath().append(colRs.get(JdbcColumnDbMetaDataEnum.COLUMN_NAME));
        this.table = jdbcTableMetaDataBuilder;
        this.sqlType = colRs.getInt(JdbcColumnDbMetaDataEnum.DATA_TYPE);
        this.typeName = colRs.get(JdbcColumnDbMetaDataEnum.TYPE_NAME);
        this.size = colRs.getInt(JdbcColumnDbMetaDataEnum.COLUMN_SIZE);
        this.radix = colRs.getInt(JdbcColumnDbMetaDataEnum.NUM_PREC_RADIX);
        this.charOctetLength = colRs.getInt(JdbcColumnDbMetaDataEnum.CHAR_OCTET_LENGTH);
        this.remarks = colRs.get(JdbcColumnDbMetaDataEnum.REMARKS);
        this.defaultValue = colRs.get(JdbcColumnDbMetaDataEnum.COLUMN_DEF);
        this.ordinalPosition = colRs.getShort(JdbcColumnDbMetaDataEnum.ORDINAL_POSITION);
        this.isNullable = colRs.getBoolean(JdbcColumnDbMetaDataEnum.IS_NULLABLE);
        this.isAutoIncremented = colRs.getBoolean(JdbcColumnDbMetaDataEnum.IS_AUTOINCREMENT);
    }

    public JdbcColumnMetaData build() throws SQLException {
        System.out.println("Jdbc column metadata builder");
        return new JdbcColumnMetaDataImpl(this);
    }

    @Override
    public DottedPath getColumnPath() {
        return this.name;
    }

    @Override
    public JdbcTableMetaData getTableMetaData() {
        return this.table;
    }

    @Override
    public String getColumnName() {
        return this.name.getName();
    }

    @Override
    public Integer getSqlType() {
        return this.sqlType;
    }

    @Override
    public String getSqlTypeName() {
        return this.typeName;
    }

    @Override
    public boolean isNullable() {
        return this.isNullable;
    }

    @Override
    public Entry getPrimaryKey() {
        return this.primaryKey;
    }

    @Override
    public boolean isMemberOfPrimaryKey() {
        JdbcIndexMetaData primaryKey = this.table.getPrimaryKey();
        if ( primaryKey.containsColumn(this) ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMemberOfIndex() {
        Collection<? extends JdbcIndexMetaData> indexSet = this.table.getIndexSet();
        for ( JdbcIndexMetaData index : indexSet ) {
            if ( index.containsColumn(this) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMemberOfForeignKey(JdbcForeignKeyMetaData fk) {
        Collection<? extends JdbcForeignKeyMetaData> foreignKeySet = this.table.getForeignKeySet();
        for ( JdbcForeignKeyMetaData fkMetaData : foreignKeySet ) {
            if ( fkMetaData.getEntrySet().contains(this) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends Entry> getUniqueIndexSet() {
        return this.uniqueIndexList;
    }

    @Override
    public boolean isMemberOfUniqueIndex() {
        return this.uniqueIndexList.contains(this);
    }

    @Override
    public Collection<? extends Entry> getNonUniqueIndexSet() {
        return this.nonUniqueIndexList;
    }

    @Override
    public Integer getRadix() {
        return this.radix;
    }

    @Override
    public boolean isAutoIncremented() {
        return this.isAutoIncremented;
    }

    @Override
    public Integer getSize() {
        return this.size;
    }

    @Override
    public Short getOrdinalPosition() {
        return this.ordinalPosition;
    }

    @Override
    public boolean hasDefaultValue() {
        return null != this.defaultValue;
    }

    public DottedPath getName() {
        return name;
    }

    public JdbcTableMetaData getTable() {
        return table;
    }

    public String getTypeName() {
        return typeName;
    }

    public Integer getCharOctetLength() {
        return charOctetLength;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public List<Entry> getUniqueIndexList() {
        return uniqueIndexList;
    }

    public List<Entry> getNonUniqueIndexList() {
        return nonUniqueIndexList;
    }

    public List<JdbcForeignKeyMetaDataBuilder.Entry> getFkList() {
        return fkList;
    }

    public JdbcTableMetaDataBuilder getJdbcTableMetaDataBuilder() {
        return jdbcTableMetaDataBuilder;
    }

    public JdbcMetaDataResultSet<JdbcColumnDbMetaDataEnum> getColRs() {
        return colRs;
    }

    public void addIndex(Entry index) {
        if ( null != primaryKey && primaryKey.getKey().getIndexName().equals(index.getKey().getIndexName()) ) {
            // Skipping primary key
        } else {
            if ( index.getKey().isUnique() )
                this.uniqueIndexList.add(index);
            else
                this.nonUniqueIndexList.add(index);
        }
    }

    public void setPrimaryKey(Entry entry) {
        this.uniqueIndexList.remove(primaryKey);
        this.primaryKey = entry;
    }
}
