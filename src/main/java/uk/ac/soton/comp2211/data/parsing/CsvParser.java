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
    }

    private Connection connect() {
        return Database.getConnection("campaign");
    }

    private void createTable() {
        try (Connection conn = Database.getConnection(databaseName);
             Statement stat = conn.createStatement()) {

            if (preamble != null) stat.execute(preamble);

            stat.execute(createTableSql);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void parse() throws Exception {
        createTable();

        try (Connection conn = connect();
             PreparedStatement prep = conn.prepareStatement(insertSql)) {
            conn.setAutoCommit(false);

            CsvParserSettings settings = new CsvParserSettings();
            com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);

            int counter = 0;

            parser.beginParsing(new File(path.get()));
            parser.parseNext();

            String[] line;
            while (((line = parser.parseNext()) != null)) {
                this.insert(prep, line);

                counter++;
                if (counter % 1000 == 0) {
                    prep.executeBatch();
                }
            }

            prep.executeBatch();
            conn.commit();
        }
    }

    abstract void insert(PreparedStatement prp, String[] line);

    public StringProperty pathProperty() {
        return path;
    }
}
