package net.madz.db.core.meta.mutable;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLBasedMetaDataBuilder<T> {

    T build(Connection conn) throws SQLException;
}
