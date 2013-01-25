package net.madz.db.core.meta.mutable.mysql.impl;

import java.sql.ResultSet;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.impl.MetaDataResultSet;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLEngineEnum;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLTableTypeEnum;
import net.madz.db.core.meta.immutable.type.TableType;
import net.madz.db.core.meta.mutable.base.impl.TableMetaDataBuilderImpl;
import net.madz.db.core.meta.mutable.mysql.MySQLColumnMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLForeignKeyMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLIndexMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLSchemaMetaDataBuilder;
import net.madz.db.core.meta.mutable.mysql.MySQLTableMetaDataBuilder;

public class MySQLTableMetaDataBuilderImpl
        extends
        TableMetaDataBuilderImpl<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData>
        implements MySQLTableMetaDataBuilder {

    private MySQLTableTypeEnum tableType;
    private MySQLEngineEnum engine;
    private String characterSet;
    private String collation;

    public MySQLTableMetaDataBuilderImpl(MySQLSchemaMetaData schema) {
        super(schema);
    }

    public void setTablePath(DottedPath tablePath) {
        this.tablePath = tablePath;
    }

    public void setType(TableType type) {
        this.type = type;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public MySQLTableMetaDataBuilder build( MetaDataResultSet<MySQLTableDbMetaDataEnum> rs) {
        
        return null;
    }

    @Override
    public MySQLTableTypeEnum getTableType() {
        return this.tableType;
    }

    @Override
    public MySQLEngineEnum getEngine() {
        return this.engine;
    }

    @Override
    public String getCharacterSet() {
        return this.characterSet;
    }

    @Override
    public String getCollation() {
        return this.collation;
    }
}
