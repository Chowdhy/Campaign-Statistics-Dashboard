package uk.ac.soton.comp2211.scene;

import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import uk.ac.soton.comp2211.control.HistogramController;
import uk.ac.soton.comp2211.ui.MainWindow;



public class HistogramScene extends MainScene {
    BarChart<String, Number> histogram;
    HistogramController hst = new HistogramController();

    public HistogramScene(MainWindow window){
        super(window);
    }

    @Override
    public void initialise() {


    }

    @Override
    public void build() {
        root = new VBox();
        StackPane stp = new StackPane();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        histogram = new BarChart<>(xAxis, yAxis);
        histogram.setLegendVisible(false);
        hst.histogram(histogram);
        stp.getChildren().add(histogram);
        stp.centerShapeProperty();
        root.getChildren().add(stp);
        var buttons = new HBox();
        var back = new Button("Back");
        back.setOnAction( e -> window.loadDashboard());
        buttons.getChildren().add(back);
        root.getChildren().add(buttons);
        buttons.setAlignment(Pos.CENTER);

        histogram.setTitle("Histogram of Click costs(Cost Distribution)");
        xAxis.setLabel("Ranges of click costs");
        yAxis.setLabel("Frequency Density");

    }


    @Override
    public void cleanup() {

    }
}
