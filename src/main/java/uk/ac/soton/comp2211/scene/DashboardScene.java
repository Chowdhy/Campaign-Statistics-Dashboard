package uk.ac.soton.comp2211.scene;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.control.DashboardController;
import uk.ac.soton.comp2211.ui.MainWindow;
import uk.ac.soton.comp2211.users.Permissions;

import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;

public class DashboardScene extends BaseScene {
    DashboardController controller;

    LineChart<String, Number> lineChart;
    LineChart<String, Number> lineChart2;
    ArrayList<String> dates;
    TextField startDate;
    TextField endDate;
    Button submit;
    Button filter;
    Tooltip tooltip1;


    ProgressIndicator progressIndicator;

    public DashboardScene(MainWindow window) {
        super(window);
    }

    @Override
    public void initialise() {
        controller.setMaxValues();
        dates = controller.getDates("2015-01-01", controller.maxDate());

        controller.calculateMetrics("2015-01-01", controller.maxDate());
        controller.changeChart(lineChart, controller.graphNumProperty().get());

        startDate.setText(dates.getFirst());
        startDate.setPromptText(dates.getFirst());

        endDate.setText(dates.getLast());
        endDate.setPromptText(dates.getLast());

        submit.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        filter.setOnAction(e -> {
            progressIndicator.setVisible(true);

            checkGraph(lineChart, dates, startDate.getText(), endDate.getText());

            tooltip1.setText("Page range: 0-" + controller.maxPage() + "\nTime range: 0-" + controller.maxTime() + "\nMaximum value from inputted data" + "\nUsed to change bounce data");


        });
    }

