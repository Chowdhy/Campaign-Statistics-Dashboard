package uk.ac.soton.comp2211.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.scene.*;

public class MainWindow {
    private static final Logger logger = LogManager.getLogger(MainWindow.class);
    private final int width;
    private final int height;
    private final Stage stage;
    private BaseScene currentScene;
    private Scene scene;
    private BaseScene dashboardScene;

    public MainWindow(Stage stage, int width, int height) {
        this.stage = stage;
        this.width = width;
        this.height = height;

        setupStage();

        setupDefault();

        loadLoginScene();
    }

    public Scene getScene() {
        return scene;
    }

    public void setupStage() {
        stage.setTitle("Ad Auction Dashboard");
        stage.setOnCloseRequest(e -> App.getInstance().shutdown());

        stage.setResizable(false);
    }

    public void setupDefault() {
        scene = new Scene(new Pane(), width, height);

        stage.setScene(scene);
    }

    public void loadScene(BaseScene nextScene) {
        cleanup();

        nextScene.build();
        currentScene = nextScene;
        scene = currentScene.setScene();
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);

        Platform.runLater(() -> currentScene.initialise());
    }

    public void switchToDashboard() {
        if (dashboardScene == null) {
            dashboardScene = new DashboardScene(this);
        }

        cleanup();

        scene = dashboardScene.getScene();
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
    }

    public void loadFileInput() {
        loadScene(new FileInputScene(this));
    }
    public void loadDashboard() {
        dashboardScene = new DashboardScene(this);
        loadScene(dashboardScene);
    }


    public void loadLoginScene() {
        loadScene(new LoginScene(this));
    }

    public void loadAddUserScene() {
        loadScene(new AddUserScene(this));
    }

    public void loadModifyUserScene() {
        loadScene(new ModifyUserScene(this));
    }

    public void loadDeleteUserScene() {
        loadScene(new DeleteUserScene(this));
    }

    public void loadUserManagementScene() {
        loadScene(new UserManagementScene(this));
    }
    public void cleanup() {
        if (currentScene != null) currentScene.cleanup();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Stage getStage() {
        return stage;
    }
}
