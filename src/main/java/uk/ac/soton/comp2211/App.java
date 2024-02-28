package uk.ac.soton.comp2211;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.parsing.ImpressionParser;

import java.io.File;




/**
 * JavaFX App
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

    public static void main(String[] args) {

        File myObj = new File("data" + File.separator + "campaign.db");

        if (myObj.delete()) {
            logger.info("Successfully deleted campaign.db");
        }

        ImpressionParser parser = new ImpressionParser();
        try {
            parser.parse("C:\\Users\\mahdi\\Downloads\\2_week_campaign_1\\2_week_campaign_2\\impression_log.csv");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        launch();
    }

}