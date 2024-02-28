package uk.ac.soton.comp2211.parsing;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class ImpressionParser implements CsvParser {
    private Connection connect(String path) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(path);
        } catch (Exception e) {

        }

        return conn;
    }

    @Override
    public void parse(String inputFilePath) throws Exception {
        List<String[]> lines = new ArrayList<>();

        Path inputPath = Path.of(inputFilePath);

        String createTableSql = """
                CREATE TABLE impression_log (
                     date TEXT NOT NULL,\s
                     ID TEXT NOT NULL,\s
                     gender TEXT CHECK (gender IN ('Male', 'Female')),\s
                     age TEXT CHECK (age IN ('<25', '25-34', '35-44', '45-54', '>54')),\s
                     income TEXT CHECK (income IN ('Low', 'Medium', 'High')),\s
                     context TEXT CHECK (context IN ('Shopping', 'News', 'Blog', 'Social Media')),\s
                     impression_cost FLOAT,\s
                     PRIMARY KEY (date, ID)
                );""";

        String insertSql = "";


        try (Connection conn = connect("jdbc:sqlite:data/campaign.db");
                Statement stat = conn.createStatement()) {
           System.out.println(createTableSql);
           System.out.println(insertSql);
            stat.execute(createTableSql);
        }

        try (Reader reader = Files.newBufferedReader(inputPath)) {
            String sql = "INSERT INTO impression_log(date, ID, gender, age, income, context, impression_cost) VALUES(?,?,?,?,?,?,?)";
            try (CSVReader csvReader = new CSVReader(reader);
            Connection conn = this.connect("jdbc:sqlite:data/campaign.db");
            PreparedStatement prp = conn.prepareStatement(sql)) {
                String[] line;
                var c = 0;

                csvReader.readNextSilently();

                while (((line = csvReader.readNext()) != null)) {

                    this.insert(prp, line[0], line[1], line[2], line[3], line[4], line[5], Float.valueOf(line[6]));
                    c++;
                }
                conn.commit();
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void insert(PreparedStatement prp, String date, String ID, String gender, String age, String income, String context, Float cost) {

        try {
            prp.setString(1, date);
            prp.setString(2, ID);
            prp.setString(3, gender);
            prp.setString(4, age);
            prp.setString(5, income);
            prp.setString(6, context);
            prp.setFloat(7, cost);
            prp.addBatch();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
    }
}
