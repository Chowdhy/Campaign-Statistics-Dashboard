package uk.ac.soton.comp2211;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.data.parsing.ClickLogParser;
import uk.ac.soton.comp2211.data.parsing.CsvParser;
import uk.ac.soton.comp2211.data.parsing.ImpressionParser;
import uk.ac.soton.comp2211.data.parsing.ServerLogParser;

/**
 * JavaFX App Test1
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private static void setupCampaignDatabase(String impressionLogPath, String clickLogPath, String serverLogPath) {
        String databaseName = "campaign";

        CsvParser impressionParser = new ImpressionParser(databaseName);
        CsvParser clickLogParser = new ClickLogParser(databaseName);
        CsvParser serverLogParser = new ServerLogParser(databaseName);

        try {
            impressionParser.parse(impressionLogPath);
            clickLogParser.parse(clickLogPath);
            serverLogParser.parse(serverLogPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        var impressionPath = "data/sample_data/2_week_campaign/impression_log.csv";
        var clickPath = "data/sample_data/2_week_campaign/click_log.csv";
        var serverPath = "data/sample_data/2_week_campaign/server_log.csv";

        setupCampaignDatabase(impressionPath, clickPath, serverPath);

        launch();
    }
}