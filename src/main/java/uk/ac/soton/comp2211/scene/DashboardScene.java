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
import uk.ac.soton.comp2211.control.FileInputController;
import uk.ac.soton.comp2211.data.graph.GraphData;
import uk.ac.soton.comp2211.ui.MainWindow;

import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardScene extends BaseScene {

    GraphData graphData = new GraphData();

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

        HBox impressionMetric = new HBox();
        Label impressionsText = new Label(" Num of impressions: ");
        Label impressionsNum = new Label("");
        impressionsNum.textProperty().bind(graphData.impressionsNumProperty().asString());
        impressionMetric.getChildren().addAll(impressionsText, impressionsNum);

        HBox uniqueMetric = new HBox();
        Label uniquesText = new Label(" Num of uniques: ");
        Label uniquesNum = new Label("");
        uniquesNum.textProperty().bind(graphData.uniqueNumProperty().asString());
        uniqueMetric.getChildren().addAll(uniquesText, uniquesNum);

        HBox clicksMetric = new HBox();
        Label clicksText = new Label(" Num of clicks: ");
        Label clicksNum = new Label("");
        clicksNum.textProperty().bind(graphData.clicksNumProperty().asString());
        clicksMetric.getChildren().addAll(clicksText, clicksNum);

        HBox bounceMetric = new HBox();
        Label bouncesText = new Label(" Num of bounces: ");
        Label bounceNum = new Label("");
        bounceNum.textProperty().bind(graphData.bounceNumProperty().asString());
        bounceMetric.getChildren().addAll(bouncesText, bounceNum);

        HBox conversionMetric = new HBox();
        Label conversionsText = new Label(" Num of conversions: ");
        Label conversionsNum = new Label("");
        conversionsNum.textProperty().bind(graphData.conversionsNumProperty().asString());
        conversionMetric.getChildren().addAll(conversionsText, conversionsNum);

        HBox totalCostMetric = new HBox();
        Label totalCostText = new Label(" Total cost: £");
        Label totalCostNum = new Label("");
        totalCostNum.textProperty().bind(graphData.totalNumProperty().asString());
        totalCostMetric.getChildren().addAll(totalCostText, totalCostNum);

        HBox ctrMetric = new HBox();
        Label ctrText = new Label(" CTR: ");
        Label ctrNum = new Label("");
        ctrNum.textProperty().bind(graphData.ctrNumProperty().asString());
        ctrMetric.getChildren().addAll(ctrText, ctrNum);

        HBox cpaMetric = new HBox();
        Label cpaText = new Label(" CPA: £");
        Label cpaNum = new Label();
        cpaNum.textProperty().bind(graphData.cpaNumProperty().asString());
        cpaMetric.getChildren().addAll(cpaText, cpaNum);

        HBox cpcMetric = new HBox();
        Label cpcText = new Label(" CPC: £");
        Label cpcNum = new Label("");
        cpcNum.textProperty().bind(graphData.cpcNumProperty().asString());
        cpcMetric.getChildren().addAll(cpcText, cpcNum);

        HBox cpmMetric = new HBox();
        Label cpmText = new Label(" CPM: £");
        Label cpmNum = new Label("");
        cpmNum.textProperty().bind(graphData.cpmNumProperty().asString());
        cpmMetric.getChildren().addAll(cpmText, cpmNum);

        HBox bounceRateMetric = new HBox();
        Label bounceRateText = new Label(" Bounce rate: ");
        Label bounceRateNum = new Label("");
        bounceRateNum.textProperty().bind(graphData.bounceRateNumProperty().asString());
        bounceRateMetric.getChildren().addAll(bounceRateText, bounceRateNum);

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
                impressionMetric, separator1, uniqueMetric, separator2, clicksMetric, separator3,
                bounceMetric, separator4, conversionMetric, separator5, totalCostMetric, separator6,
                ctrMetric, separator7, cpaMetric, separator8, cpcMetric, separator9,
                cpmMetric, separator10, bounceRateMetric
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
        ToggleGroup group = new ToggleGroup();
        RadioButton timeBounceButton = new RadioButton("Time");
        timeBounceButton.setToggleGroup(group);
        timeBounceButton.setSelected(true);
        RadioButton singlePageBounceButton = new RadioButton("Pages");
        singlePageBounceButton.setToggleGroup(group);
        bounceFilter.getChildren().addAll(bounceLabel,timeBounceButton,singlePageBounceButton);

        Label genderLabel = new Label("Gender");
        CheckBox maleButton = new CheckBox("Male");
        maleButton.setSelected(true);
        CheckBox femaleButton = new CheckBox("Female");
        femaleButton.setSelected(true);
        genderFilters.getChildren().addAll(genderLabel,maleButton,femaleButton);

        Label incomeLabel = new Label("Income");
        CheckBox lowButton = new CheckBox("Low");
        lowButton.setSelected(true);
        CheckBox mediumButton = new CheckBox("Medium");
        mediumButton.setSelected(true);
        CheckBox highButton = new CheckBox("High");
        highButton.setSelected(true);
        incomeFilters.getChildren().addAll(incomeLabel,lowButton,mediumButton,highButton);

        Label ageGroupLabel = new Label("Age group");
        CheckBox under25Button = new CheckBox("Under 25");
        under25Button.setSelected(true);
        CheckBox twentiesButton = new CheckBox("25-34");
        twentiesButton.setSelected(true);
        CheckBox thirtiesButton = new CheckBox("35-44");
        thirtiesButton.setSelected(true);
        CheckBox fortiesButton = new CheckBox("45-54");
        fortiesButton.setSelected(true);
        CheckBox above54Button = new CheckBox("Above 54");
        above54Button.setSelected(true);
        ageGroupFilters.getChildren().addAll(ageGroupLabel,under25Button,twentiesButton,thirtiesButton,fortiesButton,above54Button);

        Label contextLabel = new Label("Context");
        CheckBox socialMediaButton = new CheckBox("Social media");
        socialMediaButton.setSelected(true);
        CheckBox shoppingButton = new CheckBox("Shopping");
        shoppingButton.setSelected(true);
        CheckBox blogButton = new CheckBox("Blog");
        blogButton.setSelected(true);
        CheckBox newsButton = new CheckBox("News");
        newsButton.setSelected(true);
        CheckBox hobbiesButton = new CheckBox("Hobbies");
        hobbiesButton.setSelected(true);
        CheckBox travelButton = new CheckBox("Travel");
        travelButton.setSelected(true);
        contextFilters.getChildren().addAll(contextLabel,socialMediaButton,shoppingButton,blogButton,newsButton,hobbiesButton,travelButton);

        filterHBox.getChildren().addAll(bounceFilter,genderFilters,incomeFilters,ageGroupFilters,contextFilters);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        ArrayList<String> dates = graphData.getDates("2015-01-01", "2015-01-14");
        try {
            graphData.calculateMetrics(lineChart, "2015-01-01", "2015-01-14");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        VBox chartVbox = new VBox();
        chartVbox.setPadding(new Insets(5, 0, 0, 0));

        HBox dateSelectionBar = new HBox();
        TextField startDate = new TextField(dates.getFirst());
        startDate.setPromptText(dates.getFirst());
        TextField endDate = new TextField(dates.getLast());
        endDate.setPromptText(dates.getLast());
        Button submit = new Button("Submit");
        submit.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        dateSelectionBar.getChildren().addAll(startDate, endDate, submit);
        dateSelectionBar.setAlignment(Pos.CENTER);
        dateSelectionBar.setSpacing(10);

        maleButton.selectedProperty().bindBidirectional(graphData.maleProperty());
        maleButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        femaleButton.selectedProperty().bindBidirectional(graphData.femaleProperty());
        femaleButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));

        lowButton.selectedProperty().bindBidirectional(graphData.lowProperty());
        lowButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        mediumButton.selectedProperty().bindBidirectional(graphData.mediumProperty());
        mediumButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        highButton.selectedProperty().bindBidirectional(graphData.highProperty());
        highButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));

        under25Button.selectedProperty().bindBidirectional(graphData.under25Property());
        under25Button.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        twentiesButton.selectedProperty().bindBidirectional(graphData.twentiesProperty());
        twentiesButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        thirtiesButton.selectedProperty().bindBidirectional(graphData.thirtiesProperty());
        thirtiesButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        fortiesButton.selectedProperty().bindBidirectional(graphData.fortiesProperty());
        fortiesButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        above54Button.selectedProperty().bindBidirectional(graphData.above54Property());
        above54Button.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));

        socialMediaButton.selectedProperty().bindBidirectional(graphData.socialMediaProperty());
        socialMediaButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        shoppingButton.selectedProperty().bindBidirectional(graphData.shoppingProperty());
        shoppingButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        newsButton.selectedProperty().bindBidirectional(graphData.newsProperty());
        newsButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        blogButton.selectedProperty().bindBidirectional(graphData.blogProperty());
        blogButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        travelButton.selectedProperty().bindBidirectional(graphData.travelProperty());
        travelButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        hobbiesButton.selectedProperty().bindBidirectional(graphData.hobbiesProperty());
        hobbiesButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));

        timeBounceButton.selectedProperty().bindBidirectional(graphData.timeProperty());
        timeBounceButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        singlePageBounceButton.selectedProperty().bindBidirectional(graphData.pageProperty());
        singlePageBounceButton.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));

        HBox choiceBoxContainer = new HBox();
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Impressions", "Uniques", "Clicks", "Bounces", "Conversions", "Total cost", "CTR", "CPA", "CPC", "CPM", "Bounce rate");
        choiceBox.getSelectionModel().select(0);
        choiceBox.setOnAction(e -> {
            graphData.graphNumProperty().set(choiceBox.getValue());
            lineChart.getData().clear();
            graphData.changeChart(lineChart, dates);
        });
        choiceBoxContainer.getChildren().add(choiceBox);
        choiceBoxContainer.setAlignment(Pos.CENTER);

        chartVbox.getChildren().add(dateSelectionBar);
        chartVbox.getChildren().add(lineChart);
        chartVbox.getChildren().add(choiceBoxContainer);

        leftSplitPane.setOrientation(Orientation.VERTICAL);
        leftSplitPane.getItems().add(chartVbox);
        leftSplitPane.getItems().add(filterHBox);
    }

    @Override
    public void cleanup() {

    }

    public void checkGraph(LineChart lineChart, ArrayList<String> dates, String startDate, String endDate) {
        if (dates.contains(startDate) && dates.contains(endDate)) {
            lineChart.getData().clear();
            try {
                graphData.calculateMetrics(lineChart, startDate, endDate);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
