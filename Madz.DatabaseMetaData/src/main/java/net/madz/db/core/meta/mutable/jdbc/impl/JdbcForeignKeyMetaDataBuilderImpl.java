package net.madz.db.core.meta.mutable.jdbc.impl;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.impl.enums.AccessForeignKeyDbMetaDataEnum;
import net.madz.db.core.meta.immutable.jdbc.JdbcColumnMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcForeignKeyMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcIndexMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcSchemaMetaData;
import net.madz.db.core.meta.immutable.jdbc.JdbcTableMetaData;
import net.madz.db.core.meta.immutable.jdbc.impl.JdbcForeignKeyMetaDataImpl;
import net.madz.db.core.meta.immutable.types.CascadeRule;
import net.madz.db.core.meta.mutable.impl.BaseForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.jdbc.JdbcTableMetaDataBuilder;

public class JdbcForeignKeyMetaDataBuilderImpl
        extends
        BaseForeignKeyMetaDataBuilder<JdbcSchemaMetaDataBuilder, JdbcTableMetaDataBuilder, JdbcColumnMetaDataBuilder, JdbcForeignKeyMetaDataBuilder, JdbcIndexMetaDataBuilder, JdbcSchemaMetaData, JdbcTableMetaData, JdbcColumnMetaData, JdbcForeignKeyMetaData, JdbcIndexMetaData>
        implements JdbcForeignKeyMetaDataBuilder {

    public JdbcForeignKeyMetaDataBuilderImpl(MetaDataResultSet<AccessForeignKeyDbMetaDataEnum> metaData, JdbcTableMetaDataBuilder parent, String foreignKeyName)
            throws SQLException {
        super();
        this.fkTable = parent;
        this.foreignKeyPath = parent.getTablePath().append(foreignKeyName);
        this.deferrability = null;
        final Long grbit = metaData.getLong(AccessForeignKeyDbMetaDataEnum.grbit);
        this.deleteRule = CascadeRule.getAccessDBOnDeleteRule(grbit);
        this.updateRule = CascadeRule.getAccessDBOnUpdateRule(grbit);
        this.pkTable = parent.getSchema().getTableBuilder(metaData.get(AccessForeignKeyDbMetaDataEnum.szReferencedObject));
    }

    @Override
    public String getForeignKeyIndexName() {
        if ( null == this.fkIndex ) {
            return null;
        } else {
            return this.fkIndex.getIndexName();
        }
    }

    @Override
    public String getForeignKeyTableName() {
        return this.fkTable.getTableName();
    }

    @Override
    public String getPrimaryKeyIndexName() {
        if ( null == this.pkIndex ) {
            return null;
        } else {
            return this.pkIndex.getIndexName();
        }
    }

    @Override
    public String getPrimaryKeyTableName() {
        if ( null == this.pkTable ) {
            return null;
        } else {
            return this.pkTable.getTableName();
        }
    }

    @Override
    public JdbcForeignKeyMetaDataBuilder build(Connection conn) throws SQLException {
        return this;
    }

    @Override
    protected JdbcForeignKeyMetaData createMetaData() {
        JdbcForeignKeyMetaDataImpl result = new JdbcForeignKeyMetaDataImpl(this.fkTable.getMetaData(), this);
        this.constructedMetaData = result;
        return constructedMetaData;
    }

    public void addEntry(MetaDataResultSet<AccessForeignKeyDbMetaDataEnum> importedKeys) throws SQLException {
        final String fkColumnName = importedKeys.get(AccessForeignKeyDbMetaDataEnum.szColumn);
        final String pkColumnName = importedKeys.get(AccessForeignKeyDbMetaDataEnum.szReferencedColumn);
        JdbcColumnMetaData fkColumn = this.fkTable.getColumnBuilder(fkColumnName);
        JdbcColumnMetaData pkColumn = this.pkTable.getColumnBuilder(pkColumnName);
        BaseForeignKeyMetaDataBuilder.Entry entry = new BaseForeignKeyMetaDataBuilder.Entry(fkColumn, pkColumn, this,
                importedKeys.getShort(AccessForeignKeyDbMetaDataEnum.icolumn));
        this.entryList.add(entry);
    }
}
