package uk.ac.soton.comp2211.scene;

import uk.ac.soton.comp2211.ui.UserWindow;

public abstract class UserScene extends BaseScene {
    protected UserWindow window;

    public UserScene(UserWindow window) {
        super(window);
        this.window = window;
    }
}