    @Override
    public void build() {
        controller = new DashboardController();

        root = new StackPane();

        var mainVBox = new VBox();
        VBox.setVgrow(mainVBox,Priority.ALWAYS);
        root.getChildren().add(mainVBox);


        var optionsMenu = new Menu("Options");
        var uploadMenuItem = new MenuItem("Upload files");
        var userMenuItem = new MenuItem("User management");
        var themeMenuItem = new MenuItem("Switch theme");
        var logoutMenuItem = new MenuItem("Logout");
        optionsMenu.getItems().addAll(uploadMenuItem,userMenuItem, themeMenuItem,logoutMenuItem);


        var exportMenu = new Menu("Export");
        var logsMenuItem = new MenuItem("Logs");
        var graphMenuItem = new MenuItem("Graph");
        var reportMenuItem = new MenuItem("Report");
        exportMenu.getItems().addAll(graphMenuItem,reportMenuItem,logsMenuItem);



        MenuBar menuBar = new MenuBar(optionsMenu,exportMenu);
        if(App.getUser().getPermissions().equals(Permissions.EDITOR)){
            logsMenuItem.setDisable(true);
        }else if(App.getUser().getPermissions().equals(Permissions.VIEWER)){
            exportMenu.setDisable(true);
        }

        Circle infoIcon1 = new Circle(6, Color.BLUE);
        infoIcon1.setStroke(Color.BLACK);

        tooltip1 = new Tooltip();

        Text iText = new Text("i");
        iText.setFont(Font.font(6));
        iText.setFill(Color.WHITE);


        StackPane iconWithText = new StackPane();
        iconWithText.getChildren().addAll(infoIcon1, iText);
        Tooltip.install(iconWithText, tooltip1);

        mainVBox.getChildren().add(menuBar);



        logoutMenuItem.setOnAction(e -> {
            App.setUser(null);
            window.loadLoginScene();
        });

        uploadMenuItem.setOnAction(e -> {
            window.loadFileInput();
        });

        userMenuItem.setOnAction(e -> {
            window.loadUserManagementScene();
        });



        SplitPane splitPane = new SplitPane();
        mainVBox.getChildren().add(splitPane);
        VBox.setVgrow(splitPane,Priority.ALWAYS);
        SplitPane leftSplitPane = new SplitPane();

        splitPane.setDividerPosition(0, 0.7);
        leftSplitPane.setDividerPosition(0,0.68);


        VBox metricsVBox = new VBox();
        VBox.setVgrow(metricsVBox, Priority.ALWAYS);

        metricsVBox.setSpacing(1);

        VBox totalCostMetric = new VBox();
        VBox.setVgrow(totalCostMetric,Priority.ALWAYS);
        totalCostMetric.setMaxHeight(200);
        totalCostMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label totalCostText = new Label(" Total cost (£)");
        Label totalCostNum = new Label("");
        totalCostNum.textProperty().bind(controller.totalNumProperty().asString());
        totalCostMetric.getChildren().addAll(totalCostText, totalCostNum);
        totalCostMetric.setAlignment(Pos.CENTER);
        metricsVBox.getChildren().add(totalCostMetric);

        var splitHBox = new HBox();
        VBox.setVgrow(splitHBox,Priority.ALWAYS);

        var leftMetrics = new VBox();
        VBox.setVgrow(leftMetrics,Priority.ALWAYS);

        var rightMetrics = new VBox();
        VBox.setVgrow(rightMetrics,Priority.ALWAYS);
        splitHBox.getChildren().addAll(leftMetrics,rightMetrics);
        metricsVBox.getChildren().addAll(splitHBox);
        splitHBox.setSpacing(1);
        leftMetrics.setSpacing(1);
        rightMetrics.setSpacing(1);
        HBox.setHgrow(rightMetrics, Priority.ALWAYS);
        HBox.setHgrow(leftMetrics,Priority.ALWAYS);



        VBox impressionMetric = new VBox();
        VBox.setVgrow(impressionMetric,Priority.ALWAYS);
        impressionMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label impressionsText = new Label(" Num of impressions");
        Label impressionsNum = new Label("");
        impressionsNum.textProperty().bind(controller.impressionsNumProperty().asString());
        impressionMetric.getChildren().addAll(impressionsText, impressionsNum);
        impressionMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(impressionMetric);

        VBox uniqueMetric = new VBox();
        VBox.setVgrow(uniqueMetric,Priority.ALWAYS);
        uniqueMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label uniquesText = new Label("Num of uniques");
        Label uniquesNum = new Label("");
        uniquesNum.textProperty().bind(controller.uniqueNumProperty().asString());
        uniqueMetric.getChildren().addAll(uniquesText, uniquesNum);
        uniqueMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(uniqueMetric);

        VBox clicksMetric = new VBox();
        VBox.setVgrow(clicksMetric,Priority.ALWAYS);
        clicksMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label clicksText = new Label("Num of clicks");
        Label clicksNum = new Label("");
        clicksNum.textProperty().bind(controller.clicksNumProperty().asString());
        clicksMetric.getChildren().addAll(clicksText, clicksNum);
        clicksMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(clicksMetric);

        VBox bounceMetric = new VBox();
        VBox.setVgrow(bounceMetric,Priority.ALWAYS);
        bounceMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label bouncesText = new Label("Num of bounces");
        Label bounceNum = new Label("");
        bounceNum.textProperty().bind(controller.bounceNumProperty().asString());
        bounceMetric.getChildren().addAll(bouncesText, bounceNum);
        bounceMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(bounceMetric);

        VBox conversionMetric = new VBox();
        VBox.setVgrow(conversionMetric,Priority.ALWAYS);
        conversionMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label conversionsText = new Label("Num of conversions");
        Label conversionsNum = new Label("");
        conversionsNum.textProperty().bind(controller.conversionsNumProperty().asString());
        conversionMetric.getChildren().addAll(conversionsText, conversionsNum);
        conversionMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(conversionMetric);

        VBox ctrMetric = new VBox();
        VBox.setVgrow(ctrMetric,Priority.ALWAYS);
        ctrMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label ctrText = new Label("CTR");
        Label ctrNum = new Label("");
        ctrNum.textProperty().bind(controller.ctrNumProperty().asString());
        ctrMetric.getChildren().addAll(ctrText, ctrNum);
        ctrMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(ctrMetric);

        VBox cpaMetric = new VBox();
        VBox.setVgrow(cpaMetric,Priority.ALWAYS);
        cpaMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label cpaText = new Label("CPA");
        Label cpaNum = new Label();
        cpaNum.textProperty().bind(controller.cpaNumProperty().asString());
        cpaMetric.getChildren().addAll(cpaText, cpaNum);
        cpaMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(cpaMetric);

        VBox cpcMetric = new VBox();
        VBox.setVgrow(cpcMetric,Priority.ALWAYS);
        cpcMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label cpcText = new Label("CPC");
        Label cpcNum = new Label("");
        cpcNum.textProperty().bind(controller.cpcNumProperty().asString());
        cpcMetric.getChildren().addAll(cpcText, cpcNum);
        cpcMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(cpcMetric);

        VBox cpmMetric = new VBox();
        VBox.setVgrow(cpmMetric,Priority.ALWAYS);
        cpmMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label cpmText = new Label(" CPM");
        Label cpmNum = new Label("");
        cpmNum.textProperty().bind(controller.cpmNumProperty().asString());
        cpmMetric.getChildren().addAll(cpmText, cpmNum);
        cpmMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(cpmMetric);

        VBox bounceRateMetric = new VBox();
        VBox.setVgrow(bounceRateMetric,Priority.ALWAYS);
        bounceRateMetric.setStyle("-fx-background-color: LIGHTGRAY;");
        Label bounceRateText = new Label(" Bounce rate");
        Label bounceRateNum = new Label("");
        bounceRateNum.textProperty().bind(controller.bounceRateNumProperty().asString());
        bounceRateMetric.getChildren().addAll(bounceRateText, bounceRateNum);
        bounceRateMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(bounceRateMetric);




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
        TextField defineBounce = new TextField();
        defineBounce.setPrefWidth(35);
        defineBounce.setPromptText("0-" + controller.maxTime());
        defineBounce.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        defineBounce.textProperty().bindBidirectional(controller.timeValProperty());
        var inputAndTips = new HBox();
        inputAndTips.getChildren().addAll(defineBounce,iconWithText);
        inputAndTips.setSpacing(2);
        bounceFilter.getChildren().addAll(bounceLabel,timeBounceButton,singlePageBounceButton, inputAndTips);
        bounceFilter.setSpacing(5);

        Label genderLabel = new Label("Gender");
        CheckBox maleButton = new CheckBox("Male");
        maleButton.setSelected(true);
        CheckBox femaleButton = new CheckBox("Female");
        femaleButton.setSelected(true);
        genderFilters.getChildren().addAll(genderLabel,maleButton,femaleButton);
        genderFilters.setSpacing(5);

        Label incomeLabel = new Label("Income");
        CheckBox lowButton = new CheckBox("Low");
        lowButton.setSelected(true);
        CheckBox mediumButton = new CheckBox("Medium");
        mediumButton.setSelected(true);
        CheckBox highButton = new CheckBox("High");
        highButton.setSelected(true);
        incomeFilters.getChildren().addAll(incomeLabel,lowButton,mediumButton,highButton);
        incomeFilters.setSpacing(5);

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
        ageGroupFilters.setSpacing(5);

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
        contextFilters.setSpacing(5);

        filterHBox.getChildren().addAll(bounceFilter,genderFilters,incomeFilters,ageGroupFilters,contextFilters);
        filterHBox.setSpacing(25);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        VBox chartVbox = new VBox();
        chartVbox.setPadding(new Insets(5, 0, 0, 0));

        HBox dateSelectionBar = new HBox();
        startDate = new TextField();
        startDate.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        endDate = new TextField();endDate.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        submit = new Button("Submit");

        dateSelectionBar.getChildren().addAll(startDate, endDate, submit);
        dateSelectionBar.setAlignment(Pos.CENTER);
        dateSelectionBar.setSpacing(10);

        maleButton.selectedProperty().bindBidirectional(controller.maleProperty());
        femaleButton.selectedProperty().bindBidirectional(controller.femaleProperty());

        lowButton.selectedProperty().bindBidirectional(controller.lowProperty());
        mediumButton.selectedProperty().bindBidirectional(controller.mediumProperty());
        highButton.selectedProperty().bindBidirectional(controller.highProperty());

        under25Button.selectedProperty().bindBidirectional(controller.under25Property());
        twentiesButton.selectedProperty().bindBidirectional(controller.twentiesProperty());
        thirtiesButton.selectedProperty().bindBidirectional(controller.thirtiesProperty());
        fortiesButton.selectedProperty().bindBidirectional(controller.fortiesProperty());
        above54Button.selectedProperty().bindBidirectional(controller.above54Property());

        socialMediaButton.selectedProperty().bindBidirectional(controller.socialMediaProperty());
        shoppingButton.selectedProperty().bindBidirectional(controller.shoppingProperty());
        newsButton.selectedProperty().bindBidirectional(controller.newsProperty());
        blogButton.selectedProperty().bindBidirectional(controller.blogProperty());
        travelButton.selectedProperty().bindBidirectional(controller.travelProperty());
        hobbiesButton.selectedProperty().bindBidirectional(controller.hobbiesProperty());

        timeBounceButton.selectedProperty().bindBidirectional(controller.timeProperty());
        timeBounceButton.setOnAction(e -> changeBounce(defineBounce));
        singlePageBounceButton.selectedProperty().bindBidirectional(controller.pageProperty());
        singlePageBounceButton.setOnAction(e -> changeBounce(defineBounce));

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Impressions", "Uniques", "Clicks", "Bounces", "Conversions", "Total cost", "CTR", "CPA", "CPC", "CPM", "Bounce rate","Cost Distribution Histogram");
        choiceBox.getSelectionModel().select(0);
        choiceBox.setOnAction(e2 -> {
            String selectedValue = choiceBox.getValue();
            if (selectedValue.equals("Cost Distribution Histogram")) {
                window.loadHistogramScene();
            } else {
                controller.graphNumProperty().set(selectedValue);
                controller.changeChart(lineChart, selectedValue);
            }
        });

        ToggleGroup timeToggleGroup = new ToggleGroup();
        HBox graphTime = new HBox();

        ToggleButton hour = new ToggleButton("Hour");
        hour.setToggleGroup(timeToggleGroup);
        hour.setOnAction(e -> {
            controller.buttonValProperty().set("hour");
            controller.changeChart(lineChart, controller.graphNumProperty().get());
            if (controller.compareProperty().get()) {
                controller.changeChart(lineChart2, controller.graph2NumProperty().get());
            }
        });

        ToggleButton day = new ToggleButton("Day");
        day.setSelected(true);
        day.setToggleGroup(timeToggleGroup);
        day.setOnAction(e -> {
            controller.buttonValProperty().set("day");
            controller.changeChart(lineChart, controller.graphNumProperty().get());
            if (controller.compareProperty().get()) {
                controller.changeChart(lineChart2, controller.graph2NumProperty().get());
            }
        });

        ToggleButton week = new ToggleButton("Week");
        week.setToggleGroup(timeToggleGroup);
        week.setOnAction(e -> {
            controller.buttonValProperty().set("week");
            controller.changeChart(lineChart, controller.graphNumProperty().get());
            if (controller.compareProperty().get()) {
                controller.changeChart(lineChart2, controller.graph2NumProperty().get());
            }
        });

        graphTime.getChildren().addAll(hour, day, week);

        HBox graphOptions = new HBox();

        CategoryAxis xAxis2 = new CategoryAxis();
        NumberAxis yAxis2 = new NumberAxis();
        lineChart2 = new LineChart<>(xAxis2,yAxis2);
        lineChart2.setAnimated(false);
        lineChart2.setLegendVisible(false);

        ChoiceBox<String> choiceBox2 = new ChoiceBox<>();
        choiceBox2.getItems().addAll("Cost Distribution Histogram","Impressions", "Uniques", "Clicks", "Bounces", "Conversions", "Total cost", "CTR", "CPA", "CPC", "CPM", "Bounce rate","Cost Distribution Histogram");
        choiceBox2.getSelectionModel().select(0);
        choiceBox2.setOnAction(e2 -> {
            String selectedValue = choiceBox2.getValue();
            if (selectedValue.equals("Cost Distribution Histogram")) {
                window.loadHistogramScene();
            } else {
                controller.graph2NumProperty().set(selectedValue);
                controller.changeChart(lineChart2, selectedValue);
            }
        });

        Button compare = new Button("Compare");
        compare.setOnAction(e -> {
            controller.compareProperty().set(!controller.compareProperty().get());

            if (controller.compareProperty().get()) {
                controller.changeChart(lineChart2, controller.graph2NumProperty().get());
                chartVbox.getChildren().add(1, lineChart2);
                graphOptions.getChildren().add(choiceBox2);
            } else {
                chartVbox.getChildren().remove(lineChart2);
                graphOptions.getChildren().remove(choiceBox2);
            }
        });



        graphOptions.getChildren().addAll(compare, graphTime, choiceBox);
        graphOptions.setAlignment(Pos.CENTER);
        graphOptions.setSpacing(10);

        chartVbox.getChildren().add(dateSelectionBar);
        chartVbox.getChildren().add(lineChart);
        chartVbox.getChildren().add(graphOptions);

        filter = new Button("Filter");

        var filterButtonHBox = new HBox();
        filterButtonHBox.setSpacing(5);

        progressIndicator = new ProgressIndicator();
        progressIndicator.setMaxSize(20,20);
        progressIndicator.isIndeterminate();
        progressIndicator.setVisible(false);

        filterButtonHBox.getChildren().addAll(filter,progressIndicator);
        filterButtonHBox.setAlignment(Pos.CENTER);


        VBox bottom = new VBox();
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().add(filterHBox);
        bottom.getChildren().add(filterButtonHBox);
        bottom.setAlignment(Pos.CENTER);
        filterHBox.setAlignment(Pos.CENTER);

        VBox.setVgrow(chartVbox,Priority.ALWAYS);


        leftSplitPane.setOrientation(Orientation.VERTICAL);
        leftSplitPane.getItems().add(chartVbox);
        leftSplitPane.getItems().add(bottom);

    }

