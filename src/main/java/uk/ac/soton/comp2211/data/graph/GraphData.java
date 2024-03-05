package uk.ac.soton.comp2211.data.graph;

import javafx.beans.property.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

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

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:data/campaign.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> getData(String startMonth, String startDay, String endMonth, String endDay, String sql){
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Integer> impressions = new ArrayList<>();

        int month = Integer.parseInt(startMonth);
        int day = Integer.parseInt(startDay);
        int count = 0;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {

                if (!male.get() && Objects.equals(rs.getString("gender"), "Male")) {
                    continue;
                }

                if (!female.get() && Objects.equals(rs.getString("gender"), "Female")) {
                    continue;
                }

                if (!low.get() && Objects.equals(rs.getString("income"), "Low")) {
                    continue;
                }

                if (!medium.get() && Objects.equals(rs.getString("income"), "Medium")) {
                    continue;
                }

                if (!high.get() && Objects.equals(rs.getString("income"), "High")) {
                    continue;
                }

                if (!under25.get() && Objects.equals(rs.getString("age"), "<25")) {
                    continue;
                }

                if (!twenties.get() && Objects.equals(rs.getString("age"), "25-34")) {
                    continue;
                }

                if (!thirties.get() && Objects.equals(rs.getString("age"), "35-44")) {
                    continue;
                }

                if (!forties.get() && Objects.equals(rs.getString("age"), "45-54")) {
                    continue;
                }

                if (!above54.get() && Objects.equals(rs.getString("age"), ">54")) {
                    continue;
                }

                if (!socialMedia.get() && Objects.equals(rs.getString("context"), "Social Media")) {
                    continue;
                }

                if (!shopping.get() && Objects.equals(rs.getString("context"), "Shopping")) {
                    continue;
                }

                if (!travel.get() && Objects.equals(rs.getString("context"), "Travel")) {
                    continue;
                }

                if (!news.get() && Objects.equals(rs.getString("context"), "News")) {
                    continue;
                }

                if (!blog.get() && Objects.equals(rs.getString("context"), "Blog")) {
                    continue;
                }

                if (!hobbies.get() && Objects.equals(rs.getString("context"), "Hobbies")) {
                    continue;
                }

                String formattedDay = (day < 10 ? "0" : "") + day;
                if (rs.getString("date").contains("2015-0" + month + "-" + formattedDay)) {
                    count += 1;
                } else {
                    if (count > 1) {
                        dates.add("2015-0" + month + "-" + formattedDay);
                        impressions.add(count);
                        count = 1;
                        if (day != 31) {
                            day += 1;
                        } else {
                            month = 2;
                            day = 1;
                        }
                    }
                    if (rs.getString("date").contains("2015-" + endMonth + "-" + endDay)) {
                        return new Pair<>(dates, impressions);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        dates.add("2015-0" + month + "-" + day);
        impressions.add(count);

        return new Pair<>(dates, impressions);
    }

    public ArrayList<Double> costData(String startMonth, String startDay, String endMonth, String endDay, String sql){
        ArrayList<Double> costList = new ArrayList<>();

        int month = Integer.parseInt(startMonth);
        int day = Integer.parseInt(startDay);
        double cost = 0;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {

                if (!male.get() && Objects.equals(rs.getString("gender"), "Male")) {
                    continue;
                }

                if (!female.get() && Objects.equals(rs.getString("gender"), "Female")) {
                    continue;
                }

                if (!low.get() && Objects.equals(rs.getString("income"), "Low")) {
                    continue;
                }

                if (!medium.get() && Objects.equals(rs.getString("income"), "Medium")) {
                    continue;
                }

                if (!high.get() && Objects.equals(rs.getString("income"), "High")) {
                    continue;
                }

                if (!under25.get() && Objects.equals(rs.getString("age"), "<25")) {
                    continue;
                }

                if (!twenties.get() && Objects.equals(rs.getString("age"), "25-34")) {
                    continue;
                }

                if (!thirties.get() && Objects.equals(rs.getString("age"), "35-44")) {
                    continue;
                }

                if (!forties.get() && Objects.equals(rs.getString("age"), "45-54")) {
                    continue;
                }

                if (!above54.get() && Objects.equals(rs.getString("age"), ">54")) {
                    continue;
                }

                if (!socialMedia.get() && Objects.equals(rs.getString("context"), "Social Media")) {
                    continue;
                }

                if (!shopping.get() && Objects.equals(rs.getString("context"), "Shopping")) {
                    continue;
                }

                if (!travel.get() && Objects.equals(rs.getString("context"), "Travel")) {
                    continue;
                }

                if (!news.get() && Objects.equals(rs.getString("context"), "News")) {
                    continue;
                }

                if (!blog.get() && Objects.equals(rs.getString("context"), "Blog")) {
                    continue;
                }

                if (!hobbies.get() && Objects.equals(rs.getString("context"), "Hobbies")) {
                    continue;
                }

                String formattedDay = (day < 10 ? "0" : "") + day;
                if (rs.getString("date").contains("2015-0" + month + "-" + formattedDay)) {
                    if (sql.contains("click_cost")) {
                        cost += Double.parseDouble(rs.getString("click_cost"));
                    } else {
                        cost += Double.parseDouble(rs.getString("impression_cost"));
                    }
                } else {
                    if (cost > 0) {
                        costList.add(cost);
                        if (sql.contains("click_cost")) {
                            cost = Double.parseDouble(rs.getString("click_cost"));
                        } else {
                            cost = Double.parseDouble(rs.getString("impression_cost"));
                        }
                        if (day != 31) {
                            day += 1;
                        } else {
                            month = 2;
                            day = 1;
                        }
                    }
                    if (rs.getString("date").contains("2015-" + endMonth + "-" + endDay)) {
                        return costList;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        costList.add(cost);
        return costList;
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> filterDate(String startDate, String endDate, String sql) {
        String[] startSplit = startDate.split("-");
        String[] endSplit = endDate.split("-");

        String startMonth = startSplit[1];
        String startDay = startSplit[2];
        String endMonth = endSplit[1];
        String endDay = endSplit[2];

        int newEndDay = Integer.parseInt(endDay);
        String finalEndDay;
        String finalEndMonth;
        if (newEndDay < 9) {
            newEndDay = newEndDay + 1;
            finalEndDay = "0" + newEndDay;
            finalEndMonth = endMonth;
        } else if (newEndDay < 31) {
            newEndDay = newEndDay + 1;
            finalEndDay = "" + newEndDay;
            finalEndMonth = endMonth;
        } else {
            finalEndDay = "01";
            finalEndMonth = "02";
        }

        return getData(startMonth, startDay, finalEndMonth, finalEndDay, sql);
    }

    public ArrayList<Double> costFilterDate(String startDate, String endDate, String sql) {
        String[] startSplit = startDate.split("-");
        String[] endSplit = endDate.split("-");

        String startMonth = startSplit[1];
        String startDay = startSplit[2];
        String endMonth = endSplit[1];
        String endDay = endSplit[2];

        int newEndDay = Integer.parseInt(endDay);
        String finalEndDay;
        String finalEndMonth;
        if (newEndDay < 9) {
            newEndDay = newEndDay + 1;
            finalEndDay = "0" + newEndDay;
            finalEndMonth = endMonth;
        } else if (newEndDay < 31) {
            newEndDay = newEndDay + 1;
            finalEndDay = "" + newEndDay;
            finalEndMonth = endMonth;
        } else {
            finalEndDay = "01";
            finalEndMonth = "02";
        }

        return costData(startMonth, startDay, finalEndMonth, finalEndDay, sql);
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
}
