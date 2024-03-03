package uk.ac.soton.comp2211.scene;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import uk.ac.soton.comp2211.data.graph.GraphData;
import uk.ac.soton.comp2211.ui.MainWindow;

import java.util.ArrayList;

public class DashboardScene extends BaseScene {
    public DashboardScene(MainWindow window) {
        super(window);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        root = new VBox();

        Menu userManagementMenu = new Menu("User Management");
        Menu chartSettingsMenu = new Menu("Chart settings");
        Menu fileSettingsMenu = new Menu("File Settings");
        Menu exportMenu = new Menu("Export");
        MenuBar menuBar = new MenuBar(userManagementMenu,chartSettingsMenu,fileSettingsMenu,exportMenu);



        root.getChildren().add(menuBar);

        SplitPane splitPane = new SplitPane();
        root.getChildren().add(splitPane);
        SplitPane leftSplitPane = new SplitPane();

        splitPane.setDividerPosition(0, 0.7);
        leftSplitPane.setDividerPosition(0,0.68);

        VBox metricsVBox = new VBox();
        metricsVBox.setSpacing(11);
        Label impressionsText = new Label(" Num of impressions: x");
        Label uniquesText = new Label(" Num of uniques: x");
        Label clicksText = new Label(" Num of clicks: x");
        Label bouncesText = new Label(" Num of bounces: x");
        Label conversionsText = new Label(" Num of conversions: x");
        Label totalCostText = new Label(" Total cost: x");
        Label ctrText = new Label(" CTR: x");
        Label cpaText = new Label(" CPA: x");
        Label cpcText = new Label(" CPC: x");
        Label cpmText = new Label(" CPM: x");
        Label bounceRateText = new Label(" Bounce rate: x");

        Separator separator1 = new Separator();
        Separator separator2 = new Separator();
        Separator separator3 = new Separator();
        Separator separator4 = new Separator();
        Separator separator5 = new Separator();
        Separator separator6 = new Separator();
        Separator separator7 = new Separator();
        Separator separator8 = new Separator();
        Separator separator9 = new Separator();
        Separator separator10 = new Separator();


        metricsVBox.getChildren().addAll(
                impressionsText, separator1, uniquesText, separator2, clicksText, separator3,
                bouncesText, separator4, conversionsText, separator5, totalCostText, separator6,
                ctrText, separator7, cpaText, separator8, cpcText, separator9,
                cpmText, separator10, bounceRateText
        );


        splitPane.getItems().addAll(leftSplitPane, metricsVBox);


        HBox filterHBox = new HBox();
        filterHBox.setSpacing(20);
        VBox bounceFilter = new VBox();
        VBox genderFilters = new VBox();
        VBox incomeFilters = new VBox();
        VBox contextFilters = new VBox();
        VBox ageGroupFilters = new VBox();

        Label bounceLabel = new Label("Bounce");
        RadioButton timeBounceButton = new RadioButton("Time");
        RadioButton singlePageBounceButton = new RadioButton("Pages");
        bounceFilter.getChildren().addAll(bounceLabel,timeBounceButton,singlePageBounceButton);

        Label genderLabel = new Label("Gender");
        CheckBox maleButton = new CheckBox("Male");
        CheckBox femaleButton = new CheckBox("Female");
        genderFilters.getChildren().addAll(genderLabel,maleButton,femaleButton);

        Label incomeLabel = new Label("Income");
        CheckBox lowButton = new CheckBox("Low");
        CheckBox mediumButton = new CheckBox("Medium");
        CheckBox highButton = new CheckBox("High");
        incomeFilters.getChildren().addAll(incomeLabel,lowButton,mediumButton,highButton);

        Label ageGroupLabel = new Label("Age group");
        CheckBox under25Button = new CheckBox("Under 25");
        CheckBox twentiesButton = new CheckBox("25-34");
        CheckBox thirtiesButton = new CheckBox("35-44");
        CheckBox fortiesButton = new CheckBox("45-54");
        CheckBox above54Button = new CheckBox("Above 54");
        ageGroupFilters.getChildren().addAll(ageGroupLabel,under25Button,twentiesButton,thirtiesButton,fortiesButton,above54Button);

        Label contextLabel = new Label("Context");
        CheckBox socialMediaButton = new CheckBox("Social media");
        CheckBox shoppingButton = new CheckBox("Shopping");
        CheckBox blogButton = new CheckBox("Blog");
        CheckBox newsButton = new CheckBox("News");
        CheckBox hobbiesButton = new CheckBox("Hobbies");
        CheckBox travelButton = new CheckBox("Travel");
        contextFilters.getChildren().addAll(contextLabel,socialMediaButton,shoppingButton,blogButton,newsButton,hobbiesButton,travelButton);



        filterHBox.getChildren().addAll(bounceFilter,genderFilters,incomeFilters,ageGroupFilters,contextFilters);

        GraphData graphData = new GraphData();
        Pair<ArrayList<String>, ArrayList<Integer>> pairData = graphData.getData("01", "01", "99", "99");
        ArrayList<String> dates = pairData.getKey();
        ArrayList<Integer> impressions = pairData.getValue();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false);

        XYChart.Series series = new XYChart.Series();
        series.setName("Impressions");
        for (int i = 0; i < dates.size(); i++) {
            series.getData().add(new XYChart.Data(dates.get(i), impressions.get(i)));
        }
        lineChart.getData().add(series);

        VBox chartVbox = new VBox();
        chartVbox.setPadding(new Insets(5, 0, 0, 0));


        HBox dateSelectionBar = new HBox();
        TextField startDate = new TextField();
        startDate.setPromptText(dates.getFirst());
        TextField endDate = new TextField();
        endDate.setPromptText(dates.getLast());
        Button submit = new Button("Submit");
        submit.setOnAction(e -> {
            if (dates.contains(startDate.getText()) && dates.contains(endDate.getText())) {
                GraphData newGraphData = new GraphData();
                Pair<ArrayList<String>, ArrayList<Integer>> newData = newGraphData.filterDate(startDate.getText(), endDate.getText());
                ArrayList<String> newDates = newData.getKey();
                ArrayList<Integer> newImpressions = newData.getValue();

                XYChart.Series newSeries = new XYChart.Series();
                newSeries.setName("Impressions");
                for (int i = 0; i < newDates.size(); i++) {
                    newSeries.getData().add(new XYChart.Data(newDates.get(i), newImpressions.get(i)));
                }

                lineChart.getData().clear();
                lineChart.getData().add(newSeries);
            }
        });
        dateSelectionBar.getChildren().addAll(startDate,endDate, submit);
        dateSelectionBar.setAlignment(Pos.CENTER);
        dateSelectionBar.setSpacing(10);

        chartVbox.getChildren().add(dateSelectionBar);
        chartVbox.getChildren().add(lineChart);


        leftSplitPane.setOrientation(Orientation.VERTICAL);
        leftSplitPane.getItems().add(chartVbox);
        leftSplitPane.getItems().add(filterHBox);
    }

    @Override
    public void cleanup() {

    }
}