    @Override
    public void cleanup() {

    }

    public void checkGraph(LineChart lineChart, ArrayList<String> dates, String startDate, String endDate) {
        try {
            if (dates.contains(startDate) && dates.contains(endDate) && controller.maxTime() >= Integer.parseInt(controller.timeValProperty().get()) && controller.maxPage() >= Integer.parseInt(controller.pageValProperty().get())) {
                Platform.runLater(() ->{
                    controller.calculateMetrics(startDate, endDate);
                    controller.changeChart(lineChart, controller.graphNumProperty().get());
                    if (controller.compareProperty().get()) {
                        controller.changeChart(lineChart2, controller.graph2NumProperty().get());
                    }

                    //progressIndicator.setVisible(false);
                });
            }
        } catch(Exception ignored) {

        }
    }

    public void changeBounce(TextField defineBounce){
        if (controller.timeProperty().get()) {
            defineBounce.setPromptText("0-" + controller.maxTime());
            defineBounce.textProperty().unbindBidirectional(controller.pageValProperty());
            defineBounce.textProperty().bindBidirectional(controller.timeValProperty());
        } else {
            defineBounce.setPromptText("0-" + controller.maxPage());
            defineBounce.textProperty().unbindBidirectional(controller.timeValProperty());
            defineBounce.textProperty().bindBidirectional(controller.pageValProperty());
        }
    }
}
