package uk.ac.soton.comp2211.data.parsing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClickLogParser extends CsvParser {
    private static final Logger logger = LogManager.getLogger(ClickLogParser.class);

    private static final String createTableSql = """
                CREATE TABLE click_log (
                     date TEXT NOT NULL,\s
                     ID INTEGER NOT NULL,\s
                     click_cost FLOAT,\s
                     log_ID INTEGER,\s
                     PRIMARY KEY (log_ID)
                );""";

    private static final String insertSql = "INSERT INTO click_log(date, ID, click_cost) VALUES(?,?,?)";

    public ClickLogParser(String dbName) {
        super(dbName, "DROP TABLE IF EXISTS 'click_log'", createTableSql, insertSql);
    }

    void insert(PreparedStatement prp, String[] line) {
        if (line.length != 3) {
            throw new RuntimeException("Malformed click log file: " + path);
        }

        try {
            prp.setString(1, line[0].trim());
            prp.setLong(2, Long.parseLong(line[1].trim()));
            prp.setFloat(3, Float.parseFloat(line[2].trim()));
            prp.addBatch();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}