package uk.ac.soton.comp2211;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

import java.io.IOException;

/**
 * JavaFX App Test1
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        /*
        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);*/

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/uk/ac/soton/comp2211/FileUploadView.fxml"));
            var scene = new Scene(root, 640, 400);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }


    }

    public static void main(String[] args) {
        launch();
    }
}