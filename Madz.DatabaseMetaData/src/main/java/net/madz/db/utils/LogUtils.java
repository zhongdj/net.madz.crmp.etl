package net.madz.db.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogUtils {

    static {
        PropertyConfigurator.configure("conf/properties/ServerWithLog4j.properties");
    }

    @SuppressWarnings("rawtypes")
    public static void debug(Class cls, String message) {
        Logger.getLogger(cls).debug(message);
    }

    @SuppressWarnings("rawtypes")
    public static void error(Class cls, Throwable e) {
        Logger.getLogger(cls).error(e.getMessage(), e);
    }
}
