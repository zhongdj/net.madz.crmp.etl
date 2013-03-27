package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod;
import net.madz.db.core.meta.immutable.mysql.impl.MySQLIndexMetaDataImpl;
import net.madz.db.core.meta.immutable.types.KeyTypeEnum;
import net.madz.db.core.meta.immutable.types.SortDirectionEnum;
import net.madz.db.core.meta.mutable.impl.BaseIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;
import net.madz.db.utils.ResourceManagementUtils;
import net.madz.db.utils.Utilities;

public class MySQLIndexMetaDataBuilderImpl
        extends
        BaseIndexMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLIndexMetaDataBuilder {

    private MySQLIndexMethod indexMethod;
    private String indexComment;

    public MySQLIndexMetaDataBuilderImpl(MySQLTableMetaDataBuilder table, String indexName) {
        super(table, indexName);
    }

    public MySQLIndexMetaDataBuilderImpl(MySQLTableMetaDataBuilder table, MetaDataResultSet<MySQLIndexDbMetaDataEnum> rs) throws SQLException {
        super(table, rs.get(MySQLIndexDbMetaDataEnum.INDEX_NAME));
        this.isUnique = !rs.getBoolean(MySQLIndexDbMetaDataEnum.NON_UNIQUE);
        if ( this.isUnique )
            this.keyType = KeyTypeEnum.uniqueKey;
        else
            this.keyType = KeyTypeEnum.index;
        // TODO index type??? moved to jdbc level
        this.sortDirection = SortDirectionEnum.getSortDirection(rs.get(MySQLIndexDbMetaDataEnum.COLLATION));
        this.cardinatlity = rs.getInt(MySQLIndexDbMetaDataEnum.CARDINALITY);
        // TODO pages ?? moved to jdbc level
        this.indexMethod = MySQLIndexMethod.getIndexMethod(rs.get(MySQLIndexDbMetaDataEnum.INDEX_TYPE));
        this.indexComment = rs.get(MySQLIndexDbMetaDataEnum.INDEX_COMMENT);
    }

    @Override
    public MySQLIndexMetaDataBuilder build(Connection conn) throws SQLException {
        return this;
    }

    @Override
    public MySQLIndexMethod getIndexMethod() {
        return this.indexMethod;
    }

    @Override
    public void setIndexMethod(MySQLIndexMethod indexMethod) {
        this.indexMethod = indexMethod;
    }

    @Override
    public void setIndexComment(String indexComment) {
        this.indexComment = indexComment;
    }

    @Override
    public String getIndexComment() {
        return indexComment;
    }

    @Override
    public MySQLIndexMetaData createMetaData() {
        this.constructedMetaData = new MySQLIndexMetaDataImpl(this.table.getMetaData(), this);
        return this.constructedMetaData;
    }

    @Override
    public void setKeyType(KeyTypeEnum keyType) {
        this.keyType = keyType;
    }

    @Override
    public void addEntry(MetaDataResultSet<MySQLIndexDbMetaDataEnum> rs) throws SQLException {
        short subPart = rs.getShort(MySQLIndexDbMetaDataEnum.SUB_PART);
        short position = rs.getShort(MySQLIndexDbMetaDataEnum.SEQ_IN_INDEX);
        MySQLColumnMetaDataBuilder column = this.table.getColumnBuilder(rs.get(MySQLIndexDbMetaDataEnum.COLUMN_NAME));
        BaseIndexMetaDataBuilder.Entry entry = new BaseIndexMetaDataBuilder.Entry(this, subPart, column, position);
        if ( this.isUnique ) {
            column.appendUniqueIndexEntry(entry);
        } else {
            column.appendNonUniqueIndexEntry(entry);
        }
        this.addEntry(entry);
        
    }
}
