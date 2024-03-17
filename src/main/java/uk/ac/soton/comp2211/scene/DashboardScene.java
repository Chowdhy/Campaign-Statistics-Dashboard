package uk.ac.soton.comp2211.scene;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp2211.control.DashboardController;
import uk.ac.soton.comp2211.ui.MainWindow;

import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardScene extends BaseScene {

    DashboardController controller;
    TextField startDate;
    TextField endDate;
    LineChart<String, Number> lineChart;
    Button submit;
    Button filter;

    public DashboardScene(MainWindow window) {
        super(window);
    }

    @Override
    public void initialise() {
        controller.setMaxValues();
        ArrayList<String> dates = controller.getDates("2015-01-01", controller.maxDate());
        try {
            controller.calculateMetrics(lineChart, "2015-01-01", controller.maxDate());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        startDate.setText(dates.getFirst());
        startDate.setPromptText(dates.getFirst());
        endDate.setText(dates.getLast());
        endDate.setPromptText(dates.getLast());
        submit.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
        filter.setOnAction(e -> checkGraph(lineChart, dates, startDate.getText(), endDate.getText()));
    }

    @Override
    public void build() {
        controller = new DashboardController();

        root = new VBox();

        var userManagementMenu = new Menu("User Management");
        var addUserItem = new MenuItem("Add user");
        var modifyUserItem = new MenuItem("Modify user");
        var deleteUserItem = new MenuItem("Delete user");

        var chartSettingsMenu = new Menu("Chart settings");
        var fileSettingsMenu = new Menu("Upload files");
        var fileSettingsMenuItem = new MenuItem("Upload");
        var exportMenu = new Menu("Export");

        var logoutMenu = new Menu("Logout");
        var logoutMenuItem = new MenuItem("Logout");
        logoutMenu.getItems().add(logoutMenuItem);

        userManagementMenu.getItems().addAll(addUserItem,modifyUserItem,deleteUserItem);
        fileSettingsMenu.getItems().add(fileSettingsMenuItem);


        MenuBar menuBar = new MenuBar(fileSettingsMenu,userManagementMenu,chartSettingsMenu,exportMenu,logoutMenu);


        root.getChildren().add(menuBar);

        fileSettingsMenuItem.setOnAction( e->{
            window.loadFileInput();
        });

        addUserItem.setOnAction(e -> {
            window.loadAddUserScene();
        });

        modifyUserItem.setOnAction(e -> {
            window.loadUserManagementScene();
        });

        deleteUserItem.setOnAction(e -> {
            window.loadDeleteUserScene();
        });

        logoutMenuItem.setOnAction(e -> {
            window.loadLoginScene();
        });


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
        impressionsNum.textProperty().bind(controller.impressionsNumProperty().asString());
        impressionMetric.getChildren().addAll(impressionsText, impressionsNum);

        HBox uniqueMetric = new HBox();
        Label uniquesText = new Label(" Num of uniques: ");
        Label uniquesNum = new Label("");
        uniquesNum.textProperty().bind(controller.uniqueNumProperty().asString());
        uniqueMetric.getChildren().addAll(uniquesText, uniquesNum);

        HBox clicksMetric = new HBox();
        Label clicksText = new Label(" Num of clicks: ");
        Label clicksNum = new Label("");
        clicksNum.textProperty().bind(controller.clicksNumProperty().asString());
        clicksMetric.getChildren().addAll(clicksText, clicksNum);

        HBox bounceMetric = new HBox();
        Label bouncesText = new Label(" Num of bounces: ");
        Label bounceNum = new Label("");
        bounceNum.textProperty().bind(controller.bounceNumProperty().asString());
        bounceMetric.getChildren().addAll(bouncesText, bounceNum);

        HBox conversionMetric = new HBox();
        Label conversionsText = new Label(" Num of conversions: ");
        Label conversionsNum = new Label("");
        conversionsNum.textProperty().bind(controller.conversionsNumProperty().asString());
        conversionMetric.getChildren().addAll(conversionsText, conversionsNum);

        HBox totalCostMetric = new HBox();
        Label totalCostText = new Label(" Total cost: £");
        Label totalCostNum = new Label("");
        totalCostNum.textProperty().bind(controller.totalNumProperty().asString());
        totalCostMetric.getChildren().addAll(totalCostText, totalCostNum);

        HBox ctrMetric = new HBox();
        Label ctrText = new Label(" CTR: ");
        Label ctrNum = new Label("");
        ctrNum.textProperty().bind(controller.ctrNumProperty().asString());
        ctrMetric.getChildren().addAll(ctrText, ctrNum);

        HBox cpaMetric = new HBox();
        Label cpaText = new Label(" CPA: £");
        Label cpaNum = new Label();
        cpaNum.textProperty().bind(controller.cpaNumProperty().asString());
        cpaMetric.getChildren().addAll(cpaText, cpaNum);

        HBox cpcMetric = new HBox();
        Label cpcText = new Label(" CPC: £");
        Label cpcNum = new Label("");
        cpcNum.textProperty().bind(controller.cpcNumProperty().asString());
        cpcMetric.getChildren().addAll(cpcText, cpcNum);

        HBox cpmMetric = new HBox();
        Label cpmText = new Label(" CPM: £");
        Label cpmNum = new Label("");
        cpmNum.textProperty().bind(controller.cpmNumProperty().asString());
        cpmMetric.getChildren().addAll(cpmText, cpmNum);

        HBox bounceRateMetric = new HBox();
        Label bounceRateText = new Label(" Bounce rate: ");
        Label bounceRateNum = new Label("");
        bounceRateNum.textProperty().bind(controller.bounceRateNumProperty().asString());
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
        TextField defineBounce = new TextField();
        defineBounce.setPrefWidth(35);
        defineBounce.setPromptText("0-" + controller.maxTime());
        defineBounce.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        defineBounce.textProperty().bindBidirectional(controller.timeValProperty());
        bounceFilter.getChildren().addAll(bounceLabel,timeBounceButton,singlePageBounceButton, defineBounce);

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
        lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        VBox chartVbox = new VBox();
        chartVbox.setPadding(new Insets(5, 0, 0, 0));

        HBox dateSelectionBar = new HBox();
        startDate = new TextField();
        startDate.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        endDate = new TextField();
        endDate.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
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

        HBox choiceBoxContainer = new HBox();
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Impressions", "Uniques", "Clicks", "Bounces", "Conversions", "Total cost", "CTR", "CPA", "CPC", "CPM", "Bounce rate");
        choiceBox.getSelectionModel().select(0);
        choiceBox.setOnAction(e -> {
            controller.graphNumProperty().set(choiceBox.getValue());
            lineChart.getData().clear();
            controller.changeChart(lineChart);
        });
        choiceBoxContainer.getChildren().add(choiceBox);
        choiceBoxContainer.setAlignment(Pos.CENTER);

        chartVbox.getChildren().add(dateSelectionBar);
        chartVbox.getChildren().add(lineChart);
        chartVbox.getChildren().add(choiceBoxContainer);

        filter = new Button("Filter");

        VBox bottom = new VBox();
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().add(filterHBox);
        bottom.getChildren().add(filter);

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
                lineChart.getData().clear();
                controller.calculateMetrics(lineChart, startDate, endDate);
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
