package uk.ac.soton.comp2211.scene;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.data.parsing.ClickLogParser;
import uk.ac.soton.comp2211.data.parsing.CsvParser;
import uk.ac.soton.comp2211.data.parsing.ImpressionParser;
import uk.ac.soton.comp2211.data.parsing.ServerLogParser;
import uk.ac.soton.comp2211.ui.MainWindow;

public class FileInputScene extends BaseScene {
    public FileInputScene(MainWindow window) {
        super(window);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        root = new VBox();

        var optionsDrop = new Menu("Options");
        var menuBar = new MenuBar(optionsDrop);

        root.getChildren().add(menuBar);
        VBox.setVgrow(menuBar, Priority.NEVER);

        var containerPane = new AnchorPane();
        var centreBox = new VBox();
        containerPane.getChildren().add(centreBox);
        root.getChildren().add(containerPane);
        centreBox.setAlignment(Pos.TOP_CENTER);

        var impressionLabel = new Label("Impression Log Path");
        var clickLabel = new Label("Click Log Path");
        var serverLabel = new Label("Server Log Path");

        var impressionField = new TextField("data/sample_data/2_week_campaign/impression_log.csv");
        var clickField = new TextField("data/sample_data/2_week_campaign/click_log.csv");
        var serverField = new TextField("data/sample_data/2_week_campaign/server_log.csv");

        var uploadButton = new Button("Upload");

        centreBox.getChildren().addAll(impressionLabel, impressionField, clickLabel, clickField, serverLabel, serverField, uploadButton);

        uploadButton.setOnAction(event -> {
            event.consume();
            String databaseName = "campaign";
            CsvParser impressionParser = new ImpressionParser(databaseName);
            CsvParser clickLogParser = new ClickLogParser(databaseName);
            CsvParser serverLogParser = new ServerLogParser(databaseName);

            try {
                impressionParser.parse(impressionField.getText());
                clickLogParser.parse(clickField.getText());
                serverLogParser.parse(serverField.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            window.loadDashboard();
        });
    }

    @Override
    public void cleanup() {

    }
}
