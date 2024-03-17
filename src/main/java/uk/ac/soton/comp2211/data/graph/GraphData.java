package uk.ac.soton.comp2211.data.graph;

import javafx.beans.property.*;
import javafx.util.Pair;
import uk.ac.soton.comp2211.data.Database;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

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

    SimpleStringProperty timeVal = new SimpleStringProperty("1");
    SimpleStringProperty pageVal = new SimpleStringProperty("1");

    ArrayList<Integer> impressions;
    ArrayList<Integer> uniques;
    ArrayList<Integer> clicks;
    ArrayList<Integer> pages;
    ArrayList<Integer> times;
    ArrayList<Integer> conversions;
    ArrayList<Double> clickCost;
    ArrayList<Double> impressionCost;
    ArrayList<Double> totals;
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

    public ArrayList<String> getDates(String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        ArrayList<String> dates = new ArrayList<>();
        while (!start.isAfter(end)) {
            dates.add(start.toString());
            start = start.plusDays(1);
        }
        return dates;
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
        String date = sql.contains("DATE(date)") ? "DATE(date)" : "DATE(impressions.date)";
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
        String date = sql.contains("DATE(date)") ? "DATE(date)" : "DATE(impressions.date)";
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
        String impressionSQL = "SELECT COUNT(*), DATE(date) FROM impression_log " + sqlFilters + " GROUP BY DATE(date)";
        String uniqueSQL = "WITH clicks AS (SELECT DISTINCT id FROM click_log), impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), DATE(impressions.date) FROM clicks INNER JOIN impressions ON clicks.id = impressions.id GROUP BY DATE(impressions.date)";
        String clickSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), DATE(impressions.date) FROM click_log INNER JOIN impressions ON click_log.id = impressions.id GROUP BY DATE(impressions.date)";
        String pageSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), DATE(impressions.date) FROM server_log INNER JOIN impressions ON server_log.id = impressions.id WHERE server_log.pages_viewed <= " + pageVal.get() + " GROUP BY DATE(impressions.date)";
        String timeSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), DATE(impressions.date) FROM server_log INNER JOIN impressions ON server_log.id = impressions.id WHERE time(server_log.exit_date) < time(server_log.entry_date, '+" + timeVal.get() + " minutes') GROUP BY DATE(impressions.date)";
        String conversionSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT COUNT(*), DATE(impressions.date) FROM server_log INNER JOIN impressions ON server_log.id = impressions.id WHERE server_log.conversion = 'Yes' GROUP BY DATE(impressions.date)";
        String clickCostSQL = "WITH impressions AS (SELECT * FROM impression_log " + sqlFilters + " GROUP BY id) SELECT SUM(click_cost), DATE(impressions.date) FROM click_log INNER JOIN impressions ON click_log.id = impressions.id GROUP BY DATE(impressions.date)";
        String impressionCostSQL = "SELECT SUM(impression_cost), DATE(date) FROM impression_log " + sqlFilters + " GROUP BY DATE(date)";

        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {
            DecimalFormat value = new DecimalFormat("#.00000");
            DecimalFormat cost = new DecimalFormat("#.00");
            dates = getDates(startDate, endDate);

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
                totals.add(clickCost.get(i) + impressionCost.get(i));
            }

            impressionsNum.set(impressions.stream().mapToInt(a -> a).sum());
            uniqueNum.set(uniques.stream().mapToInt(a -> a).sum());
            clicksNum.set(clicks.stream().mapToInt(a -> a).sum());
            if (page.get()) {
                bounceNum.set(pages.stream().mapToInt(a -> a).sum());
            } else {
                bounceNum.set(times.stream().mapToInt(a -> a).sum());
            }
            conversionsNum.set(getIntMetric(stmt, conversionSQL).stream().mapToInt(a -> a).sum());
            double clickCostNum = clickCost.stream().mapToDouble(a -> a).sum();
            double impressionCostNum = impressionCost.stream().mapToDouble(a -> a).sum();
            totalNum.set(Double.parseDouble(cost.format(clickCostNum + impressionCostNum)));
            ctrNum.set(Double.parseDouble(value.format((double) clicksNum.get() / impressionsNum.get())));
            cpaNum.set(Double.parseDouble(cost.format(totalNum.get() / conversionsNum.get())));
            cpcNum.set(Double.parseDouble(cost.format(clickCostNum / clicksNum.get())));
            cpmNum.set(Double.parseDouble(cost.format(impressionCostNum / ((double) impressionsNum.get() / 1000))));
            bounceRateNum.set(Double.parseDouble(value.format((double) bounceNum.get() / clicksNum.get())));
        }
    }

    public ArrayList<String> getDates() {
        return dates;
    }

    public Pair<ArrayList<Integer>, ArrayList<Double>> getData() {
        ArrayList<Integer> integerData = null;
        ArrayList<Double> doubleData = new ArrayList<>();

        if (graphNumProperty().get().equals("Impressions")) {
            integerData = impressions;
        } else if (graphNumProperty().get().equals("Clicks")) {
            integerData = clicks;
        } else if (graphNumProperty().get().equals("Uniques")) {
            integerData = uniques;
        } else if (graphNumProperty().get().equals("Bounces")) {
            if (page.get()) {
                integerData = pages;
            } else {
                integerData = times;
            }
        } else if (graphNumProperty().get().equals("Conversions")) {
            integerData = conversions;
        } else if (graphNumProperty().get().equals("Total cost")) {
            for (int i = 0; i < dates.size(); i++) {
                doubleData.add(totals.get(i));
            }
        } else if (graphNumProperty().get().equals("CTR")) {
            for (int i = 0; i < dates.size(); i++) {
                doubleData.add((double) clicks.get(i) / impressions.get(i));
            }
        } else if (graphNumProperty().get().equals("CPA")) {
            for (int i = 0; i < dates.size(); i++) {
                if (conversions.get(i) == 0) {
                    doubleData.add((double) 0);
                } else {
                    doubleData.add(totals.get(i) / conversions.get(i));
                }
            }
        } else if (graphNumProperty().get().equals("CPC")) {
            for (int i = 0; i < dates.size(); i++) {
                doubleData.add(clickCost.get(i) / clicks.get(i));
            }
        } else if (graphNumProperty().get().equals("CPM")) {
            for (int i = 0; i < dates.size(); i++) {
                doubleData.add(impressionCost.get(i) / ((double) impressions.get(i) / 1000));
            }
        } else if (graphNumProperty().get().equals("Bounce rate")) {
            for (int i = 0; i < dates.size(); i++) {
                if (page.get()) {
                    doubleData.add((double) pages.get(i) / clicks.get(i));
                } else {
                    doubleData.add((double) times.get(i) / clicks.get(i));
                }
            }
        }

        return new Pair<>(integerData, doubleData);
    }

    public void maxValues() {
        String maxDateSQL = "SELECT MAX(DATE(date)) FROM impression_log";
        String maxPageSQL = "SELECT MAX(pages_viewed) FROM server_log";
        String maxTimeSQL = "SELECT TIME(MAX(julianday(datetime(exit_date)) - julianday(datetime(entry_date)))) FROM server_log";

        try (Connection conn = this.connect(); Statement stmt = conn.createStatement()) {

            ResultSet dateRS = stmt.executeQuery(maxDateSQL);
            maxDate = dateRS.getString("MAX(DATE(date))");

            ResultSet pageRS = stmt.executeQuery(maxPageSQL);
            maxPage = pageRS.getInt("MAX(pages_viewed)");

            ResultSet timeRS = stmt.executeQuery(maxTimeSQL);
            maxTime = Integer.parseInt(timeRS.getString("TIME(MAX(julianday(datetime(exit_date)) - julianday(datetime(entry_date))))").split(":")[1]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public SimpleStringProperty timeValProperty(){ return timeVal; }

    public SimpleStringProperty pageValProperty(){ return pageVal; }

    public String getMaxDate(){ return maxDate; }

    public Integer getMaxPage(){ return maxPage; }

    public Integer getMaxTime(){ return maxTime; }
}
