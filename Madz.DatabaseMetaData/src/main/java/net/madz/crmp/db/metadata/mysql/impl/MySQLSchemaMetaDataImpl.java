package net.madz.crmp.db.metadata.mysql.impl;

import java.util.Map;

import net.madz.crmp.db.metadata.DottedPath;
import net.madz.crmp.db.metadata.jdbc.JdbcTableMetaData;
import net.madz.crmp.db.metadata.jdbc.impl.JdbcSchemaMetaDataImpl;
import net.madz.crmp.db.metadata.mysql.MySqlEngine;

public class MySQLSchemaMetaDataImpl extends JdbcSchemaMetaDataImpl {

    private String charSet;
    private String collation;

    public MySQLSchemaMetaDataImpl(DottedPath schemaPath, Map tables, String charSet2, String collation2) {
        super(schemaPath, tables);
        this.charSet = charSet;
        this.collation = collation;
    }

    public String getCharSet() {
        return charSet;
    }

    public String getCollation() {
        return collation;
    }
}
