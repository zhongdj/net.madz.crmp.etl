package net.madz.crmp.db.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({})
public class DatabaseMetaDataTestSuite {
    static {
        System.setProperty("net.madz.db.configuration", "/test-databases.xml");
    }
}
