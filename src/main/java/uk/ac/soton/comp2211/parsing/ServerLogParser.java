package uk.ac.soton.comp2211.parsing;

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
                     PRIMARY KEY (entry_date, ID)
                );""";

    private static final String insertSql = "INSERT INTO server_log(entry_date, ID, exit_date, pages_viewed, conversion) VALUES(?,?,?,?,?)";

    public ServerLogParser() {
        super(createTableSql, insertSql);
    }

    void insert(PreparedStatement prp, String[] line) {
        if (line.length != 5) return;

        try {
            prp.setString(1, line[0]);
            prp.setLong(2, Long.parseLong(line[1]));
            prp.setString(3, line[2]);
            prp.setInt(4, Integer.parseInt(line[3]));
            prp.setString(5, line[4]);
            prp.addBatch();
        } catch (SQLException e) {
          logger.error(e.getMessage());
        }
    }
}