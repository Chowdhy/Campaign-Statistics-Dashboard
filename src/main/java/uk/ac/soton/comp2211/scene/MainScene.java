package uk.ac.soton.comp2211.scene;

import uk.ac.soton.comp2211.ui.MainWindow;

public abstract class MainScene extends BaseScene {
    protected MainWindow window;

    public MainScene(MainWindow window) {
        super(window);
        this.window = window;
    }
}
