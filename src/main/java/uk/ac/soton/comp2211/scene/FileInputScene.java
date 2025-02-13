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
import uk.ac.soton.comp2211.control.FileInputController;
import uk.ac.soton.comp2211.ui.Dialogs;
import uk.ac.soton.comp2211.ui.MainWindow;

public class FileInputScene extends MainScene {
    public FileInputScene(MainWindow window) {
        super(window);
    }

    @Override
    public void initialise() {
    }

    @Override
    public void build() {
        FileInputController controller = new FileInputController();

        root = new StackPane();
        root.setPadding(new Insets(225,500,225,500));

        var centreBox = new VBox();

        var uploadLabel = new Label("Upload files");
        uploadLabel.getStyleClass().add("form-title");

        var impressionLabel = new Label("Impression Log Path");
        var clickLabel = new Label("Click Log Path");
        var serverLabel = new Label("Server Log Path");

        var impressionField = new TextField();
        impressionField.getStyleClass().add("login-field");
        var clickField = new TextField();
        clickField.getStyleClass().add("login-field");
        var serverField = new TextField();
        serverField.getStyleClass().add("login-field");
        impressionField.setEditable(false);
        clickField.setEditable(false);
        serverField.setEditable(false);
        impressionField.textProperty().bindBidirectional(controller.impressionPathProperty());
        clickField.textProperty().bindBidirectional(controller.clickPathProperty());
        serverField.textProperty().bindBidirectional(controller.serverPathProperty());

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
        impressionExplorer.getStyleClass().add("outline-button");
        var clickExplorer = new Button("Browse");
        clickExplorer.getStyleClass().add("outline-button");
        var serverExplorer = new Button("Browse");
        serverExplorer.getStyleClass().add("outline-button");

        impressionBox.getChildren().addAll(impressionField, impressionExplorer);
        clickBox.getChildren().addAll(clickField, clickExplorer);
        serverBox.getChildren().addAll(serverField, serverExplorer);

        var uploadButton = new Button("Upload");
        uploadButton.getStyleClass().add("fill-button");

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.isIndeterminate();
        progressIndicator.setVisible(false);

        var buttonsHBox = new HBox();
        var backButton = new Button("Back");
        backButton.getStyleClass().add("outline-button");
        buttonsHBox.getChildren().addAll(backButton,uploadButton);
        buttonsHBox.setSpacing(10);
        buttonsHBox.setAlignment(Pos.CENTER);

        VBox impressionContainer = new VBox();
        impressionContainer.getChildren().addAll(impressionLabel, impressionBox);

        VBox clickContainer = new VBox();
        impressionContainer.getChildren().addAll(clickLabel, clickBox);

        VBox serverContainer = new VBox();
        impressionContainer.getChildren().addAll(serverLabel, serverBox);

        centreBox.getStyleClass().add("upload-container");
        centreBox.getChildren().addAll(uploadLabel, impressionContainer, clickContainer, serverContainer, buttonsHBox, progressIndicator);
        centreBox.setAlignment(Pos.CENTER);
        centreBox.setSpacing(10);
        root.getChildren().add(centreBox);

        impressionExplorer.setOnAction(event -> {
           event.consume();
           FileChooser chooser = new FileChooser();
           chooser.setInitialDirectory(new File("data"));

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
            chooser.setInitialDirectory(new File("data"));

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
            chooser.setInitialDirectory(new File("data"));

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

            Task<Void> task = controller.requestParse();

            task.setOnSucceeded(workerStateEvent -> {
                progressIndicator.setVisible(false);
                window.loadDashboard();
            });

            task.setOnFailed(workerStateEvent -> {
                progressIndicator.setVisible(false);

                Dialogs.error((Exception) task.getException());
                //throw new RuntimeException(task.getException());
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });

        backButton.setOnAction( e -> {
            window.switchToDashboard();
        });

    }

    @Override
    public void cleanup() {

    }
}
