package uk.ac.soton.comp2211.data.graph;

import javafx.beans.property.*;
import javafx.util.Pair;
import uk.ac.soton.comp2211.data.Database;

import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
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
        return Database.getConnection("campaign");
    }

    private String sqlConcat(String delimiter, ArrayList<String> clauses) {
        String joinedClauses = String.join(" " + delimiter + " ", clauses);

        return "(" + joinedClauses + ")";
    }

    public ResultSet queryDatabase(String sql, String[] additionalWheres, String postWhere) {
        var whereClauses = new ArrayList<String>();

        if (!(male.get() && female.get())) {
            var genderWheres = new ArrayList<String>();

            if (male.get()) {
                genderWheres.add("impression_log.gender = 'Male'");
            }
            if (female.get()) {
                genderWheres.add("impression_log.gender = 'Female'");
            }

            whereClauses.add(sqlConcat("OR", genderWheres));
        }

        if (!(low.get() && medium.get() && high.get())) {
            var incomeWheres = new ArrayList<String>();

            if (low.get()) {
                incomeWheres.add("impression_log.income = 'Low'");
            }
            if (medium.get()) {
                incomeWheres.add("impression_log.income = 'Medium'");
            }
            if (high.get()) {
                incomeWheres.add("impression_log.income = 'High'");
            }

            whereClauses.add(sqlConcat("OR", incomeWheres));
        }

        if (!(under25.get() && twenties.get() && thirties.get() && forties.get() && above54.get())) {
            var ageWheres = new ArrayList<String>();

            if (under25.get()) {
                ageWheres.add("impression_log.age = '<25'");
            }
            if (twenties.get()) {
                ageWheres.add("impression_log.age = '25-34'");
            }
            if (thirties.get()) {
                ageWheres.add("impression_log.age = '35-44'");
            }
            if (forties.get()) {
                ageWheres.add("impression_log.age = '45-54'");
            }
            if (above54.get()) {
                ageWheres.add("impression_log.age = '>54'");
            }

            whereClauses.add(sqlConcat("OR", ageWheres));
        }

        if (!(socialMedia.get() && shopping.get() && travel.get() && news.get() && blog.get() && hobbies.get())) {
            var contextWheres = new ArrayList<String>();

            if (socialMedia.get()) {
                contextWheres.add("impression_log.context = 'Social Media'");
            }
            if (shopping.get()) {
                contextWheres.add("impression_log.context = 'Shopping'");
            }
            if (travel.get()) {
                contextWheres.add("impression_log.context = 'Travel'");
            }
            if (news.get()) {
                contextWheres.add("impression_log.context = 'News'");
            }
            if (blog.get()) {
                contextWheres.add("impression_log.context = 'Blog'");
            }
            if (hobbies.get()) {
                contextWheres.add("impression_log.context = 'Hobbies'");
            }

            whereClauses.add(sqlConcat("OR", contextWheres));
        }

        whereClauses.addAll(List.of(additionalWheres));

        String finalSql = sql;

        if (!whereClauses.isEmpty()) {
            String whereClause = sqlConcat("AND", whereClauses);
            finalSql = finalSql.concat(" WHERE " + whereClause);
        }

        finalSql = finalSql.concat(" " + postWhere);

        System.out.println(finalSql);

        Connection conn = null;
        Statement stat = null;
        ResultSet rs;

        try {
            conn = this.connect();
            stat = conn.createStatement();
            rs = stat.executeQuery(finalSql);
            System.out.println("GOT HERE");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rs;
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> getData(String startMonth, String startDay, String endMonth, String endDay, String sql, String[] additionalWheres, String postWhere){
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Integer> impressions = new ArrayList<>();

        int month = Integer.parseInt(startMonth);
        int day = Integer.parseInt(startDay);
        int count = 0;

        try (ResultSet rs    = queryDatabase(sql, additionalWheres, postWhere)){
            // loop through the result set
            while (rs.next()) {
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

    public ArrayList<Double> costData(String startMonth, String startDay, String endMonth, String endDay, String sql, String[] additionalWheres, String postWhere){
        ArrayList<Double> costList = new ArrayList<>();

        int month = Integer.parseInt(startMonth);
        int day = Integer.parseInt(startDay);
        double cost = 0;

        try (ResultSet rs    = queryDatabase(sql, additionalWheres, postWhere)){

            // loop through the result set
            while (rs.next()) {
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

    public Pair<ArrayList<String>, ArrayList<Integer>> filterDate(String startDate, String endDate, String sql, String[] additionalWheres, String postWhere) {
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

        return getData(startMonth, startDay, finalEndMonth, finalEndDay, sql, additionalWheres, postWhere);
    }

    public ArrayList<Double> costFilterDate(String startDate, String endDate, String sql, String[] additionalWheres, String postWhere) {
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

        return costData(startMonth, startDay, finalEndMonth, finalEndDay, sql, additionalWheres, postWhere);
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
