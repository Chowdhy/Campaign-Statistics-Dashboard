package uk.ac.soton.comp2211.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.SQLiteConfig;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private final static String url = "jdbc:sqlite:data/";

    private static final Logger logger = LogManager.getLogger(Database.class);

    public static Connection getConnection(String dbName) {
        Connection conn = null;

        try {
            Files.createDirectories(Paths.get("data"));

            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(url + dbName + ".db", config.toProperties());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return conn;
    }
}