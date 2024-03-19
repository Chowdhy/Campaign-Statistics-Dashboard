package uk.ac.soton.comp2211.control;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import uk.ac.soton.comp2211.data.graph.GraphData;
import java.util.ArrayList;

public class HistogramController {

    GraphData graphData;

    public HistogramController() {
        this.graphData = new GraphData();
    }


    public void histogram(BarChart barChart) {
        barChart.getData().clear();
        ArrayList<Double> data = graphData.getClickCosts();

        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        for (Double value : data) {
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }

        int numBins = 10;
        double binWidth = (maxValue - minValue) / numBins;


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < numBins; i++) {
            double binStart = minValue + i * binWidth;
            double binEnd = binStart + binWidth;
            int frequency = countValuesInBin(data, binStart, binEnd);
            double frequencyPerWidth = (double) frequency / binWidth;
            String binLabel = String.format("%.2f - %.2f", binStart, binEnd);
            series.getData().add(new XYChart.Data<>(binLabel, frequencyPerWidth));
        }

        barChart.getData().add(series);
    }

    private int countValuesInBin(ArrayList<Double> data, double binStart, double binEnd) {
        int count = 0;
        for (Double value : data) {
            if (value >= binStart && value < binEnd) {
                count++;
            }
        }
        return count;
    }
}
