package net.madz.db.core.meta.immutable.mysql.impl;

import net.madz.db.core.meta.immutable.impl.IndexMetaDataImpl;
import net.madz.db.core.meta.immutable.mysql.MySQLColumnMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLForeignKeyMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLIndexMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLSchemaMetaData;
import net.madz.db.core.meta.immutable.mysql.MySQLTableMetaData;
import net.madz.db.core.meta.immutable.mysql.enums.MySQLIndexMethod;

public final class MySQLIndexMetaDataImpl extends
        IndexMetaDataImpl<MySQLSchemaMetaData, MySQLTableMetaData, MySQLColumnMetaData, MySQLForeignKeyMetaData, MySQLIndexMetaData> implements
        MySQLIndexMetaData {

    private final boolean isNull;
    private final MySQLIndexMethod indexMethod;
    private final String indexComment;

    public MySQLIndexMetaDataImpl(MySQLTableMetaData parent, MySQLIndexMetaData metaData) {
        super(parent, metaData);
        this.isNull = metaData.isNull();
        this.indexMethod = metaData.getIndexMethod();
        this.indexComment = metaData.getIndexComment();
    }

    @Override
    public boolean isNull() {
        return this.isNull;
    }

    @Override
    public MySQLIndexMethod getIndexMethod() {
        return this.indexMethod;
    }

    @Override
    public String getIndexComment() {
        return this.indexComment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( indexComment == null ) ? 0 : indexComment.hashCode() );
        result = prime * result + ( ( indexMethod == null ) ? 0 : indexMethod.hashCode() );
        result = prime * result + ( isNull ? 1231 : 1237 );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( !super.equals(obj) ) return false;
        if ( getClass() != obj.getClass() ) return false;
        MySQLIndexMetaDataImpl other = (MySQLIndexMetaDataImpl) obj;
        if ( indexComment == null ) {
            if ( other.indexComment != null ) return false;
        } else if ( !indexComment.equals(other.indexComment) ) return false;
        if ( indexMethod != other.indexMethod ) return false;
        if ( isNull != other.isNull ) return false;
        return true;
    }
}
