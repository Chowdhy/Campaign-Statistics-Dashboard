package uk.ac.soton.comp2211.data.parsing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class ImpressionParser extends CsvParser {
    private static final Logger logger = LogManager.getLogger(ImpressionParser.class);

    private static final String createTableSql = """
                CREATE TABLE impression_log (
                     date TEXT NOT NULL,\s
                     ID INTEGER NOT NULL,\s
                     gender TEXT CHECK (gender IN ('Male', 'Female')),\s
                     age TEXT CHECK (age IN ('<25', '25-34', '35-44', '45-54', '>54')),\s
                     income TEXT CHECK (income IN ('Low', 'Medium', 'High')),\s
                     context TEXT CHECK (context IN ('Shopping', 'News', 'Blog', 'Social Media', 'Hobbies', 'Travel')),\s
                     impression_cost FLOAT,\s
                     PRIMARY KEY (date, ID)
                );""";

    private static final String insertSql = "INSERT INTO impression_log(date, ID, gender, age, income, context, impression_cost) VALUES(?,?,?,?,?,?,?)";

    public ImpressionParser(String dbName) {
        super(dbName, "DROP TABLE IF EXISTS 'impression_log'", createTableSql, insertSql);
    }

    void insert(PreparedStatement prp, String[] line) {
        if (line.length != 7) {
            throw new RuntimeException("Malformed impression log file: " + path);
        }

        try {
            prp.setString(1, line[0].trim());
            prp.setLong(2, Long.parseLong(line[1].trim()));
            prp.setString(3, line[2].trim());
            prp.setString(4, line[3].trim());
            prp.setString(5, line[4].trim());
            prp.setString(6, line[5].trim());
            prp.setFloat(7, Float.parseFloat(line[6].trim()));
            prp.addBatch();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }
}
