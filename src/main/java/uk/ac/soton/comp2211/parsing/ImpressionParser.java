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
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class ImpressionParser implements CsvParser {
    @Override
    public void parse(String inputFilePath) throws Exception {
        List<String[]> lines = new ArrayList<>();

        Path inputPath = Path.of(inputFilePath);

        try (Reader reader = Files.newBufferedReader(inputPath)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    lines.add(line);
                }
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
        }

        String createTableSql = """
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


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:data/campaign.db");
                Statement stat = conn.createStatement()) {
            stat.execute(createTableSql);
        }
    }
}
