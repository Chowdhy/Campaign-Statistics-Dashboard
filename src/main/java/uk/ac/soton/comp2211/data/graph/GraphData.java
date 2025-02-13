package uk.ac.soton.comp2211.data.graph;

import javafx.beans.property.*;
import javafx.util.Pair;
import uk.ac.soton.comp2211.data.Database;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class GraphData {

    BooleanProperty male = new SimpleBooleanProperty(true);
    BooleanProperty female = new SimpleBooleanProperty(true);
    BooleanProperty low = new SimpleBooleanProperty(true);
    BooleanProperty medium = new SimpleBooleanProperty(true);
    BooleanProperty high = new SimpleBooleanProperty(true);
    BooleanProperty under25 = new SimpleBooleanProperty(true);
    BooleanProperty twenties = new SimpleBooleanProperty(true);
    BooleanProperty thirties = new SimpleBooleanProperty(true);
    BooleanProperty forties = new SimpleBooleanProperty(true);
    BooleanProperty above54 = new SimpleBooleanProperty(true);
    BooleanProperty socialMedia = new SimpleBooleanProperty(true);
    BooleanProperty shopping = new SimpleBooleanProperty(true);
    BooleanProperty news = new SimpleBooleanProperty(true);
    BooleanProperty travel = new SimpleBooleanProperty(true);
    BooleanProperty blog = new SimpleBooleanProperty(true);
    BooleanProperty hobbies = new SimpleBooleanProperty(true);
    BooleanProperty time = new SimpleBooleanProperty(true);
    BooleanProperty page = new SimpleBooleanProperty(false);
    BooleanProperty compare = new SimpleBooleanProperty(false);

    IntegerProperty impressionsNum = new SimpleIntegerProperty(0);
    IntegerProperty uniqueNum = new SimpleIntegerProperty(0);
    IntegerProperty clicksNum = new SimpleIntegerProperty(0);
    IntegerProperty conversionsNum = new SimpleIntegerProperty(0);
    IntegerProperty bounceNum = new SimpleIntegerProperty(0);
    DoubleProperty ctrNum = new SimpleDoubleProperty(0);
    DoubleProperty cpcNum = new SimpleDoubleProperty(0);
    DoubleProperty cpmNum = new SimpleDoubleProperty(0);
    DoubleProperty totalNum = new SimpleDoubleProperty(0);
    DoubleProperty cpaNum = new SimpleDoubleProperty(0);
    DoubleProperty bounceRateNum = new SimpleDoubleProperty(0);
    StringProperty graph = new SimpleStringProperty("Impressions");
    StringProperty graph2 = new SimpleStringProperty("Impressions");

    SimpleStringProperty timeVal = new SimpleStringProperty("1");
    SimpleStringProperty pageVal = new SimpleStringProperty("1");
    SimpleStringProperty buttonVal = new SimpleStringProperty("day");

    ArrayList<Integer> impressions;
    ArrayList<Integer> uniques;
    ArrayList<Integer> clicks;
    ArrayList<Integer> pages;
    ArrayList<Integer> times;
    ArrayList<Integer> conversions;
    ArrayList<Double> clickCost;
    ArrayList<Double> impressionCost;
    ArrayList<Double> totals;
    ArrayList<String> controlDates;
    ArrayList<String> dates;
    String maxDate;
    Integer maxPage;
    Integer maxTime;

    private Connection connect() {
        return Database.getConnection("campaign");
    }

    public boolean isTableEmpty(String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;

        try (Connection conn = this.connect();
             Statement stat = conn.createStatement()) {
            ResultSet rs = stat.executeQuery(query);

            if (rs.next()) {
                return (rs.getInt(1) == 0);
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<String> getDayDates(String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        ArrayList<String> dates = new ArrayList<>();
        while (!start.isAfter(end)) {
            dates.add(start.toString());
            start = start.plusDays(1);
        }
        return dates;
    }

    public ArrayList<String> getHourDates() {
        ArrayList<String> newDates = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>(Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"));
        for (int n = 0; n < controlDates.size(); n++) {
            if (n == 0 && controlDates.get(n).equals("2015-01-01")) {
                for (int m = 12; m < times.size(); m++) {
                    newDates.add(controlDates.get(n) + " " + times.get(m));
                }
            } else if (n == controlDates.size()-1 && (controlDates.get(n).equals("2015-01-14") || controlDates.get(n).equals("2015-02-28"))) {
                for (int m = 0; m < 13; m++) {
                    newDates.add(controlDates.get(n) + " " + times.get(m));
                }
            } else {
                for (int m = 0; m < times.size(); m++) {
                    newDates.add(controlDates.get(n) + " " + times.get(m));
                }
            }
        }
        return newDates;
    }

    public ArrayList<String> getWeekDates() {
        ArrayList<String> newDates = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < controlDates.size(); i++) {
            if (counter == 0) {
                newDates.add(controlDates.get(i));
                counter++;
            } else if (counter == 6) {
                counter = 0;
            } else {
                counter++;
            }
        }
        return newDates;
    }

    public void maxValues(){
        String maxDateSQL = "SELECT MAX(DATE(date)) FROM impression_log";
        String maxPageSQL = "SELECT MAX(pages_viewed) FROM server_log";
        String maxTimeSQL = "SELECT TIME(MAX(julianday(datetime(exit_date)) - julianday(datetime(entry_date)))) FROM server_log";
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement()) {

            ResultSet dateRS = stmt.executeQuery(maxDateSQL);
            maxDate = dateRS.getString("MAX(DATE(date))");

            ResultSet pageRS = stmt.executeQuery(maxPageSQL);
            maxPage = pageRS.getInt("MAX(pages_viewed)");

            ResultSet timeRS = stmt.executeQuery(maxTimeSQL);
            maxTime = Integer.parseInt(timeRS.getString("TIME(MAX(julianday(datetime(exit_date)) - julianday(datetime(entry_date))))").split(":")[1]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String filterSQL(String startDate, String endDate){
        String startFilter = "DATETIME('" + startDate + " 00:00:01') < date ";
        String endFilter = "AND DATETIME('" + endDate + " 23:59:59') > date";
        String sql = "WHERE " + startFilter + endFilter;

        if (!male.get()) { sql += " AND gender != 'Male'"; }
        if (!female.get()) { sql += " AND gender != 'Female'"; }
        if (!low.get()) { sql += " AND income != 'Low'"; }
        if (!medium.get()) { sql += " AND income != 'Medium'"; }
        if (!high.get()) { sql += " AND income != 'High'"; }
        if (!under25.get()) { sql += " AND age != '<25'"; }
        if (!twenties.get()) { sql += " AND age != '25-34'"; }
        if (!thirties.get()) { sql += " AND age != '35-44'"; }
        if (!forties.get()) { sql += " AND age != '45-54'"; }
        if (!above54.get()) { sql += " AND age != '>54'"; }
        if (!socialMedia.get()) { sql += " AND context != 'Social Media'"; }
        if (!shopping.get()) { sql += " AND context != 'Shopping'"; }
        if (!blog.get()) { sql += " AND context != 'Blog'"; }
        if (!news.get()) { sql += " AND context != 'News'"; }
        if (!hobbies.get()) { sql += " AND context != 'Hobbies'"; }
        if (!travel.get()) { sql += " AND context != 'Travel'"; }

        return sql;
    }

    public ArrayList<Integer> getIntMetric(Statement stmt, String sql) throws SQLException {
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<Integer> result = new ArrayList<>();
        String count = sql.contains("COUNT(*)") ? "COUNT(*)" : "COUNT(DISTINCT clicks.id)";
        String date = sql.contains("date") ? "date" : "impressions.date";
        int i = 0;
        while (rs.next()) {
            while (!rs.getString(date).contains(dates.get(i))) {
                result.add(0);
                i += 1;
            }
            result.add(Integer.parseInt(rs.getString(count)));
            i += 1;
        }
        while (result.size() < dates.size()) {
            result.add(0);
        }
        return result;
    }

    public ArrayList<Double> getDoubleMetric(Statement stmt, String sql) throws SQLException {
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<Double> result = new ArrayList<>();
        String sum = sql.contains("SUM(click_cost)") ? "SUM(click_cost)" : "SUM(impression_cost)";
        String date = sql.contains("date") ? "date" : "impressions.date";
        int i = 0;
        while (rs.next()) {
            while (!rs.getString(date).contains(dates.get(i))) {
                result.add((double) 0);
                i += 1;
            }
            result.add(Double.parseDouble(rs.getString(sum)));
            i += 1;
        }
        while (result.size() < dates.size()) {
            result.add((double) 0);
        }
        return result;
    }

    public void calculateMetrics(String startDate, String endDate) throws SQLException {
        String sqlFilters = filterSQL(startDate, endDate);
        String impressionSQL = "SELECT COUNT(*), date FROM impression_log " + sqlFilters + " GROUP BY strftime('%Y-%m-%d', date), strftime('%H', date)";
        String uniqueSQL = "WITH clicks AS (SELECT DISTINCT id FROM click_log), impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), impressions.date FROM clicks INNER JOIN impressions ON clicks.id = impressions.id GROUP BY strftime('%Y-%m-%d', impressions.date), strftime('%H', impressions.date)";
        String clickSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), impressions.date FROM click_log INNER JOIN impressions ON click_log.id = impressions.id GROUP BY strftime('%Y-%m-%d', impressions.date), strftime('%H', impressions.date)";
        String pageSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), impressions.date FROM server_log INNER JOIN impressions ON server_log.id = impressions.id WHERE server_log.pages_viewed <= " + pageVal.get() + " GROUP BY strftime('%Y-%m-%d', impressions.date), strftime('%H', impressions.date)";
        String timeSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), impressions.date FROM server_log INNER JOIN impressions ON server_log.id = impressions.id WHERE time(server_log.exit_date) < time(server_log.entry_date, '+" + timeVal.get() + " minutes') GROUP BY strftime('%Y-%m-%d', impressions.date), strftime('%H', impressions.date)";
        String conversionSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), impressions.date FROM server_log INNER JOIN impressions ON server_log.id = impressions.id WHERE server_log.conversion = 'Yes' GROUP BY strftime('%Y-%m-%d', impressions.date), strftime('%H', impressions.date)";
        String clickCostSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT SUM(click_cost), impressions.date FROM click_log INNER JOIN impressions ON click_log.id = impressions.id GROUP BY strftime('%Y-%m-%d', impressions.date), strftime('%H', impressions.date)";
        String impressionCostSQL = "SELECT SUM(impression_cost), date FROM impression_log " + sqlFilters + " GROUP BY strftime('%Y-%m-%d', date), strftime('%H', date)";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {

            DecimalFormat value = new DecimalFormat("#.00000");
            DecimalFormat cost = new DecimalFormat("#.00");
            controlDates = getDayDates(startDate, endDate);
            dates = getHourDates();

            impressions = getIntMetric(stmt, impressionSQL);
            uniques = getIntMetric(stmt, uniqueSQL);
            clicks = getIntMetric(stmt, clickSQL);
            pages = getIntMetric(stmt, pageSQL);
            times = getIntMetric(stmt, timeSQL);
            conversions = getIntMetric(stmt, conversionSQL);
            clickCost = getDoubleMetric(stmt, clickCostSQL);
            impressionCost = getDoubleMetric(stmt, impressionCostSQL);
            totals = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                totals.add((clickCost.get(i) + impressionCost.get(i))/100);
            }

            impressionsNum.set(impressions.stream().mapToInt(a -> a).sum());
            uniqueNum.set(uniques.stream().mapToInt(a -> a).sum());
            clicksNum.set(clicks.stream().mapToInt(a -> a).sum());
            if (page.get()) {
                bounceNum.set(pages.stream().mapToInt(a -> a).sum());
            } else {
                bounceNum.set(times.stream().mapToInt(a -> a).sum());
            }
            conversionsNum.set(conversions.stream().mapToInt(a -> a).sum());
            double clickCostNum = clickCost.stream().mapToDouble(a -> a).sum()/100;
            double impressionCostNum = impressionCost.stream().mapToDouble(a -> a).sum()/100;
            totalNum.set(Double.parseDouble(cost.format(clickCostNum + impressionCostNum)));
            ctrNum.set(Double.parseDouble(value.format((double) clicksNum.get() / impressionsNum.get())));
            cpaNum.set(Double.parseDouble(cost.format(totalNum.get() / conversionsNum.get())));
            cpcNum.set(Double.parseDouble(cost.format(clickCostNum / clicksNum.get())));
            cpmNum.set(Double.parseDouble(cost.format(impressionCostNum / ((double) impressionsNum.get() / 1000))));
            bounceRateNum.set(Double.parseDouble(value.format((double) bounceNum.get() / clicksNum.get())));
        }
    }
    public Pair<ArrayList<Integer>, ArrayList<Double>> getData(String graphNum) {
        ArrayList<Integer> integerData = null;
        ArrayList<Double> doubleData = new ArrayList<>();

        dates = getHourDates();
        if (graphNum.equals("Impressions")) {
            integerData = impressions;
        } else if (graphNum.equals("Clicks")) {
            integerData = clicks;
        } else if (graphNum.equals("Uniques")) {
            integerData = uniques;
        } else if (graphNum.equals("Bounces")) {
            if (page.get()) {
                integerData = pages;
            } else {
                integerData = times;
            }
        } else if (graphNum.equals("Conversions")) {
            integerData = conversions;
        } else if (graphNum.equals("Total cost")) {
            for (int i = 0; i < dates.size(); i++) {
                doubleData.add(totals.get(i));
            }
        } else if (graphNum.equals("CTR")) {
            for (int i = 0; i < dates.size(); i++) {
                if (impressions.get(i) == 0) {
                    doubleData.add((double) 0);
                } else {
                    doubleData.add((double) clicks.get(i) / impressions.get(i));
                }
            }
        } else if (graphNum.equals("CPA")) {
            for (int i = 0; i < dates.size(); i++) {
                if (conversions.get(i) == 0) {
                    doubleData.add((double) 0);
                } else {
                    doubleData.add(totals.get(i) / conversions.get(i));
                }
            }
        } else if (graphNum.equals("CPC")) {
            for (int i = 0; i < dates.size(); i++) {
                if (clicks.get(i) == 0) {
                    doubleData.add((double) 0);
                } else {
                    doubleData.add(clickCost.get(i) / clicks.get(i));
                }
            }
        } else if (graphNum.equals("CPM")) {
            for (int i = 0; i < dates.size(); i++) {
                if (impressions.get(i) == 0) {
                    doubleData.add((double) 0);
                } else {
                    doubleData.add(impressionCost.get(i) / ((double) impressions.get(i) / 1000));
                }
            }
        } else if (graphNum.equals("Bounce rate")) {
            for (int i = 0; i < dates.size(); i++) {
                if (clicks.get(i) == 0) {
                    doubleData.add((double) 0);
                } else {
                    if (page.get()) {
                        doubleData.add((double) pages.get(i) / clicks.get(i));
                    } else {
                        doubleData.add((double) times.get(i) / clicks.get(i));
                    }
                }
            }
        }

        if (buttonVal.get().equals("hour")) {
            dates = getHourDates();
        } else if (buttonVal.get().equals("day")) {
            dates = controlDates;
        } else if (buttonVal.get().equals("week")) {
            dates = getWeekDates();
        }

        if (integerData != null) {
            if (buttonVal.get().equals("day")) {
                integerData = getIntGraphTime(integerData, 24);
            } else if (buttonVal.get().equals("week")) {
                integerData = getIntGraphTime(integerData, 168);
            }
        }

        if (!doubleData.isEmpty()) {
            if (buttonVal.get().equals("day")) {
                doubleData = getDoubleGraphTime(doubleData, 24, graphNum);
            } else if (buttonVal.get().equals("week")) {
                doubleData = getDoubleGraphTime(doubleData, 168, graphNum);
            }
        }

        return new Pair<>(integerData, doubleData);
    }

    public ArrayList<Integer> getIntGraphTime(ArrayList<Integer> data, int loopy) {
        ArrayList<Integer> newData = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (loopy == 24 && dates.get(0).equals("2015-01-01") && i == 12) {
                counter = 0;
            }
            if (loopy == 168 && dates.get(0).equals("2015-01-01") && i == 12) {
                counter = 24;
            }
            if (counter == 0) {
                newData.add(data.get(i));
                counter++;
            } else if (counter == loopy-1) {
                newData.set(newData.size()-1, newData.getLast()+data.get(i));
                counter = 0;
            } else {
                newData.set(newData.size()-1, newData.getLast()+data.get(i));
                counter++;
            }
        }
        return newData;
    }

    public ArrayList<Double> getDoubleGraphTime(ArrayList<Double> data, int loopy, String graphNum) {
        ArrayList<Double> newData = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < data.size(); i++) {
            if (loopy == 24 && dates.get(0).equals("2015-01-01") && i == 12) {
                counter = 0;
            }
            if (loopy == 168 && dates.get(0).equals("2015-01-01") && i == 12) {
                counter = 24;
            }
            if (counter == 0) {
                newData.add(data.get(i));
                counter++;
            } else if (counter == loopy-1) {
                newData.set(newData.size()-1, newData.getLast()+data.get(i));
                counter = 0;
            } else {
                newData.set(newData.size()-1, newData.getLast()+data.get(i));
                counter++;
            }
        }

        int sum = counter == 0 ? 157 : counter;

        if (loopy == 24 && !graphNum.equals("Total cost")) {
            for (int i = 0; i < newData.size(); i++) {
                if (i == 0 && dates.get(0).equals("2015-01-01")) {
                    newData.set(i, newData.get(i)/12);
                } else if (i == newData.size()-1 && (dates.get(i).equals("2015-01-14") || dates.get(i).equals("2015-02-28"))) {
                    newData.set(i, newData.get(i)/13);
                } else {
                    newData.set(i, newData.get(i)/24);
                }
            }
        }

        if (loopy == 168 && !graphNum.equals("Total cost")) {
            for (int i = 0; i < newData.size(); i++) {
                if (i == 0 && dates.get(0).equals("2015-01-01")) {
                    newData.set(i, newData.get(i)/156);
                } else if (i == newData.size()-1 && (dates.get(i).equals("2015-01-14") || dates.get(i).equals("2015-02-28"))) {
                    newData.set(i, newData.get(i) / 157);
                } else if (i == newData.size()-1) {
                    newData.set(i, newData.get(i) / sum);
                } else {
                    newData.set(i, newData.get(i)/168);
                }
            }
        }

        return newData;
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public BooleanProperty maleProperty(){
        return male;
    }

    public BooleanProperty femaleProperty(){
        return female;
    }

    public BooleanProperty lowProperty(){
        return low;
    }

    public BooleanProperty mediumProperty(){
        return medium;
    }

    public BooleanProperty highProperty(){
        return high;
    }

    public BooleanProperty under25Property(){
        return under25;
    }

    public BooleanProperty twentiesProperty(){
        return twenties;
    }

    public BooleanProperty thirtiesProperty(){
        return thirties;
    }

    public BooleanProperty fortiesProperty(){
        return forties;
    }

    public BooleanProperty above54Property(){
        return above54;
    }

    public BooleanProperty socialMediaProperty(){
        return socialMedia;
    }

    public BooleanProperty shoppingProperty(){
        return shopping;
    }

    public BooleanProperty newsProperty(){
        return news;
    }

    public BooleanProperty blogProperty(){
        return blog;
    }

    public BooleanProperty travelProperty(){
        return travel;
    }

    public BooleanProperty hobbiesProperty(){
        return hobbies;
    }

    public BooleanProperty timeProperty(){
        return time;
    }

    public BooleanProperty pageProperty(){
        return page;
    }

    public BooleanProperty compareProperty(){
        return compare;
    }

    public IntegerProperty impressionsNumProperty(){
        return impressionsNum;
    }

    public IntegerProperty uniqueNumProperty(){
        return uniqueNum;
    }

    public IntegerProperty clicksNumProperty(){
        return clicksNum;
    }

    public IntegerProperty conversionsNumProperty(){
        return conversionsNum;
    }

    public IntegerProperty bounceNumProperty(){
        return bounceNum;
    }

    public DoubleProperty ctrNumProperty(){
        return ctrNum;
    }

    public DoubleProperty cpcNumProperty(){
        return cpcNum;
    }

    public DoubleProperty cpmNumProperty(){
        return cpmNum;
    }

    public DoubleProperty totalNumProperty(){
        return totalNum;
    }

    public DoubleProperty cpaNumProperty(){
        return cpaNum;
    }

    public DoubleProperty bounceRateNumProperty(){
        return bounceRateNum;
    }

    public StringProperty graphNumProperty() {
        return graph;
    }

    public StringProperty graph2NumProperty() {
        return graph2;
    }

    public SimpleStringProperty timeValProperty(){ return timeVal; }

    public SimpleStringProperty pageValProperty(){ return pageVal; }

    public SimpleStringProperty buttonValProperty(){ return buttonVal; }

    public String getMaxDate(){ return maxDate; }

    public Integer getMaxPage(){ return maxPage; }

    public Integer getMaxTime(){ return maxTime; }

    public ArrayList<Double> getClickCost(){ return clickCost; }
}
