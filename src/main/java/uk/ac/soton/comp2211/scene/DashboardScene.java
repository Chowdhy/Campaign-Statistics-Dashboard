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
        Label totalCostText = new Label(" Total cost: ");
        Label totalCostNum = new Label("");
        totalCostNum.textProperty().bind(graphData.totalNumProperty().asString());
        totalCostMetric.getChildren().addAll(totalCostText, totalCostNum);

        HBox ctrMetric = new HBox();
        Label ctrText = new Label(" CTR: ");
        Label ctrNum = new Label("");
        ctrNum.textProperty().bind(graphData.ctrNumProperty().asString());
        ctrMetric.getChildren().addAll(ctrText, ctrNum);

        HBox cpaMetric = new HBox();
        Label cpaText = new Label(" CPA: ");
        Label cpaNum = new Label();
        cpaNum.textProperty().bind(graphData.cpaNumProperty().asString());
        cpaMetric.getChildren().addAll(cpaText, cpaNum);

        HBox cpcMetric = new HBox();
        Label cpcText = new Label(" CPC: ");
        Label cpcNum = new Label("");
        cpcNum.textProperty().bind(graphData.cpcNumProperty().asString());
        cpcMetric.getChildren().addAll(cpcText, cpcNum);

        HBox cpmMetric = new HBox();
        Label cpmText = new Label(" CPM: ");
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
        ArrayList<String> dates = makeGraph(lineChart, "2015-01-01", "2015-02-28");

        VBox chartVbox = new VBox();
        chartVbox.setPadding(new Insets(5, 0, 0, 0));


        HBox dateSelectionBar = new HBox();
        TextField startDate = new TextField(dates.getFirst());
        startDate.setPromptText(dates.getFirst());
        TextField endDate = new TextField(dates.getLast());
        endDate.setPromptText(dates.getLast());
        Button submit = new Button("Submit");
        submit.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        dateSelectionBar.getChildren().addAll(startDate,endDate, submit);
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
            checkGraph(lineChart, dates, startDate.getText(), endDate.getText());
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

    public void checkGraph(LineChart lineChart, ArrayList<String> dates, String startDate, String endDate){
        if (dates.contains(startDate) && dates.contains(endDate)) {
            lineChart.getData().clear();
            makeGraph(lineChart, startDate, endDate);
        }
    }

    public ArrayList<String> makeGraph(LineChart lineChart, String startDate, String endDate) {
        graphData.impressionsNumProperty().set(0);
        String impressionSQL = "SELECT impression_log.id, impression_log.date FROM impression_log";
        Pair<ArrayList<String>, ArrayList<Integer>> impressionData = graphData.filterDate(startDate, endDate, impressionSQL, new String[]{}, "");
        ArrayList<String> dates = impressionData.getKey();
        ArrayList<Integer> impressions = impressionData.getValue();
        graphData.impressionsNumProperty().set(impressions.stream().mapToInt(a -> a).sum());

        graphData.uniqueNumProperty().set(0);
        String uniqueSQL = "SELECT impression_log.id, impression_log.date FROM impression_log INNER JOIN click_log ON impression_log.id = click_log.id";
        Pair<ArrayList<String>, ArrayList<Integer>> uniqueData = graphData.filterDate(startDate, endDate, uniqueSQL, new String[]{}, "GROUP BY impression_log.id ORDER BY impression_log.date");
        ArrayList<Integer> unique = uniqueData.getValue();
        graphData.uniqueNumProperty().set(unique.stream().mapToInt(a -> a).sum());

        graphData.clicksNumProperty().set(0);
        String clickSQL = "SELECT impression_log.id, impression_log.date FROM impression_log INNER JOIN click_log ON impression_log.id = click_log.id";
        Pair<ArrayList<String>, ArrayList<Integer>> clickData = graphData.filterDate(startDate, endDate, clickSQL, new String[]{}, "");
        ArrayList<Integer> clicks = clickData.getValue();
        graphData.clicksNumProperty().set(clicks.stream().mapToInt(a -> a).sum());

        graphData.conversionsNumProperty().set(0);
        String conversionSQL = "SELECT impression_log.id, impression_log.date FROM server_log INNER JOIN impression_log ON impression_log.id = server_log.id";
        Pair<ArrayList<String>, ArrayList<Integer>> conversionData = graphData.filterDate(startDate, endDate, conversionSQL, new String[]{"server_log.conversion = 'Yes'"}, "ORDER BY impression_log.date");
        ArrayList<Integer> conversions = conversionData.getValue();
        graphData.conversionsNumProperty().set(conversions.stream().mapToInt(a -> a).sum());

        graphData.ctrNumProperty().set(0);
        ArrayList<Double> ctr = new ArrayList<>();
        for (int i = 0; i < impressions.size(); i++) {
            ctr.add(Double.parseDouble(String.format("%.2g%n", (double) clicks.get(i) / impressions.get(i))));
        }
        graphData.ctrNumProperty().set(Double.parseDouble(String.format("%.5g%n", ctr.stream().mapToDouble(a -> a).sum() / dates.size())));

        graphData.cpcNumProperty().set(0);
        String cpcSQL = "SELECT impression_log.id, impression_log.date, click_log.click_cost FROM impression_log INNER JOIN click_log ON impression_log.id = click_log.id";
        ArrayList<Double> cpcTest = graphData.costFilterDate(startDate, endDate, cpcSQL, new String[]{}, "");
        ArrayList<Double> cpc = new ArrayList<>();
        for (int i = 0; i < cpcTest.size(); i++) {
            cpc.add(Double.parseDouble(String.format("%.5g%n", cpcTest.get(i) / clicks.get(i))));
        }
        graphData.cpcNumProperty().set(Double.parseDouble(String.format("%.5g%n", cpc.stream().mapToDouble(a -> a).sum() / dates.size())));

        graphData.cpmNumProperty().set(0);
        String cpmSQL = "SELECT impression_log.id, impression_log.date, impression_log.impression_cost FROM impression_log";
        ArrayList<Double> cpmTest = graphData.costFilterDate(startDate, endDate, cpmSQL, new String[]{}, "");
        ArrayList<Double> cpm = new ArrayList<>();
        for (int i = 0; i < cpmTest.size(); i++) {
            cpm.add(Double.parseDouble(String.format("%.5g%n", (cpmTest.get(i) / impressions.get(i))*1000)));
        }
        graphData.cpmNumProperty().set(Double.parseDouble(String.format("%.5g%n", cpm.stream().mapToDouble(a -> a).sum() / dates.size())));

        graphData.totalNumProperty().set(0);
        ArrayList<Double> total = cpcTest;
        graphData.totalNumProperty().set(Double.parseDouble(String.format("%.5g%n", total.stream().mapToDouble(a -> a).sum())));

        graphData.cpaNumProperty().set(0);
        ArrayList<Double> cpa = new ArrayList<>();
        for (int i = 0; i < total.size(); i++) {
            cpa.add(Double.parseDouble(String.format("%.5g%n", total.get(i) / conversions.get(i))));
        }
        graphData.cpaNumProperty().set(Double.parseDouble(String.format("%.5g%n", cpa.stream().mapToDouble(a -> a).sum() / dates.size())));

        graphData.bounceNumProperty().set(0);
        String timeSQL = "SELECT impression_log.id, impression_log.date FROM server_log INNER JOIN impression_log ON impression_log.id = server_log.id";
        Pair<ArrayList<String>, ArrayList<Integer>> timeData = graphData.filterDate(startDate, endDate, timeSQL, new String[]{"time(server_log.exit_date) > time(server_log.entry_date, '+1 minutes')"}, "ORDER BY impression_log.date");
        ArrayList<Integer> time = timeData.getValue();
        String pageSQL = "SELECT impression_log.id, impression_log.date FROM server_log INNER JOIN impression_log ON impression_log.id = server_log.id";
        Pair<ArrayList<String>, ArrayList<Integer>> pageData = graphData.filterDate(startDate, endDate, pageSQL, new String[]{"server_log.pages_viewed > 1"}, "ORDER BY impression_log.date");
        ArrayList<Integer> page = pageData.getValue();
        if (graphData.timeProperty().getValue()) {
            graphData.bounceNumProperty().set(time.stream().mapToInt(a -> a).sum());
        } else {
            graphData.bounceNumProperty().set(page.stream().mapToInt(a -> a).sum());
        }

        graphData.bounceRateNumProperty().set(0);
        ArrayList<Double> timeRate = new ArrayList<>();
        for (int i = 0; i < clicks.size(); i++) {
            timeRate.add(Double.parseDouble(String.format("%.5g%n", (double) time.get(i) / clicks.get(i))));
        }
        ArrayList<Double> pageRate = new ArrayList<>();
        for (int i = 0; i < clicks.size(); i++) {
            pageRate.add(Double.parseDouble(String.format("%.5g%n", (double) page.get(i) / clicks.get(i))));
        }
        if (graphData.timeProperty().getValue()) {
            graphData.bounceRateNumProperty().set(Double.parseDouble(String.format("%.5g%n", timeRate.stream().mapToDouble(a -> a).sum() / dates.size())));
        } else {
            graphData.bounceRateNumProperty().set(Double.parseDouble(String.format("%.5g%n", pageRate.stream().mapToDouble(a -> a).sum() / dates.size())));
        }

        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < dates.size(); i++) {
            if (graphData.graphNumProperty().get().equals("Impressions")) {
                series.getData().add(new XYChart.Data(dates.get(i), impressions.get(i)));
            } else if (graphData.graphNumProperty().get().equals("Uniques")) {
                series.getData().add(new XYChart.Data(dates.get(i), unique.get(i)));
            } else if (graphData.graphNumProperty().get().equals("Clicks")) {
                series.getData().add(new XYChart.Data(dates.get(i), clicks.get(i)));
            } else if (graphData.graphNumProperty().get().equals("Bounces") && graphData.timeProperty().getValue()) {
                series.getData().add(new XYChart.Data(dates.get(i), time.get(i)));
            } else if (graphData.graphNumProperty().get().equals("Bounces")) {
                series.getData().add(new XYChart.Data(dates.get(i), page.get(i)));
            } else if (graphData.graphNumProperty().get().equals("Conversions")) {
                series.getData().add(new XYChart.Data(dates.get(i), conversions.get(i)));
            } else if (graphData.graphNumProperty().get().equals("Total cost")) {
                series.getData().add(new XYChart.Data(dates.get(i), total.get(i)));
            } else if (graphData.graphNumProperty().get().equals("CTR")) {
                series.getData().add(new XYChart.Data(dates.get(i), ctr.get(i)));
            } else if (graphData.graphNumProperty().get().equals("CPA")) {
                series.getData().add(new XYChart.Data(dates.get(i), cpa.get(i)));
            } else if (graphData.graphNumProperty().get().equals("CPC")) {
                series.getData().add(new XYChart.Data(dates.get(i), cpc.get(i)));
            } else if (graphData.graphNumProperty().get().equals("CPM")) {
                series.getData().add(new XYChart.Data(dates.get(i), cpm.get(i)));
            } else if (graphData.graphNumProperty().get().equals("Bounce rate") && graphData.timeProperty().getValue()) {
                series.getData().add(new XYChart.Data(dates.get(i), timeRate.get(i)));
            } else if (graphData.graphNumProperty().get().equals("Bounce rate")) {
                series.getData().add(new XYChart.Data(dates.get(i), pageRate.get(i)));
            }
        }
        lineChart.getData().add(series);

        return dates;
    }
}
