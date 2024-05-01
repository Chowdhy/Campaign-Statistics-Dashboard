package uk.ac.soton.comp2211.control;

import javafx.beans.property.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.data.graph.GraphData;
import uk.ac.soton.comp2211.ui.Dialogs;
import uk.ac.soton.comp2211.users.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardController {
    GraphData graphData;


    public DashboardController() {
        graphData = new GraphData();
    }

    public void changeChart(LineChart lineChart, String graphNum) {
        lineChart.getData().clear();
        Pair<ArrayList<Integer>, ArrayList<Double>> data = graphData.getData(graphNum);
        ArrayList<String> dates = graphData.getDates();
        ArrayList<Integer> integerData = data.getKey();
        ArrayList<Double> doubleData = data.getValue();

        XYChart.Series series = new XYChart.Series();
        if (doubleData.size() > 0) {
            for (int i = 0; i < dates.size(); i++) {
                if (buttonValProperty().get().equals("hour")) {
                    series.getData().add(new XYChart.Data((dates.get(i).split("2015-")[1] + ":00"), doubleData.get(i)));
                } else {
                    series.getData().add(new XYChart.Data(dates.get(i).split("2015-")[1], doubleData.get(i)));
                }
            }
        } else {
            for (int i = 0; i < dates.size(); i++) {
                if (buttonValProperty().get().equals("hour")) {
                    series.getData().add(new XYChart.Data((dates.get(i).split("2015-")[1] + ":00"), integerData.get(i)));
                } else {
                    series.getData().add(new XYChart.Data(dates.get(i).split("2015-")[1], integerData.get(i)));
                }
            }
        }

        lineChart.getData().add(series);
    }

    public void calculateMetrics(String startDate, String endDate) {
        if (!checkSanity()) return;

        try {
            graphData.calculateMetrics(startDate, endDate);
        } catch (SQLException e) {
            Dialogs.error(e);
        }
    }

    public ArrayList<String> getDates(String startDate, String endDate) {
        return graphData.getDayDates(startDate, endDate);
    }

    public void setMaxValues() {
        if (!checkSanity()) return;

        try {
            graphData.maxValues();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkSanity() {
        boolean emptyImpressionLog = graphData.isTableEmpty("impression_log");
        boolean emptyServerLog = graphData.isTableEmpty("server_log");
        boolean emptyClickLog = graphData.isTableEmpty("click_log");

        if (emptyImpressionLog && emptyClickLog && emptyServerLog) {
            return false;
        } else if (emptyImpressionLog) {
            Dialogs.error("No impression log data");
            return false;
        } else if (emptyClickLog) {
            Dialogs.error("No click log data");
            return false;
        } else if (emptyServerLog) {
            Dialogs.error("No server log data");
            return false;
        }

        return true;
    }

    public void requestLogout() {
        OperationLogging.logAction("Logged out");

        App.setUser(null);
    }

    public boolean isValidPassword(String password) throws InvalidPasswordException {
        Credentials credentials = new Credentials();

        return credentials.isValidPassword(password);
    }

    public void updatePassword(String password) {
        Credentials credentials = new Credentials();

        try {
            credentials.resetPassword(App.getUser().getUsername(), password);
        } catch (UserDoesntExistException | IncorrectPermissionsException e) {
            throw new RuntimeException(e);
        }
    }

    public BooleanProperty maleProperty(){
        return graphData.maleProperty();
    }

    public BooleanProperty femaleProperty(){
        return graphData.femaleProperty();
    }

    public BooleanProperty lowProperty(){
        return graphData.lowProperty();
    }

    public BooleanProperty mediumProperty(){
        return graphData.mediumProperty();
    }

    public BooleanProperty highProperty(){
        return graphData.highProperty();
    }

    public BooleanProperty under25Property(){
        return graphData.under25Property();
    }

    public BooleanProperty twentiesProperty(){
        return graphData.twentiesProperty();
    }

    public BooleanProperty thirtiesProperty(){
        return graphData.thirtiesProperty();
    }

    public BooleanProperty fortiesProperty(){
        return graphData.fortiesProperty();
    }

    public BooleanProperty above54Property(){
        return graphData.above54Property();
    }

    public BooleanProperty socialMediaProperty(){
        return graphData.socialMediaProperty();
    }

    public BooleanProperty shoppingProperty(){
        return graphData.shoppingProperty();
    }

    public BooleanProperty newsProperty(){
        return graphData.newsProperty();
    }

    public BooleanProperty blogProperty(){
        return graphData.blogProperty();
    }

    public BooleanProperty travelProperty(){
        return graphData.travelProperty();
    }

    public BooleanProperty hobbiesProperty(){
        return graphData.hobbiesProperty();
    }

    public BooleanProperty timeProperty(){
        return graphData.timeProperty();
    }

    public BooleanProperty pageProperty(){
        return graphData.pageProperty();
    }

    public BooleanProperty compareProperty(){
        return graphData.compareProperty();
    }

    public IntegerProperty impressionsNumProperty(){
        return graphData.impressionsNumProperty();
    }

    public IntegerProperty uniqueNumProperty(){
        return graphData.uniqueNumProperty();
    }

    public IntegerProperty clicksNumProperty(){
        return graphData.clicksNumProperty();
    }

    public IntegerProperty conversionsNumProperty(){
        return graphData.conversionsNumProperty();
    }

    public IntegerProperty bounceNumProperty(){
        return graphData.bounceNumProperty();
    }

    public DoubleProperty ctrNumProperty(){
        return graphData.ctrNumProperty();
    }

    public DoubleProperty cpcNumProperty(){
        return graphData.cpcNumProperty();
    }

    public DoubleProperty cpmNumProperty(){
        return graphData.cpmNumProperty();
    }

    public DoubleProperty totalNumProperty(){
        return graphData.totalNumProperty();
    }

    public DoubleProperty cpaNumProperty(){
        return graphData.cpaNumProperty();
    }

    public DoubleProperty bounceRateNumProperty(){
        return graphData.bounceRateNumProperty();
    }

    public StringProperty graphNumProperty() {
        return graphData.graphNumProperty();
    }

    public StringProperty graph2NumProperty() {
        return graphData.graph2NumProperty();
    }

    public SimpleStringProperty timeValProperty(){ return graphData.timeValProperty(); }

    public SimpleStringProperty pageValProperty(){ return graphData.pageValProperty(); }

    public SimpleStringProperty buttonValProperty(){ return graphData.buttonValProperty(); }

    public String maxDate(){ return graphData.getMaxDate(); }

    public Integer maxPage(){ return graphData.getMaxPage(); }

    public Integer maxTime(){ return graphData.getMaxTime(); }
}
