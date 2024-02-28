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

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite::resource:campaign_data" + File.separator + "impression_log.db")) {

        }

    }
}
