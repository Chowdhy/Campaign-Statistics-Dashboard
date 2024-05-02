package uk.ac.soton.comp2211.scene;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import uk.ac.soton.comp2211.control.FileInputController;
import uk.ac.soton.comp2211.ui.MainWindow;
import uk.ac.soton.comp2211.users.OperationLogging;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ExportChartScene extends MainScene {
    WritableImage image;

    public ExportChartScene(MainWindow window, WritableImage image) {
        super(window);
        this.image = image;
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

            var exportLabel = new Label("Export Charts (chart.pdf)");
            exportLabel.getStyleClass().add("form-title");

            var folderLabel = new Label("Export to Path");

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

            var exportButton = new Button("Export");
            exportButton.getStyleClass().add("fill-button");

            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.isIndeterminate();
            progressIndicator.setVisible(false);

            var buttonsHBox = new HBox();
            var backButton = new Button("Back");
            var incorrectPrompt = new Label();
            incorrectPrompt.setStyle("-fx-text-fill: red");
            backButton.getStyleClass().add("outline-button");
            buttonsHBox.getChildren().addAll(backButton,exportButton);
            buttonsHBox.setSpacing(10);
            buttonsHBox.setAlignment(Pos.CENTER);

            VBox folderPathContainer = new VBox();
            folderPathContainer.getChildren().addAll(folderLabel, folderPathBox);

            centreBox.getStyleClass().add("upload-container");
            centreBox.getChildren().addAll(exportLabel, folderPathContainer, buttonsHBox, incorrectPrompt, progressIndicator);
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

                try {
                    OperationLogging.logAction("Exported summary chart");

                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(this.image, null);

                    // Create a new PDF document
                    PDDocument document = new PDDocument();

                    File outputFile = new File("image.png");
                    ImageIO.write(bufferedImage, "png", outputFile);
                    // Add a blank page to the document
                    PDPage page = new PDPage();
                    document.addPage(page);

                    PDPageContentStream contentStream = new PDPageContentStream(document, page);
                    PDImageXObject image = PDImageXObject.createFromFile("image.png", document);
                    PDRectangle pageSize = page.getMediaBox();
                    float pageWidth = pageSize.getWidth();
                    float pageHeight = pageSize.getHeight();

                    float imageWidth = image.getWidth();
                    float imageHeight = image.getHeight();

                    float scale = Math.min(pageWidth / imageHeight, pageHeight / imageWidth);
                    float scaledWidth = imageHeight * scale;
                    float scaledHeight = imageWidth * scale;



                    contentStream.transform(Matrix.getRotateInstance(Math.toRadians(90), 0, 0));


                    contentStream.drawImage(image,0, -scaledWidth, scaledHeight, scaledWidth);
                    contentStream.close();

                    String fileName = "chart.pdf";
                    String outputDirectory = folderField.getText();


                    String filePath = outputDirectory + File.separator + fileName;
                    document.save(filePath);


                    document.close();

                    incorrectPrompt.setStyle("-fx-text-fill: green");
                    incorrectPrompt.setText("Successfully exported metrics");
                } catch (IOException e) {
                    incorrectPrompt.setStyle("-fx-text-fill: red");
                    incorrectPrompt.setText("Error exporting, make sure chart.pdf is closed");
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
