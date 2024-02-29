package uk.ac.soton.comp2211.parsing;

import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

abstract class CsvParser {
    protected static final Logger logger = LogManager.getLogger(ImpressionParser.class);

    private final String createTableSql;

    private final String insertSql;

    private Connection connect() {
        String url = "jdbc:sqlite:data/campaign.db";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return conn;
    }

    public CsvParser(String createTableSql, String insertSql) {
        this.createTableSql = createTableSql;
        this.insertSql = insertSql;

        createTable();
    }

    private void createTable() {
        Connection conn = null;
        Statement stat = null;

        try {
            conn = this.connect();
            if (conn == null) return;

            stat = conn.createStatement();
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

    public void parse(String inputFilePath) {
        Path inputPath = Path.of(inputFilePath);

        Connection conn = null;
        PreparedStatement prp = null;

        try (Reader reader = Files.newBufferedReader(inputPath)) {
            conn = this.connect();
            if (conn == null) return;
            conn.setAutoCommit(false);
            prp = conn.prepareStatement(insertSql);

            CSVReader csvReader = new CSVReader(reader);
            String[] line;
            csvReader.readNextSilently();

            while (((line = csvReader.readNext()) != null)) {
                this.insert(prp, line);
            }

            int[] updateCounts = prp.executeBatch();

            conn.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (prp != null) prp.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    abstract void insert(PreparedStatement prp, String[] line);
}
