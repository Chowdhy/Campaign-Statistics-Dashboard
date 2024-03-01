package uk.ac.soton.comp2211;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.ui.MainWindow;

import java.io.IOException;

/**
 * JavaFX App Test1
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    private static App instance;

    @Override
    public void start(Stage stage) {
        instance = this;

        var window = new MainWindow(stage, 640, 480);

        stage.show();

    }

    public void shutdown() {
        logger.info("Closing instance");
        System.exit(0);
    }

    public static App getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch();
    }
}