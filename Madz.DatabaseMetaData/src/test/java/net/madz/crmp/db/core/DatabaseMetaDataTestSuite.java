package net.madz.crmp.db.core;

import net.madz.crmp.db.core.impl.DatabaseGeneratorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({DatabaseGeneratorTest.class})
public class DatabaseMetaDataTestSuite {
    static {
        System.setProperty("net.madz.db.configuration", "/test-databases.xml");
    }
}
