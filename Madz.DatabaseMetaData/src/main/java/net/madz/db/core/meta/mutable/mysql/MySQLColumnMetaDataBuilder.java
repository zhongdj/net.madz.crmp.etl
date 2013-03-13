package net.madz.db.core.meta.mutable.mysql;

import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.datatype.DataType;
import net.madz.db.core.meta.mutable.ColumnMetaDataBuilder;

public interface MySQLColumnMetaDataBuilder
        extends
        MySQLColumnMetaData,
        ColumnMetaDataBuilder<MySQLSchemaMetaDataBuilder, MySQLTableMetaDataBuilder, MySQLColumnMetaDataBuilder, MySQLForeignKeyMetaDataBuilder, MySQLIndexMetaDataBuilder, MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> {

    public abstract MySQLColumnMetaDataBuilder appendForeignKeyEntry(
            ForeignKeyMetaData.Entry<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> entry);

    public abstract void setExtra(String extra);

    public abstract void setColumnKey(String columnKey);

    public abstract void setCollationName(String collationName);

    public abstract void setNumericScale(int numericScale);

    public abstract void setNumericPrecision(int numericPrecision);

    public abstract void setCharacterMaximumLength(long characterMaximumLength);

    public abstract void setColumnType(String columnType);

    public abstract void setCharacterSet(String characterSet);

    void setUnsigned(Boolean isUnsigned);

    void setZeroFill(Boolean isZeroFill);

    void setCollationWithBin(Boolean isCollationWithBin);

    void addTypeValue(String typeValue);

    void setDataType(DataType dataType);
}
