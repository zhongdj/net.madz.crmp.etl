package net.madz.db.core;

import net.madz.db.core.meta.DottedPath;
import net.madz.db.core.meta.immutable.mysql.impl.MySQLSchemaMetaDataImpl;

import org.junit.Test;

import com.gargoylesoftware.base.testing.EqualsTester;

public class MySQLSchemaMetaDataImplTest {

    @Test
    public void testEqualsObject() {
        MySQLSchemaMetaDataImpl a = new MySQLSchemaMetaDataImpl(new DottedPath("crmp"), null, null);
        MySQLSchemaMetaDataImpl b = new MySQLSchemaMetaDataImpl(new DottedPath("crmp"), null, null);
        MySQLSchemaMetaDataImpl c = new MySQLSchemaMetaDataImpl(new DottedPath("crmp"), "utf8", null);
        MySQLSchemaMetaDataImpl d = null;
        new EqualsTester(a, b, c, d);
    }
}
