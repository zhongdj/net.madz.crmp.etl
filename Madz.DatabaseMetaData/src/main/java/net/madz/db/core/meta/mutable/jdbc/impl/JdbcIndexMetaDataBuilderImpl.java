package net.madz.db.core.meta.mutable.jdbc.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.impl.enums.IndexDbMetaDataEnum;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.immutable.jdbc.impl.JdbcIndexMetaDataImpl;
import net.madz.db.core.meta.immutable.types.KeyTypeEnum;
import net.madz.db.core.meta.immutable.types.SortDirectionEnum;
import net.madz.db.core.meta.mutable.impl.BaseIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcTableMetaDataBuilder;

public class JdbcIndexMetaDataBuilderImpl
        extends
        BaseIndexMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData>
        implements JdbcIndexMetaDataBuilder {

    public JdbcIndexMetaDataBuilderImpl(JdbcTableMetaDataBuilder table, MetaDataResultSet<IndexDbMetaDataEnum> indexRs, String indexName) throws SQLException {
        super(table, indexName);
        this.cardinatlity = indexRs.getInt(IndexDbMetaDataEnum.CARDINALITY);
//        final Short type = indexRs.getShort(IndexDbMetaDataEnum.TYPE);
//        this.indexType = IndexTypeEnum.getIndexTypeFromJdbcType(type);
        this.isUnique = !indexRs.getBoolean(IndexDbMetaDataEnum.NON_UNIQUE);
        if ( this.isUnique ) {
            this.keyType = KeyTypeEnum.uniqueKey;
        } else {
            this.keyType = KeyTypeEnum.index;
        }
        this.pages = indexRs.getInt(IndexDbMetaDataEnum.PAGES);
        final String aseOrDesc = indexRs.get(IndexDbMetaDataEnum.ASC_OR_DESC);
        this.sortDirection = SortDirectionEnum.getSortDirection(aseOrDesc);
    }

    @Override
    public JdbcIndexMetaDataBuilder build(Connection conn) throws SQLException {
        return this;
    }

    @Override
    protected JdbcIndexMetaData createMetaData() {
        final JdbcIndexMetaDataImpl result = new JdbcIndexMetaDataImpl(table.getMetaData(), this);
        this.constructedMetaData = result;
        return this.constructedMetaData;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addIndexEntry(MetaDataResultSet<IndexDbMetaDataEnum> indexRs) throws SQLException {
        final String columnName = indexRs.get(IndexDbMetaDataEnum.COLUMN_NAME);
        final JdbcColumnMetaData column = this.table.getColumnBuilder(columnName);
        final Short ordinalPosition = indexRs.getShort(IndexDbMetaDataEnum.ORDINAL_POSITION);
        BaseIndexMetaDataBuilder.Entry entry = new BaseIndexMetaDataBuilder.Entry(this, 0, column, ordinalPosition);
        this.entryList.add(entry);
    }

    @Override
    public void setPrimaryKey() {
        this.keyType = KeyTypeEnum.primaryKey;
    }
}
