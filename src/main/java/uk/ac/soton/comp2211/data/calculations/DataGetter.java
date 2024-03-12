package uk.ac.soton.comp2211.data.calculations;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.data.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataGetter {
    private static final Logger logger = LogManager.getLogger(DataGetter.class);

    String filterDataTable = "impression_log";
    String genderField = "gender";
    String incomeField = "income";
    String ageField = "age";
    String contextField = "context";

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
    BooleanProperty bounceOnTime = new SimpleBooleanProperty(true);

    private String dataStartDate;
    private String dataEndDate;

    StringProperty startDate = new SimpleStringProperty();
    StringProperty endDate = new SimpleStringProperty();

    public DataGetter() {
        ResultSet rs = queryDatabase("SELECT STRFTIME('%Y-%m-%d', MIN(impression_log.date)) AS first_date, STRFTIME('%Y-%m-%d', MAX(impression_log.date)) AS last_date FROM impression_log", null, "ORDER BY STRFTIME('%Y-%m-%d', impression_log.date)");

        try {
            if (rs.next()) {
                dataStartDate = rs.getString("first_date");
                dataEndDate = rs.getString("last_date");

                startDate.set(dataStartDate);
                endDate.set(dataEndDate);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String sqlConcat(String delimiter, ArrayList<String> clauses) {
        String joinedClauses = clauses.stream().filter(Objects::nonNull).collect(Collectors.joining(" " + delimiter + " "));

        return "(" + joinedClauses + ")";
    }

    private String concatPropertyFilter(String field, String value) {
        return (filterDataTable + "." + field + " = '" + value + "'");
    }

    private String constructQuery(String selectSql, String whereSql, String postWhereSql) {
        String querySql = selectSql;
        String filterSql = concatFilters();

        if (!(filterSql == null) || !(whereSql == null) || ((dataStartDate != null && (!startDate.get().equals(dataStartDate)))) || (dataEndDate != null && (!endDate.get().equals(dataEndDate)))) {
            var clauses = new ArrayList<String>();
            clauses.add(filterSql);
            clauses.add(whereSql);

            if (!startDate.get().equals(dataStartDate)) {
                String startClause = "STRFTIME('%Y-%m-%d', impression_log.date) >= " + startDate.get();
                clauses.add(startClause);
            }

            if (!endDate.get().equals(dataEndDate)) {
                String endClause = "STRFTIME('%Y-%m-%d', impression_log.date) <= " + endDate.get();
                clauses.add(endClause);
            }

            querySql = querySql + " WHERE ";

            querySql = querySql + sqlConcat("AND", clauses);
        }

        if (!(postWhereSql == null)) {
            querySql = querySql + " " + postWhereSql;
        }

        return querySql;
    }

    private ResultSet queryDatabase(String querySql) {
        Connection conn;
        Statement stat;
        ResultSet rs;

        logger.info(querySql);

        try {
            conn = Database.getConnection("campaign");
            stat = conn.createStatement();
            rs = stat.executeQuery(querySql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rs;
    }

    private ResultSet queryDatabase(String selectSql, String whereSql, String postWhereSql) {
        String querySql = constructQuery(selectSql, whereSql, postWhereSql);

        return queryDatabase(querySql);
    }

    private String concatFilters() {
        var whereClauses = new ArrayList<String>();

        if (!(male.get() && female.get())) {
            var genderWheres = new ArrayList<String>();

            if (male.get()) {
                genderWheres.add(concatPropertyFilter(genderField, "Male"));
            }
            if (female.get()) {
                genderWheres.add(concatPropertyFilter(genderField, "Female"));
            }

            whereClauses.add(sqlConcat("OR", genderWheres));
        }

        if (!(low.get() && medium.get() && high.get())) {
            var incomeWheres = new ArrayList<String>();

            if (low.get()) {
                incomeWheres.add(concatPropertyFilter(incomeField, "Low"));
            }
            if (medium.get()) {
                incomeWheres.add(concatPropertyFilter(incomeField, "Medium"));
            }
            if (high.get()) {
                incomeWheres.add(concatPropertyFilter(incomeField, "High"));
            }

            whereClauses.add(sqlConcat("OR", incomeWheres));
        }

        if (!(under25.get() && twenties.get() && thirties.get() && forties.get() && above54.get())) {
            var ageWheres = new ArrayList<String>();

            if (under25.get()) {
                ageWheres.add(concatPropertyFilter(ageField, "<25"));
            }
            if (twenties.get()) {
                ageWheres.add(concatPropertyFilter(ageField, "25-34"));
            }
            if (thirties.get()) {
                ageWheres.add(concatPropertyFilter(ageField, "35-44"));
            }
            if (forties.get()) {
                ageWheres.add(concatPropertyFilter(ageField, "45-54"));
            }
            if (above54.get()) {
                ageWheres.add(concatPropertyFilter(ageField, ">54"));
            }

            whereClauses.add(sqlConcat("OR", ageWheres));
        }

        if (!(socialMedia.get() && shopping.get() && travel.get() && news.get() && blog.get() && hobbies.get())) {
            var contextWheres = new ArrayList<String>();

            if (socialMedia.get()) {
                contextWheres.add(concatPropertyFilter(contextField, "Social Media"));
            }
            if (shopping.get()) {
                contextWheres.add(concatPropertyFilter(contextField, "Shopping"));
            }
            if (travel.get()) {
                contextWheres.add(concatPropertyFilter(contextField, "Travel"));
            }
            if (news.get()) {
                contextWheres.add(concatPropertyFilter(contextField, "News"));
            }
            if (blog.get()) {
                contextWheres.add(concatPropertyFilter(contextField, "Blog"));
            }
            if (hobbies.get()) {
                contextWheres.add(concatPropertyFilter(contextField, "Hobbies"));
            }

            whereClauses.add(sqlConcat("OR", contextWheres));
        }

        String concatenated = null;

        if (!whereClauses.isEmpty()) {
            concatenated = sqlConcat("AND", whereClauses);
        }

        return concatenated;
    }

    public Map<String, Integer> getImpressionsByDay() {
        Map<String, Integer> impressionData;

        String selectSql = "SELECT STRFTIME('%Y-%m-%d', impression_log.date) AS unique_date, COUNT(impression_log.impression_cost) AS total_impressions FROM impression_log";

        try (ResultSet rs = queryDatabase(selectSql, null, "GROUP BY STRFTIME('%Y-%m-%d', impression_log.date) ORDER BY impression_log.date")) {
            impressionData = new TreeMap<>();

            while (rs.next()) {
                String date = rs.getString("unique_date");
                Integer totalImpressions = rs.getInt("total_impressions");

                impressionData.put(date, totalImpressions);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return impressionData;
    }

    public Integer getTotalImpressions() {
        String selectSql = "SELECT COUNT(impression_log.log_id) AS total FROM impression_log";

        try (ResultSet rs = queryDatabase(selectSql, null, null)) {
            if (rs.next()) {
                return rs.getInt("total");
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // /Users/mahdi/IdeaProjects/ad-auction-dashboard/data/sample_data/2_week_campaign/impression_log.csv
    public Integer getUniques() {
        String innerSelect = "SELECT impression_log.id FROM impression_log";

        String innerQuery = constructQuery(innerSelect, null, null);
        String querySql = "SELECT COUNT(*) AS uniques FROM (SELECT click_log.log_id FROM click_log WHERE click_log.id IN (" + innerQuery + ") GROUP BY click_log.id)";

        try (ResultSet rs = queryDatabase(querySql)) {
            if (rs.next()) {
                return rs.getInt("uniques");
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getConversions() {
        String innerSelect = "SELECT impression_log.id FROM impression_log";
        String innerQuery = constructQuery(innerSelect, null, null);
        String selectSql = "SELECT COUNT(server_log.id) AS conversions FROM server_log WHERE (server_log.id IN (" + innerQuery + ")) AND server_log.conversion = 'Yes'";

        try (ResultSet rs = queryDatabase(selectSql)) {
            if (rs.next()) {
                return rs.getInt("conversions");
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Float getTotalImpressionCost() {
        String selectSql = "SELECT SUM(impression_log.impression_cost) AS total_cost from impression_log";

        try (ResultSet rs = queryDatabase(selectSql, null, null)) {
            if (rs.next()) {
                return rs.getFloat("total_cost");
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Float getTotalClickCost() {
        String innerSelect = "SELECT impression_log.id FROM impression_log";
        String innerQuery = constructQuery(innerSelect, null, null);

        String selectSql = "SELECT SUM(click_log.click_cost) AS total_cost FROM click_log WHERE click_log.id IN (" + innerQuery + ")";
        try (ResultSet rs = queryDatabase(selectSql)) {
            if (rs.next()) {
                return rs.getFloat("total_cost");
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Float getTotalCost() {
        return (getTotalImpressionCost() + getTotalClickCost());
    }

    public Integer getTotalClicks() {
        String innerSelect = "SELECT impression_log.id FROM impression_log";
        String innerQuery = constructQuery(innerSelect, null, null);

        String selectSql = "SELECT COUNT(click_log.log_id) AS total FROM click_log WHERE click_log.id IN (" + innerQuery + ")";


        try (ResultSet rs = queryDatabase(selectSql)) {
            if (rs.next()) {
                return rs.getInt("total");
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getTotalBounces() {
        return null;
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public StringProperty endDateProperty() {
        return endDate;
    }
}
