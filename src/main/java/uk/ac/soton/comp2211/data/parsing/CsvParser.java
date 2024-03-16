package uk.ac.soton.comp2211.data.parsing;

import com.univocity.parsers.csv.CsvParserSettings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.data.Database;

import java.io.File;
import java.sql.*;

public abstract class CsvParser {
    private static final Logger logger = LogManager.getLogger(CsvParser.class);
    private final String createTableSql;
    private final String insertSql;
    private final String databaseName;
    private String preamble = null;
    protected StringProperty path = new SimpleStringProperty();

    public CsvParser(String dbName, String createTableSql, String insertSql) {
        this.databaseName = dbName;
        this.createTableSql = createTableSql;
        this.insertSql = insertSql;

        createTable();
    }

    public CsvParser(String dbName, String preamble, String createTableSql, String insertSql) {
        this.databaseName = dbName;
        this.preamble = preamble;
        this.createTableSql = createTableSql;
        this.insertSql = insertSql;

        createTable();
    }

    private void createTable() {
        Connection conn = null;
        Statement stat = null;

        try {
            conn = Database.getConnection(databaseName);
            if (conn == null) return;

            stat = conn.createStatement();

            if (preamble != null) stat.execute(preamble);

            stat.execute(createTableSql);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (stat != null) stat.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void parse() throws Exception {
        Connection conn = null;
        PreparedStatement prp = null;

        try {
            conn = Database.getConnection(databaseName);
            if (conn == null) return;
            conn.setAutoCommit(false);
            prp = conn.prepareStatement(insertSql);

            CsvParserSettings settings = new CsvParserSettings();
            com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);

            int counter = 0;

            parser.beginParsing(new File(path.get()));
            parser.parseNext();

            String[] line;
            while (((line = parser.parseNext()) != null)) {
                this.insert(prp, line);

                counter++;
                if (counter % 1000 == 0) {
                    prp.executeBatch();
                }
            }

            prp.executeBatch();
            conn.commit();
        } finally {
            try {
                if (prp != null) prp.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    abstract void insert(PreparedStatement prp, String[] line);

    public StringProperty pathProperty() {
        return path;
    }
}
