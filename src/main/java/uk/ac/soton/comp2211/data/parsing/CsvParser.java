package uk.ac.soton.comp2211.data.parsing;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.data.Database;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public abstract class CsvParser {
    private static final Logger logger = LogManager.getLogger(CSVParser.class);
    private final String createTableSql;
    private final String insertSql;
    private final String databaseName;
    private String preamble = null;
    protected String path;

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

    public void parse(String inputFilePath) throws Exception {
        Connection conn = null;
        PreparedStatement prp = null;

        path = inputFilePath;
        Path inputPath = Path.of(inputFilePath);
        Reader reader = null;
        CSVReader csvReader = null;

        try {
            conn = Database.getConnection(databaseName);
            if (conn == null) return;
            conn.setAutoCommit(false);
            prp = conn.prepareStatement(insertSql);

            reader = Files.newBufferedReader(inputPath);
            csvReader = new CSVReader(reader);
            String[] line;
            csvReader.readNextSilently();

            while (((line = csvReader.readNext()) != null)) {
                this.insert(prp, line);
            }

            prp.executeBatch();

            conn.commit();
        } finally {
            try {
                if (prp != null) prp.close();
                if (conn != null) conn.close();
                if (csvReader != null) csvReader.close();
                if (reader != null) reader.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    abstract void insert(PreparedStatement prp, String[] line);
}
