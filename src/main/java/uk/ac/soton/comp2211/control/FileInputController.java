package uk.ac.soton.comp2211.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import uk.ac.soton.comp2211.data.parsing.ClickLogParser;
import uk.ac.soton.comp2211.data.parsing.CsvParser;
import uk.ac.soton.comp2211.data.parsing.ImpressionParser;
import uk.ac.soton.comp2211.data.parsing.ServerLogParser;

public class FileInputController {
    @FXML
    private TextField impressionLogPath;
    @FXML
    private TextField clickLogPath;
    @FXML
    private TextField serverLogPath;

    public FileInputController() {
        System.out.println("Test");
    }
    @FXML
    void upload(ActionEvent event) {
        event.consume();

        String databaseName = "campaign";

        CsvParser impressionParser = new ImpressionParser(databaseName);
        CsvParser clickLogParser = new ClickLogParser(databaseName);
        CsvParser serverLogParser = new ServerLogParser(databaseName);

        try {
            impressionParser.parse(impressionLogPath.getText());
            clickLogParser.parse(clickLogPath.getText());
            serverLogParser.parse(serverLogPath.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
