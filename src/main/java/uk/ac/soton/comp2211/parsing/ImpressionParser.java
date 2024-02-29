package uk.ac.soton.comp2211.parsing;

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
                     context TEXT CHECK (context IN ('Shopping', 'News', 'Blog', 'Social Media')),\s
                     impression_cost FLOAT,\s
                     PRIMARY KEY (date, ID)
                );""";

    private static final String insertSql = "INSERT INTO impression_log(date, ID, gender, age, income, context, impression_cost) VALUES(?,?,?,?,?,?,?)";

    public ImpressionParser() {
        super(createTableSql, insertSql);
    }

    void insert(PreparedStatement prp, String[] line) {
        if (line.length != 7) return;

        try {
            prp.setString(1, line[0]);
            prp.setLong(2, Long.parseLong(line[1]));
            prp.setString(3, line[2]);
            prp.setString(4, line[3]);
            prp.setString(5, line[4]);
            prp.setString(6, line[5]);
            prp.setFloat(7, Float.parseFloat(line[6]));
            prp.addBatch();
        } catch (SQLException e) {
          logger.error(e.getMessage());
        }
    }
}
