package net.madz.db.core;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;

/**
 * This class is responsible for creating DatabaseMetadata instance of specified
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class AbsSchemaMetaDataParser<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>> {

    protected final String databaseName;
    // TODO [Jan 22, 2013][barry] Reconsider resource lifecycle of Connection
    protected final Connection conn;

    /**
     * @param conn
     */
    public AbsSchemaMetaDataParser(String databaseName, Connection conn) {
        this.databaseName = databaseName;
        this.conn = conn;
    }

    /**
     * @param dbName
     * @return
     */
    public abstract SMD parseSchemaMetaData() throws SQLException;
}
