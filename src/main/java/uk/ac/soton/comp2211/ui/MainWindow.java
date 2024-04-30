package uk.ac.soton.comp2211.ui;

import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.scene.*;

import java.util.List;

public class MainWindow extends Window {
    private static final Logger logger = LogManager.getLogger(MainWindow.class);
    private BaseScene dashboardScene;

    public MainWindow(Stage stage, int width, int height) {
        super(stage, width, height);
    }

    @Override
    void setupStage() {
        stage.setTitle("Ad Auction Dashboard");
        stage.setOnCloseRequest(e -> App.getInstance().shutdown());

        stage.setResizable(false);
    }

    @Override
    void loadDefaultScene() {
        loadLoginScene();
    }

    public void switchToDashboard() {
        if (dashboardScene == null) {
            dashboardScene = new DashboardScene(this);
            loadScene(dashboardScene);
        } else {
            cleanup();

            scene = dashboardScene.getScene();
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
        }
    }

    public void loadFileInput() {
        loadScene(new FileInputScene(this));
    }
    public void loadDashboard() {
        dashboardScene = new DashboardScene(this);
        loadScene(dashboardScene);
    }

    public void loadExportLogsScene(){
        var exportLogsScene = new ExportLogsScene(this);
        loadScene(exportLogsScene);
    }

    public void loadExportValuesScene(List<Number> arr){
        var exportValuesScene = new ExportValuesScene(this, arr);
        loadScene(exportValuesScene);
    }

    public void loadLoginScene() {
        loadScene(new LoginScene(this));
    }

    public void loadUserManagementScene() {
        try {
            Stage secondStage = new Stage();
            UserWindow userWindow = new UserWindow(secondStage, 600, 400);
            secondStage.show();
        } catch (Exception e) {

        }
    }
}
