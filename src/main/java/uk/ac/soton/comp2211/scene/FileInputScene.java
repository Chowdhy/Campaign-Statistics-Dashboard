package uk.ac.soton.comp2211.scene;

import java.io.File;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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

        var containerPane = new StackPane();
        var centreBox = new VBox();
        containerPane.getChildren().add(centreBox);
        containerPane.setAlignment(Pos.CENTER);
        containerPane.setPadding(new Insets(100));
        root.getChildren().add(containerPane);
        centreBox.setAlignment(Pos.TOP_CENTER);
        centreBox.setMaxWidth(400);

        var impressionLabel = new Label("Impression Log Path");
        var clickLabel = new Label("Click Log Path");
        var serverLabel = new Label("Server Log Path");

        var impressionField = new TextField();
        var clickField = new TextField();
        var serverField = new TextField();

        var impressionBox = new HBox();
        var clickBox = new HBox();
        var serverBox = new HBox();
        VBox.setVgrow(impressionBox, Priority.ALWAYS);
        VBox.setVgrow(clickBox, Priority.ALWAYS);
        VBox.setVgrow(serverBox, Priority.ALWAYS);

        VBox.setMargin(impressionBox, new Insets(0, 0, 10, 0));
        VBox.setMargin(clickBox, new Insets(0, 0, 10, 0));
        VBox.setMargin(serverBox, new Insets(0, 0, 10, 0));

        HBox.setHgrow(impressionField, Priority.ALWAYS);
        HBox.setHgrow(clickField, Priority.ALWAYS);
        HBox.setHgrow(serverField, Priority.ALWAYS);

        var impressionExplorer = new Button("Browse");
        var clickExplorer = new Button("Browse");
        var serverExplorer = new Button("Browse");

        impressionBox.getChildren().addAll(impressionField, impressionExplorer);
        clickBox.getChildren().addAll(clickField, clickExplorer);
        serverBox.getChildren().addAll(serverField, serverExplorer);

        var uploadButton = new Button("Upload");

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.isIndeterminate();
        progressIndicator.setVisible(false);

        centreBox.getChildren().addAll(impressionLabel, impressionBox, clickLabel, clickBox, serverLabel, serverBox, uploadButton,progressIndicator);

        impressionExplorer.setOnAction(event -> {
           event.consume();
           FileChooser chooser = new FileChooser();
           chooser.setInitialDirectory(new File("data/sample_data"));

           File file = chooser.showOpenDialog(window.getStage());

           if (file == null) return;

           impressionField.setText(file.getAbsolutePath());
        });

        impressionField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                clickField.requestFocus();
            }
        });

        clickExplorer.setOnAction(event -> {
            event.consume();
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File("data/sample_data"));

            File file = chooser.showOpenDialog(window.getStage());

            if (file == null) return;

            clickField.setText(file.getAbsolutePath());
        });

        clickField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                serverField.requestFocus();
            }
        });

        serverExplorer.setOnAction(event -> {
            event.consume();
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File("data/sample_data"));

            File file = chooser.showOpenDialog(window.getStage());

            if (file == null) return;

            serverField.setText(file.getAbsolutePath());
        });

        serverField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                uploadButton.requestFocus();
            }
        });

        uploadButton.setOnAction(event -> {
            event.consume();
            progressIndicator.setVisible(true);

            String databaseName = "campaign";
            CsvParser impressionParser = new ImpressionParser(databaseName);
            CsvParser clickLogParser = new ClickLogParser(databaseName);
            CsvParser serverLogParser = new ServerLogParser(databaseName);

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    impressionParser.parse(impressionField.getText());
                    clickLogParser.parse(clickField.getText());
                    serverLogParser.parse(serverField.getText());
                    return null;
                }
            };

            task.setOnSucceeded(workerStateEvent -> {
                progressIndicator.setVisible(false);
                window.loadDashboard();
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

    }

    @Override
    public void cleanup() {

    }
}
