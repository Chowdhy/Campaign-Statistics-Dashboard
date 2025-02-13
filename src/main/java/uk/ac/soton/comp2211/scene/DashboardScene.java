package uk.ac.soton.comp2211.scene;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.control.DashboardController;
import uk.ac.soton.comp2211.control.HistogramController;
import uk.ac.soton.comp2211.ui.MainWindow;
import uk.ac.soton.comp2211.ui.Window;
import uk.ac.soton.comp2211.users.InvalidPasswordException;
import uk.ac.soton.comp2211.users.Permissions;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class DashboardScene extends MainScene {
    DashboardController controller;
    HistogramController hController;

    LineChart<String, Number> lineChart;
    LineChart<String, Number> lineChart2;
    BarChart<String, Number> histogram;
    BarChart<String, Number> histogram2;
    ArrayList<String> dates;
    DatePicker startPicker;
    DatePicker endPicker;
    Button submit;
    Button filter;
    Tooltip tooltip1;
    Tooltip totalTip;
    Tooltip ctrTip;
    Tooltip cpaTip;
    Tooltip cpcTip;
    Tooltip cpmTip;
    Tooltip brTip;
    TextField defineBounce;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    HBox filterHBox;
    VBox chartVbox;
    VBox metricsVBox;
    Label bounceErrorLabel;

    ProgressIndicator progressIndicator;

    public DashboardScene(MainWindow window) {
        super(window);
    }

    @Override
    public void initialise() {
        try {
            controller.setMaxValues();
            tooltip1.setText("Page range: 1-" + controller.maxPage() + "\nTime range: 1-" + controller.maxTime() + "\nMaximum value from inputted data" + "\nUsed to change bounce metrics");
            totalTip.setText("Impression cost + click cost");
            ctrTip.setText("Clicks / impressions");
            cpaTip.setText("Total cost / conversions");
            cpcTip.setText("Click cost / clicks");
            cpmTip.setText("Impressions cost / (impressions/1000)");
            brTip.setText("Bounce / clicks");
            dates = controller.getDates("2015-01-01", controller.maxDate());

            controller.calculateMetrics("2015-01-01", controller.maxDate());
            controller.changeChart(lineChart, controller.graphNumProperty().get());


        startPicker.setValue(LocalDate.parse(dates.getFirst(), formatter));
        endPicker.setValue(LocalDate.parse(dates.getLast(), formatter));
        startPicker.setDayCellFactory(e -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || !dates.contains(date.format(formatter)));
            }
        });
        endPicker.setDayCellFactory(e -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || !dates.contains(date.format(formatter)));
            }
        });

            startPicker.setDayCellFactory(e -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || !dates.contains(date.format(formatter)));
                }
            });
            endPicker.setDayCellFactory(e -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || !dates.contains(date.format(formatter)));
                }
            });

            submit.setOnAction(e -> checkGraph(dates, startPicker.getValue().format(formatter), endPicker.getValue().format(formatter)));
            filter.setOnAction(e -> checkGraph(dates, startPicker.getValue().format(formatter), endPicker.getValue().format(formatter)));

            changeBounce();
        } catch (Exception e) {

        }

        lineChart.setMinWidth(lineChart.getWidth()-150);
        chartVbox.setMinHeight(chartVbox.getHeight()-100);
        metricsVBox.setMinWidth(metricsVBox.getWidth()-75);
    }

    @Override
    public void build() {
        controller = new DashboardController();
        hController = new HistogramController();

        root = new StackPane();

        var mainVBox = new VBox();
        VBox.setVgrow(mainVBox,Priority.ALWAYS);
        root.getChildren().add(mainVBox);

        var optionsMenu = new Menu("Options");
        var uploadMenuItem = new MenuItem("Upload files");
        var userMenuItem = new MenuItem("Manage users");
        var passwordMenuItem = new MenuItem("Change password");
        var logoutMenuItem = new MenuItem("Logout");
        optionsMenu.getItems().addAll(uploadMenuItem,userMenuItem, passwordMenuItem, logoutMenuItem);

        Dialog<Void> passwordDialog = new Dialog<>();
        passwordDialog.setTitle("Change your password");
        passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        passwordMenuItem.setOnAction(e -> passwordDialog.showAndWait());

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Enter new password");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm new password");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red");

        VBox dialogContent = new VBox();
        dialogContent.setMinWidth(275);
        dialogContent.getChildren().addAll(newPassword, confirmPassword, errorLabel);

        Button okButton = (Button) passwordDialog.getDialogPane().lookupButton(ButtonType.OK);

        passwordDialog.getDialogPane().setContent(dialogContent);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (newPassword.getText() == null || newPassword.getText().isEmpty()) {
                errorLabel.setText("Password cannot be empty");
                ae.consume();
            } else if (!newPassword.getText().equals(confirmPassword.getText())) {
                errorLabel.setText("Passwords must be the same");
                ae.consume();
            } else {
                try {
                    controller.isValidPassword(newPassword.getText());
                } catch (InvalidPasswordException e) {
                    errorLabel.setText(e.getMessage());
                    ae.consume();
                }
            }
        });

        passwordDialog.setResultConverter(dialogButton -> {
            if (dialogButton.equals(ButtonType.OK)) {
                controller.updatePassword(newPassword.getText());
            }

            return null;
        });

        var exportMenu = new Menu("Export");

        var graphMenuItem = new MenuItem("Graph");
        graphMenuItem.setOnAction(e -> {
            window.loadExportChartScene(chartVbox.snapshot(new SnapshotParameters(), new WritableImage((int) chartVbox.getWidth(), (int) chartVbox.getHeight())));
        });

        var reportMenuItem = new MenuItem("Report");
        exportMenu.getItems().addAll(graphMenuItem,reportMenuItem);

        var themeMenu = new Menu("Themes");
        var blueTheme = new MenuItem("Blue");
        blueTheme.setOnAction(e -> {
            getScene().getStylesheets().removeFirst();
            getScene().getStylesheets().add("blue.css");
            Window.theme = "blue.css";
        });
        var orangeTheme = new MenuItem("Orange");
        orangeTheme.setOnAction(e -> {
            getScene().getStylesheets().removeFirst();
            getScene().getStylesheets().add("orange.css");
            Window.theme = "orange.css";
        });
        var purpleTheme = new MenuItem("Purple");
        purpleTheme.setOnAction(e -> {
            getScene().getStylesheets().removeFirst();
            getScene().getStylesheets().add("purple.css");
            Window.theme = "purple.css";
        });
        var greenTheme = new MenuItem("Green");
        greenTheme.setOnAction(e -> {
            getScene().getStylesheets().removeFirst();
            getScene().getStylesheets().add("green.css");
            Window.theme = "green.css";
        });
        var redTheme = new MenuItem("Red");
        redTheme.setOnAction(e -> {
            getScene().getStylesheets().removeFirst();
            getScene().getStylesheets().add("red.css");
            Window.theme = "red.css";
        });
        themeMenu.getItems().addAll(blueTheme, redTheme, greenTheme, purpleTheme, orangeTheme);

        var helpMenu = new Menu("Help");
        var helpMenuItem = new MenuItem("User guide");
        helpMenu.getItems().addAll(helpMenuItem);

        MenuBar menuBar = new MenuBar(optionsMenu,exportMenu,themeMenu,helpMenu);

        if(App.getUser().getPermissions().equals(Permissions.EDITOR)){
            userMenuItem.setDisable(true);

        }else if(App.getUser().getPermissions().equals(Permissions.VIEWER)){
            userMenuItem.setDisable(true);
            uploadMenuItem.setDisable(true);

        }

        Circle infoIcon1 = new Circle(10);
        infoIcon1.getStyleClass().add("circle");

        tooltip1 = new Tooltip();

        Text iText = new Text("i");
        iText.setFont(Font.font(16));
        iText.setFill(Color.WHITE);


        StackPane iconWithText = new StackPane();
        iconWithText.getChildren().addAll(infoIcon1, iText);
        Tooltip.install(iconWithText, tooltip1);

        helpMenuItem.setOnAction(e -> {
            try {
                File myFile = new File("src/main/resources/guide.pdf");
                Desktop.getDesktop().open(myFile);
            }catch(IOException e1){

            }
        });


        reportMenuItem.setOnAction(e -> {
            window.loadExportValuesScene(controller.collateNumericalReport());
        });


        mainVBox.getChildren().add(menuBar);

        logoutMenuItem.setOnAction(e -> {
            controller.requestLogout();
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

        splitPane.setDividerPosition(0, 0.72);
        leftSplitPane.setDividerPosition(0,0.68);


        metricsVBox = new VBox();
        VBox.setVgrow(metricsVBox, Priority.ALWAYS);

        metricsVBox.setSpacing(1);

        totalTip = new Tooltip();
        VBox totalCostMetric = new VBox();
        VBox.setVgrow(totalCostMetric,Priority.ALWAYS);
        totalCostMetric.setMaxHeight(200);
        totalCostMetric.getStyleClass().add("totalBox");
        Label totalCostText = new Label("Total cost (£)");
        totalCostText.getStyleClass().add("big-metric-title");
        Label totalCostNum = new Label("");
        totalCostNum.getStyleClass().add("big-metric-val");
        totalCostNum.textProperty().bind(controller.totalNumProperty().asString());
        totalCostMetric.getChildren().addAll(totalCostText, totalCostNum);
        totalCostMetric.setAlignment(Pos.CENTER);
        metricsVBox.getChildren().add(totalCostMetric);
        Tooltip.install(totalCostMetric, totalTip);

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
        impressionMetric.getStyleClass().add("metricBox");
        Label impressionsText = new Label("Num of impressions");
        impressionsText.getStyleClass().add("metric-title");
        Label impressionsNum = new Label("");
        impressionsNum.getStyleClass().add("metric-val");
        impressionsNum.textProperty().bind(controller.impressionsNumProperty().asString());
        impressionMetric.getChildren().addAll(impressionsText, impressionsNum);
        impressionMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(impressionMetric);

        VBox uniqueMetric = new VBox();
        VBox.setVgrow(uniqueMetric,Priority.ALWAYS);
        uniqueMetric.getStyleClass().add("metricBox");
        Label uniquesText = new Label("Num of uniques");
        uniquesText.getStyleClass().add("metric-title");
        Label uniquesNum = new Label("");
        uniquesNum.getStyleClass().add("metric-val");
        uniquesNum.textProperty().bind(controller.uniqueNumProperty().asString());
        uniqueMetric.getChildren().addAll(uniquesText, uniquesNum);
        uniqueMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(uniqueMetric);

        VBox clicksMetric = new VBox();
        VBox.setVgrow(clicksMetric,Priority.ALWAYS);
        clicksMetric.getStyleClass().add("metricBox");
        Label clicksText = new Label("Num of clicks");
        clicksText.getStyleClass().add("metric-title");
        Label clicksNum = new Label("");
        clicksNum.getStyleClass().add("metric-val");
        clicksNum.textProperty().bind(controller.clicksNumProperty().asString());
        clicksMetric.getChildren().addAll(clicksText, clicksNum);
        clicksMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(clicksMetric);

        VBox bounceMetric = new VBox();
        VBox.setVgrow(bounceMetric,Priority.ALWAYS);
        bounceMetric.getStyleClass().add("metricBox");
        Label bouncesText = new Label("Num of bounces");
        bouncesText.getStyleClass().add("metric-title");
        Label bounceNum = new Label("");
        bounceNum.getStyleClass().add("metric-val");
        bounceNum.textProperty().bind(controller.bounceNumProperty().asString());
        bounceMetric.getChildren().addAll(bouncesText, bounceNum);
        bounceMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(bounceMetric);

        VBox conversionMetric = new VBox();
        VBox.setVgrow(conversionMetric,Priority.ALWAYS);
        conversionMetric.getStyleClass().add("metricBox");
        Label conversionsText = new Label("Num of conversions");
        conversionsText.getStyleClass().add("metric-title");
        Label conversionsNum = new Label("");
        conversionsNum.getStyleClass().add("metric-val");
        conversionsNum.textProperty().bind(controller.conversionsNumProperty().asString());
        conversionMetric.getChildren().addAll(conversionsText, conversionsNum);
        conversionMetric.setAlignment(Pos.CENTER);
        leftMetrics.getChildren().add(conversionMetric);

        ctrTip = new Tooltip();
        VBox ctrMetric = new VBox();
        VBox.setVgrow(ctrMetric,Priority.ALWAYS);
        ctrMetric.getStyleClass().add("metricBox");
        Label ctrText = new Label("CTR");
        ctrText.getStyleClass().add("metric-title");
        Label ctrNum = new Label("");
        ctrNum.getStyleClass().add("metric-val");
        ctrNum.textProperty().bind(controller.ctrNumProperty().asString());
        ctrMetric.getChildren().addAll(ctrText, ctrNum);
        ctrMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(ctrMetric);
        Tooltip.install(ctrMetric, ctrTip);

        cpaTip = new Tooltip();
        VBox cpaMetric = new VBox();
        VBox.setVgrow(cpaMetric,Priority.ALWAYS);
        cpaMetric.getStyleClass().add("metricBox");
        Label cpaText = new Label("CPA (£)");
        cpaText.getStyleClass().add("metric-title");
        Label cpaNum = new Label();
        cpaNum.getStyleClass().add("metric-val");
        cpaNum.textProperty().bind(controller.cpaNumProperty().asString());
        cpaMetric.getChildren().addAll(cpaText, cpaNum);
        cpaMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(cpaMetric);
        Tooltip.install(cpaMetric, cpaTip);

        cpcTip = new Tooltip();
        VBox cpcMetric = new VBox();
        VBox.setVgrow(cpcMetric,Priority.ALWAYS);
        cpcMetric.getStyleClass().add("metricBox");
        Label cpcText = new Label("CPC (£)");
        cpcText.getStyleClass().add("metric-title");
        Label cpcNum = new Label("");
        cpcNum.getStyleClass().add("metric-val");
        cpcNum.textProperty().bind(controller.cpcNumProperty().asString());
        cpcMetric.getChildren().addAll(cpcText, cpcNum);
        cpcMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(cpcMetric);
        Tooltip.install(cpcMetric, cpcTip);

        cpmTip = new Tooltip();
        VBox cpmMetric = new VBox();
        VBox.setVgrow(cpmMetric,Priority.ALWAYS);
        cpmMetric.getStyleClass().add("metricBox");
        Label cpmText = new Label("CPM (£)");
        cpmText.getStyleClass().add("metric-title");
        Label cpmNum = new Label("");
        cpmNum.getStyleClass().add("metric-val");
        cpmNum.textProperty().bind(controller.cpmNumProperty().asString());
        cpmMetric.getChildren().addAll(cpmText, cpmNum);
        cpmMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(cpmMetric);
        Tooltip.install(cpmMetric, cpmTip);

        brTip = new Tooltip();
        VBox bounceRateMetric = new VBox();
        VBox.setVgrow(bounceRateMetric,Priority.ALWAYS);
        bounceRateMetric.getStyleClass().add("metricBox");
        Label bounceRateText = new Label("Bounce rate");
        bounceRateText.getStyleClass().add("metric-title");
        Label bounceRateNum = new Label("");
        bounceRateNum.getStyleClass().add("metric-val");
        bounceRateNum.textProperty().bind(controller.bounceRateNumProperty().asString());
        bounceRateMetric.getChildren().addAll(bounceRateText, bounceRateNum);
        bounceRateMetric.setAlignment(Pos.CENTER);
        rightMetrics.getChildren().add(bounceRateMetric);
        Tooltip.install(bounceRateMetric, brTip);

        splitPane.getItems().addAll(leftSplitPane, metricsVBox);



        filterHBox = new HBox();
        filterHBox.setSpacing(20);
        VBox bounceFilter = new VBox();
        VBox genderFilters = new VBox();
        VBox incomeFilters = new VBox();
        VBox contextFilters = new VBox();
        VBox ageGroupFilters = new VBox();

        Label bounceLabel = new Label("Bounce");
        bounceLabel.getStyleClass().add("filter-title");
        ToggleGroup group = new ToggleGroup();
        RadioButton timeBounceButton = new RadioButton("Time");
        timeBounceButton.getStyleClass().add("filter-text");
        timeBounceButton.setToggleGroup(group);
        timeBounceButton.setSelected(true);
        RadioButton singlePageBounceButton = new RadioButton("Pages");
        singlePageBounceButton.getStyleClass().add("filter-text");
        singlePageBounceButton.setToggleGroup(group);
        defineBounce = new TextField();
        UnaryOperator<TextFormatter.Change> bounceInputController = change -> {
            String text = change.getText();
            if (text.matches("\\d?")) {
                return change;
            }
            return null;
        };
        defineBounce.setTextFormatter(new TextFormatter<String>(bounceInputController));
        defineBounce.setPrefWidth(50);
        defineBounce.setPromptText("1-" + controller.maxTime());
        defineBounce.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        defineBounce.textProperty().bindBidirectional(controller.timeValProperty());
        var inputAndTips = new HBox();
        inputAndTips.getChildren().addAll(defineBounce,iconWithText);
        inputAndTips.setSpacing(2);

        bounceErrorLabel = new Label();
        bounceErrorLabel.setStyle("-fx-text-fill: red");

        bounceFilter.getChildren().addAll(bounceLabel,timeBounceButton,singlePageBounceButton, inputAndTips, bounceErrorLabel);
        bounceFilter.setSpacing(5);

        Label genderLabel = new Label("Gender");
        genderLabel.getStyleClass().add("filter-title");
        CheckBox maleButton = new CheckBox("Male");
        maleButton.getStyleClass().add("filter-text");
        maleButton.setSelected(true);
        CheckBox femaleButton = new CheckBox("Female");
        femaleButton.getStyleClass().add("filter-text");
        femaleButton.setSelected(true);
        genderFilters.getChildren().addAll(genderLabel,maleButton,femaleButton);
        genderFilters.setSpacing(5);

        Label incomeLabel = new Label("Income");
        incomeLabel.getStyleClass().add("filter-title");
        CheckBox lowButton = new CheckBox("Low");
        lowButton.getStyleClass().add("filter-text");
        lowButton.setSelected(true);
        CheckBox mediumButton = new CheckBox("Medium");
        mediumButton.getStyleClass().add("filter-text");
        mediumButton.setSelected(true);
        CheckBox highButton = new CheckBox("High");
        highButton.getStyleClass().add("filter-text");
        highButton.setSelected(true);
        incomeFilters.getChildren().addAll(incomeLabel,lowButton,mediumButton,highButton);
        incomeFilters.setSpacing(5);

        Label ageGroupLabel = new Label("Age group");
        ageGroupLabel.getStyleClass().add("filter-title");
        CheckBox under25Button = new CheckBox("Under 25");
        under25Button.getStyleClass().add("filter-text");
        under25Button.setSelected(true);
        CheckBox twentiesButton = new CheckBox("25-34");
        twentiesButton.getStyleClass().add("filter-text");
        twentiesButton.setSelected(true);
        CheckBox thirtiesButton = new CheckBox("35-44");
        thirtiesButton.getStyleClass().add("filter-text");
        thirtiesButton.setSelected(true);
        CheckBox fortiesButton = new CheckBox("45-54");
        fortiesButton.getStyleClass().add("filter-text");
        fortiesButton.setSelected(true);
        CheckBox above54Button = new CheckBox("Above 54");
        above54Button.getStyleClass().add("filter-text");
        above54Button.setSelected(true);
        ageGroupFilters.getChildren().addAll(ageGroupLabel,under25Button,twentiesButton,thirtiesButton,fortiesButton,above54Button);
        ageGroupFilters.setSpacing(5);

        Label contextLabel = new Label("Context");
        contextLabel.getStyleClass().add("filter-title");
        CheckBox socialMediaButton = new CheckBox("Social media");
        socialMediaButton.getStyleClass().add("filter-text");
        socialMediaButton.setSelected(true);
        CheckBox shoppingButton = new CheckBox("Shopping");
        shoppingButton.getStyleClass().add("filter-text");
        shoppingButton.setSelected(true);
        CheckBox blogButton = new CheckBox("Blog");
        blogButton.getStyleClass().add("filter-text");
        blogButton.setSelected(true);
        CheckBox newsButton = new CheckBox("News");
        newsButton.getStyleClass().add("filter-text");
        newsButton.setSelected(true);
        CheckBox hobbiesButton = new CheckBox("Hobbies");
        hobbiesButton.getStyleClass().add("filter-text");
        hobbiesButton.setSelected(true);
        CheckBox travelButton = new CheckBox("Travel");
        travelButton.getStyleClass().add("filter-text");
        travelButton.setSelected(true);
        contextFilters.getChildren().addAll(contextLabel,socialMediaButton,shoppingButton,blogButton,newsButton,hobbiesButton,travelButton);
        contextFilters.setSpacing(5);

        filterHBox.getChildren().addAll(bounceFilter,genderFilters,incomeFilters,ageGroupFilters,contextFilters);
        filterHBox.setSpacing(50);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);

        chartVbox = new VBox();
        chartVbox.setPadding(new Insets(5, 0, 0, 0));
        chartVbox.setSpacing(10);
        chartVbox.setAlignment(Pos.CENTER);

        HBox dateSelectionBar = new HBox();

        startPicker = new DatePicker();
        startPicker.getStyleClass().add("date-picker");
        startPicker.getEditor().setDisable(true);
        startPicker.getEditor().setOpacity(1);
        endPicker = new DatePicker();
        startPicker.getStyleClass().add("date-picker");
        endPicker.getEditor().setDisable(true);
        endPicker.getEditor().setOpacity(1);

        startPicker.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        endPicker.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");
        submit = new Button("Submit");
        submit.getStyleClass().add("fill-button");

        dateSelectionBar.getChildren().addAll(startPicker, endPicker, submit);
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
        timeBounceButton.setOnAction(e -> changeBounce());
        singlePageBounceButton.selectedProperty().bindBidirectional(controller.pageProperty());
        singlePageBounceButton.setOnAction(e -> changeBounce());

        CategoryAxis xHAxis = new CategoryAxis();
        NumberAxis yHAxis = new NumberAxis();
        histogram = new BarChart<>(xHAxis, yHAxis);
        histogram.setLegendVisible(false);
        histogram.setAnimated(false);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Impressions", "Uniques", "Clicks", "Bounces", "Conversions", "Total cost", "CTR", "CPA", "CPC", "CPM", "Bounce rate","Cost Distribution Histogram");
        choiceBox.getSelectionModel().select(0);
        choiceBox.setOnAction(e2 -> {
            String selectedValue = choiceBox.getValue();
            if (selectedValue.equals("Cost Distribution Histogram")) {
                chartVbox.getChildren().remove(lineChart);
                hController.histogram(controller, histogram);
                chartVbox.getChildren().add(1, histogram);
            } else {
                if (chartVbox.getChildren().contains(histogram)){
                    chartVbox.getChildren().remove(histogram);
                    chartVbox.getChildren().add(1, lineChart);
                }
                controller.graphNumProperty().set(selectedValue);
                controller.changeChart(lineChart, selectedValue);
            }
        });

        ToggleGroup timeToggleGroup = new ToggleGroup();
        HBox graphTime = new HBox();

        ToggleButton hour = new ToggleButton("Hour");
        hour.getStyleClass().add("outline-button");
        hour.setToggleGroup(timeToggleGroup);
        hour.setOnAction(e -> {
            controller.buttonValProperty().set("hour");
            controller.changeChart(lineChart, controller.graphNumProperty().get());
            if (controller.compareProperty().get()) {
                controller.changeChart(lineChart2, controller.graph2NumProperty().get());
            }
        });

        ToggleButton day = new ToggleButton("Day");
        day.getStyleClass().add("outline-button");
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
        week.getStyleClass().add("outline-button");
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

        CategoryAxis xH2Axis = new CategoryAxis();
        NumberAxis yH2Axis = new NumberAxis();
        histogram2 = new BarChart<>(xH2Axis, yH2Axis);
        histogram2.setLegendVisible(false);
        histogram2.setAnimated(false);

        ChoiceBox<String> choiceBox2 = new ChoiceBox<>();
        choiceBox2.getItems().addAll("Impressions", "Uniques", "Clicks", "Bounces", "Conversions", "Total cost", "CTR", "CPA", "CPC", "CPM", "Bounce rate","Cost Distribution Histogram");
        choiceBox2.getSelectionModel().select(0);
        choiceBox2.setOnAction(e2 -> {
            String selectedValue = choiceBox2.getValue();
            if (selectedValue.equals("Cost Distribution Histogram")) {
                chartVbox.getChildren().remove(lineChart2);
                hController.histogram(controller, histogram2);
                chartVbox.getChildren().add(2, histogram2);
            } else {
                if (chartVbox.getChildren().contains(histogram2)){
                    chartVbox.getChildren().remove(histogram2);
                    chartVbox.getChildren().add(2, lineChart2);
                }
                controller.graph2NumProperty().set(selectedValue);
                controller.changeChart(lineChart2, selectedValue);
            }
        });

        Button compare = new Button("Compare");
        compare.getStyleClass().add("outline-button");
        compare.setOnAction(e -> {
            controller.compareProperty().set(!controller.compareProperty().get());
            if (controller.compareProperty().get()) {
                controller.changeChart(lineChart2, controller.graph2NumProperty().get());
                chartVbox.getChildren().add(2, lineChart2);
                graphOptions.getChildren().add(choiceBox2);
                compare.getStyleClass().remove("outline-button");
                compare.getStyleClass().add("fill-button");
            } else {
                chartVbox.getChildren().remove(lineChart2);
                graphOptions.getChildren().remove(choiceBox2);
                compare.getStyleClass().remove("fill-button");
                compare.getStyleClass().add("outline-button");
            }
        });


        graphOptions.getChildren().addAll(compare, graphTime, choiceBox);
        graphOptions.setAlignment(Pos.CENTER);
        graphOptions.setSpacing(10);

        chartVbox.getChildren().add(dateSelectionBar);
        chartVbox.getChildren().add(lineChart);
        chartVbox.getChildren().add(graphOptions);
        VBox.setVgrow(chartVbox, Priority.ALWAYS);
        chartVbox.getStyleClass().add("graphContainer");

        filter = new Button("Filter");
        filter.getStyleClass().add("filter-button");

        var filterButtonHBox = new HBox();

        progressIndicator = new ProgressIndicator();
        progressIndicator.isIndeterminate();
        progressIndicator.setVisible(false);

        filterButtonHBox.getChildren().addAll(filter,progressIndicator);
        filterButtonHBox.setAlignment(Pos.CENTER);

        VBox bottom = new VBox();
        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().add(filterHBox);
        bottom.getChildren().add(filterButtonHBox);
        bottom.setSpacing(10);
        bottom.setAlignment(Pos.CENTER);
        filterHBox.setAlignment(Pos.CENTER);

        VBox.setVgrow(chartVbox,Priority.ALWAYS);


        leftSplitPane.setOrientation(Orientation.VERTICAL);
        leftSplitPane.setStyle("-fx-background-color: white;");
        leftSplitPane.getItems().add(chartVbox);
        leftSplitPane.getItems().add(bottom);
    }

    @Override
    public void cleanup() {

    }

    public void checkGraph(ArrayList<String> dates, String startDate, String endDate) {
        try {
            if (dates.contains(startDate) && dates.contains(endDate)) {
                if (controller.maxTime() >= Integer.parseInt(controller.timeValProperty().get()) && 1 <= Integer.parseInt(controller.timeValProperty().get()) && controller.maxPage() >= Integer.parseInt(controller.pageValProperty().get()) && 1 <= Integer.parseInt(controller.pageValProperty().get())) {
                    bounceErrorLabel.setText("");

                    controller.calculateMetrics(startDate, endDate);
                    controller.changeChart(lineChart, controller.graphNumProperty().get());
                    hController.histogram(controller, histogram);
                    if (controller.compareProperty().get()) {
                        controller.changeChart(lineChart2, controller.graph2NumProperty().get());
                        hController.histogram(controller, histogram2);
                    }
                } else {
                    bounceErrorLabel.setText("Out of range");
                }
            }
        } catch(Exception ignored) {

        }
    }

    public void changeBounce(){
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
