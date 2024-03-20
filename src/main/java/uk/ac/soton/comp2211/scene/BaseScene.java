package uk.ac.soton.comp2211.scene;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import uk.ac.soton.comp2211.ui.Window;

public abstract class BaseScene {
    private final Window window;
    protected Pane root;
    protected Scene scene;

    public BaseScene(Window window) {
        this.window = window;
    }

    public abstract void initialise();

    public abstract void build();

    public Scene setScene() {
        var previous = window.getScene();
        Scene scene = new Scene(root, previous.getWidth(), previous.getHeight());
        this.scene = scene;
        return scene;
    }

    public Scene getScene() {
        return scene;
    }

    public abstract void cleanup();
}
