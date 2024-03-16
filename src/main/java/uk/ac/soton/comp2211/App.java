package uk.ac.soton.comp2211;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.ui.MainWindow;
import uk.ac.soton.comp2211.users.User;

/**
 * JavaFX App Test1
 */
public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static App instance;
    private static User currentUser;

    @Override
    public void start(Stage stage) {
        instance = this;
        //        var screen = Screen.getPrimary();
        //        var bounds = screen.getVisualBounds();
        //
        //        var screenWidth = (int) bounds.getWidth();
        //        var screenHeight = (int) bounds.getHeight();
        //
        //
        //        MainWindow window = new MainWindow(stage, screenWidth, screenHeight);

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

    public static void setUser(User user) {
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        launch();
    }
}