package uk.ac.soton.comp2211.data.parsing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServerLogParser extends CsvParser {
    private static final Logger logger = LogManager.getLogger(ServerLogParser.class);

    private static final String createTableSql = """
                CREATE TABLE server_log (
                     entry_date TEXT NOT NULL,\s
                     ID INTEGER NOT NULL,\s
                     exit_date TEXT NOT NULL,\s
                     pages_viewed INTEGER,\s
                     conversion TEXT CHECK (conversion IN ('Yes', 'No')),\s
                     log_ID INTEGER,\s
                     PRIMARY KEY (log_ID)
                );""";

    private static final String insertSql = "INSERT INTO server_log(entry_date, ID, exit_date, pages_viewed, conversion) VALUES(?,?,?,?,?)";

    public ServerLogParser(String dbName) {
        super(dbName, "DROP TABLE IF EXISTS 'server_log'", createTableSql, insertSql);
    }

    void insert(PreparedStatement prp, String[] line) {
        if (line.length != 5) {
            throw new RuntimeException("Malformed server log file: " + path.get());
        }

        try {
            prp.setString(1, line[0].trim());
            prp.setLong(2, Long.parseLong(line[1].trim()));
            prp.setString(3, line[2].trim());
            prp.setInt(4, Integer.parseInt(line[3].trim()));
            prp.setString(5, line[4].trim());
            prp.addBatch();
        } catch (SQLException e) {
          logger.error(e.getMessage());
        }
    }
}