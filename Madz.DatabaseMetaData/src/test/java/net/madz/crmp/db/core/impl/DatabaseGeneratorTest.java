package net.madz.crmp.db.core.impl;

import junit.framework.Assert;
import net.madz.crmp.db.core.AbsDatabaseGenerator;
import net.madz.crmp.db.core.AbsDatabaseGeneratorTest;


public class DatabaseGeneratorTest extends AbsDatabaseGeneratorTest {

    @Override
    protected AbsDatabaseGenerator create() {
        String property = System.getProperty("net.madz.db.configuration");
        Assert.assertEquals("/test-databases.xml", property);
        return null;
    }
}
