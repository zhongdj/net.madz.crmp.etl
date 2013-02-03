package net.madz.db.core;

import java.sql.Connection;
import java.sql.SQLException;

import net.madz.db.core.meta.immutable.ColumnMetaData;
import net.madz.db.core.meta.immutable.ForeignKeyMetaData;
import net.madz.db.core.meta.immutable.IndexMetaData;
import net.madz.db.core.meta.immutable.SchemaMetaData;
import net.madz.db.core.meta.immutable.TableMetaData;

/**
 * This class is responsible for cloning the database schema into MySQL
 * database.
 * 
 * @author tracy
 * 
 */
public abstract class AbsDatabaseGenerator<SMD extends SchemaMetaData<SMD, TMD, CMD, FMD, IMD>, TMD extends TableMetaData<SMD, TMD, CMD, FMD, IMD>, CMD extends ColumnMetaData<SMD, TMD, CMD, FMD, IMD>, FMD extends ForeignKeyMetaData<SMD, TMD, CMD, FMD, IMD>, IMD extends IndexMetaData<SMD, TMD, CMD, FMD, IMD>> {

    // TODO [Jan 22, 2013][barry] Reconsider resource lifecycle
    // protected final Connection conn;
    /**
     * @param conn
     */
    public AbsDatabaseGenerator() {
    }

    /**
     * @param metadata
     * @return generated database name
     */
    public abstract String generateDatabase(SMD metadata, Connection conn, String targetDatabaseName) throws SQLException;
}
