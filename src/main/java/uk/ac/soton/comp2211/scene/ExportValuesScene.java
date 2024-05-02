package uk.ac.soton.comp2211.scene;

import java.io.File;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import uk.ac.soton.comp2211.control.FileInputController;
import uk.ac.soton.comp2211.data.Export.ReportExporter;
import uk.ac.soton.comp2211.ui.MainWindow;
import uk.ac.soton.comp2211.users.OperationLogging;


public class ExportValuesScene extends MainScene{
    private List<Number> dataArray;

    public ExportValuesScene(MainWindow window, List<Number> arr) {
        super(window);
        this.dataArray = arr;
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

        var exportLabel = new Label("Export Reports (report.csv)");
        exportLabel.getStyleClass().add("form-title");
        var incorrectPrompt = new Label();
        incorrectPrompt.setStyle("-fx-text-fill: red");
        var folderLabel = new Label("Export to File Path");

        var folderField = new TextField();
        folderField.getStyleClass().add("login-field");
        folderField.setEditable(false);


        var folderPathBox = new HBox();

        VBox.setVgrow(folderPathBox, Priority.ALWAYS);


        VBox.setMargin(folderPathBox, new Insets(0, 0, 10, 0));


        HBox.setHgrow(folderField, Priority.ALWAYS);


        var folderExplorer = new Button("Browse");
        folderExplorer.getStyleClass().add("outline-button");

        folderPathBox.getChildren().addAll(folderField, folderExplorer);

        var exportButton = new Button("Export Report");
        exportButton.getStyleClass().add("fill-button");

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.isIndeterminate();
        progressIndicator.setVisible(false);

        var buttonsHBox = new HBox();
        var backButton = new Button("Back");
        backButton.getStyleClass().add("outline-button");
        buttonsHBox.getChildren().addAll(backButton,exportButton, incorrectPrompt);
        buttonsHBox.setSpacing(10);
        buttonsHBox.setAlignment(Pos.CENTER);

        VBox folderPathContainer = new VBox();
        folderPathContainer.getChildren().addAll(folderLabel, folderPathBox);

        centreBox.getStyleClass().add("upload-container");
        centreBox.getChildren().addAll(exportLabel, folderPathContainer, buttonsHBox, progressIndicator);
        centreBox.setAlignment(Pos.CENTER);
        centreBox.setSpacing(10);
        root.getChildren().add(centreBox);

        folderExplorer.setOnAction(event -> {
            event.consume();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder");
            File selectedDirectory = directoryChooser.showDialog(window.getStage());

            if (selectedDirectory != null) {
                folderField.setText(selectedDirectory.getAbsolutePath());
            }
        });



        exportButton.setOnAction(event -> {
            OperationLogging.logAction("Exported metrics.");
            try {
                String folderPath = folderField.getText();
                ReportExporter.getReportCSV(folderPath, dataArray);
                window.switchToDashboard();
            }catch (Exception e){
                incorrectPrompt.setText("Exporting failed, make sure report.csv is not open");
            }
        });

        backButton.setOnAction( e -> {
            window.switchToDashboard();
        });

    }


    @Override
    public void cleanup() {

    }
}
