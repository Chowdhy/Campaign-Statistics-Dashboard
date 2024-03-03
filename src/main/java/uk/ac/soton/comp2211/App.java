package uk.ac.soton.comp2211;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.data.Database;
import uk.ac.soton.comp2211.data.calculations.CalculateMetrics;
import uk.ac.soton.comp2211.data.calculations.CampaignDataRetriever;
import uk.ac.soton.comp2211.data.parsing.ClickLogParser;
import uk.ac.soton.comp2211.data.parsing.CsvParser;
import uk.ac.soton.comp2211.data.parsing.ImpressionParser;
import uk.ac.soton.comp2211.data.parsing.ServerLogParser;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * JavaFX App Test1
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");


        SplitPane splitPane = new SplitPane();
        SplitPane leftSplitPane = new SplitPane();

        splitPane.setDividerPosition(0, 0.7);
        leftSplitPane.setDividerPosition(0,0.68);

        VBox metricsVBox = new VBox();
        metricsVBox.setSpacing(12);
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

        LineChart exampleChart = new LineChart(new NumberAxis(), new NumberAxis());


        leftSplitPane.setOrientation(Orientation.VERTICAL);
        leftSplitPane.getItems().add(exampleChart);
        leftSplitPane.getItems().add(filterHBox);


        var scene = new Scene(splitPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private static void setupCampaignDatabase(String impressionLogPath, String clickLogPath, String serverLogPath) {
        String databaseName = "campaign";

        CsvParser impressionParser = new ImpressionParser(databaseName);
        CsvParser clickLogParser = new ClickLogParser(databaseName);
        CsvParser serverLogParser = new ServerLogParser(databaseName);

        try {
            impressionParser.parse(impressionLogPath);
            clickLogParser.parse(clickLogPath);
            serverLogParser.parse(serverLogPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)  {
        var impressionPath = "data/sample_data/2_week_campaign/impression_log.csv";
        var clickPath = "data/sample_data/2_week_campaign/click_log.csv";
        var serverPath = "data/sample_data/2_week_campaign/server_log.csv";

        setupCampaignDatabase(impressionPath, clickPath, serverPath);
        Connection db = Database.getConnection("campaign");
        CampaignDataRetriever b = new CampaignDataRetriever(db);




        launch();
    }
}