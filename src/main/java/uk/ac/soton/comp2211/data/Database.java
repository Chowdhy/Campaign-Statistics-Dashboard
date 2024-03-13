package uk.ac.soton.comp2211.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private final static String url = "jdbc:sqlite:data/";

    private static final Logger logger = LogManager.getLogger(Database.class);

    public static Connection getConnection(String dbName) {
        Connection conn = null;

        try {
            SQLiteConfig config = new SQLiteConfig();
            config.setCacheSize(1000000);
            config.setPageSize(16000);
            config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
            config.setJournalMode(SQLiteConfig.JournalMode.OFF);
            config.setTempStore(SQLiteConfig.TempStore.MEMORY);
            config.setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE);
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(url + dbName + ".db", config.toProperties());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return conn;
    }
}