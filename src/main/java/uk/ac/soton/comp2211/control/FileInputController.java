package uk.ac.soton.comp2211.control;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import uk.ac.soton.comp2211.data.parsing.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileInputController {
    String databaseName = "campaign";
    CsvParser impressionLogParser;
    CsvParser clickLogParser;
    CsvParser serverLogParser;

    public FileInputController() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        impressionLogParser = new ImpressionLogParser(databaseName);
        clickLogParser = new ClickLogParser(databaseName);
        serverLogParser = new ServerLogParser(databaseName);
    }

    public StringProperty impressionPathProperty() {
        return impressionLogParser.pathProperty();
    }

    public StringProperty clickPathProperty() {
        return clickLogParser.pathProperty();
    }

    public StringProperty serverPathProperty() {
        return serverLogParser.pathProperty();
    }

    public Task<Void> requestParse() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                impressionLogParser.parse();
                clickLogParser.parse();
                serverLogParser.parse();
                return null;
            }
        };
    }
}
