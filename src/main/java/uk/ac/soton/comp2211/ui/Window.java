package uk.ac.soton.comp2211.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.scene.*;

public abstract class Window {
    private static final Logger logger = LogManager.getLogger(Window.class);
    private final int width;
    private final int height;
    protected final Stage stage;
    protected BaseScene currentScene;
    protected Scene scene;
    public static String theme = "blue.css";

    public Window(Stage stage, int width, int height) {
        this.stage = stage;
        this.width = width;
        this.height = height;

        setupStage();

        setupDefault();

        loadDefaultScene();
    }

    public Scene getScene() {
        return scene;
    }

    abstract void setupStage();

    public void setupDefault() {
        scene = new Scene(new Pane(), width, height);

        stage.setScene(scene);
    }

    abstract void loadDefaultScene();

    public void loadScene(BaseScene nextScene) {
        cleanup();

        nextScene.build();
        currentScene = nextScene;
        scene = currentScene.setScene();
        scene.getStylesheets().add(theme);
        stage.setScene(scene);

        Platform.runLater(() -> currentScene.initialise());
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