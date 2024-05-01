package uk.ac.soton.comp2211.ui;

import javafx.stage.Stage;
import uk.ac.soton.comp2211.control.UserManagementController;
import uk.ac.soton.comp2211.scene.ExportLogsScene;
import uk.ac.soton.comp2211.scene.UserManagementScene;

public class UserWindow extends Window {
    private UserManagementController controller;
    private UserManagementScene userManagementScene;

    public UserWindow(Stage stage, int width, int height) {
        super(stage, width, height);
    }

    @Override
    void setupStage() {
        stage.setTitle("User Management");

        stage.setResizable(false);
    }

    @Override
    void loadDefaultScene() {
        userManagementScene = new UserManagementScene(this);
        controller = new UserManagementController(userManagementScene);
        loadScene(userManagementScene);
    }

    public void loadExportLogsScene(){
        var exportLogsScene = new ExportLogsScene(this);
        loadScene(exportLogsScene);
    }

    public void loadUserManagementScene() {
        if (userManagementScene == null) {
            userManagementScene = new UserManagementScene(this);
            loadScene(userManagementScene);
        } else {
            cleanup();

            scene = userManagementScene.getScene();
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
        }
    }

    public UserManagementController getController() {
        return controller;
    }
}
