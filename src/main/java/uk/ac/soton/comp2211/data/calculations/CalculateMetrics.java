package uk.ac.soton.comp2211.data.calculations;
import java.sql.*;

public class CalculateMetrics {
public Connection connection;

public Statement statement;

private Integer numberOfImpressions;

private Integer numberOfClicks;

private Integer numberOfUniques;

private Integer numberOfBouncesVisit;

private Integer numberOfBouncesTime;

private Integer totalConversions;

private Float totalCost;

private Integer numberOfPages;

private Integer timeDifference;

public Float CTR;
public Float CPA;
public Float CPC;
public Float CPM;

    public CalculateMetrics(Connection c, int pageBounce, int timeDiff)  {
        try {
            this.connection = c;
            this.statement = c.createStatement();
            this.numberOfPages = pageBounce;
            this.timeDifference = timeDiff;
            numberOfImpressions();
            numberOfClicks();
            numberOfUniques();
            visitBounce(numberOfPages);
            timeBounce(timeDifference);
            numberOfConversions();
            totalCost();
            calculateCTR();
            calculateCPA();
            calculateCPC();
            calculateCPM();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }


    public CalculateMetrics(Connection c)  {
        try {
            this.connection = c;
            this.statement = c.createStatement();
            this.numberOfPages = 1;
            this.timeDifference = 1;
            numberOfImpressions();
            numberOfClicks();
            numberOfUniques();
            visitBounce(numberOfPages);
            timeBounce(timeDifference);
            numberOfConversions();
            totalCost();
            calculateCTR();
            calculateCPA();
            calculateCPC();
            calculateCPM();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void numberOfImpressions() {

        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS imp_total FROM impression_log;");
            this.numberOfImpressions = resultSet.getInt("imp_total") + 1;
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void numberOfClicks() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS click_total FROM click_log;");
            this.numberOfClicks = resultSet.getInt("click_total") + 1;

            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }




    public void numberOfUniques() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (DISTINCT ID) AS unique_count FROM click_log;");
            this.numberOfUniques = resultSet.getInt("unique_count");

            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }



    public void visitBounce(int minVisits)  {

        String query = "SELECT COUNT(pages_viewed) AS page_bounce FROM server_log WHERE pages_viewed <= " + minVisits;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            this.numberOfBouncesVisit = resultSet.getInt("page_bounce");

            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public void timeBounce(int time_diff)  {

        String query = "SELECT COUNT(*) AS time_bounce FROM server_log WHERE (strftime('%s', exit_date) - strftime('%s', entry_date)) / 60 <= " + time_diff;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            this.numberOfBouncesTime = resultSet.getInt("time_bounce");


            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public  void numberOfConversions() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS conversion_count FROM server_log WHERE conversion = 'Yes';");
            this.totalConversions = resultSet.getInt("conversion_count");
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void totalCost() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT SUM(click_cost) AS total_cost FROM click_log");
            this.totalCost = resultSet.getFloat("total_cost");

            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void calculateCTR() {
        CTR = (float)getNumberOfClicks() / getNumberOfImpressions();
    }

    public void calculateCPA() {
        try {
            ResultSet resultSet = statement.executeQuery("""
                    SELECT SUM(t1.click_cost) AS click_cost FROM click_log t1\s
                    JOIN (
                        SELECT * FROM server_log WHERE conversion == 'Yes'
                    ) t2
                    ON t1.date == t2.entry_date AND t1.ID == t2.ID""");
            float click_cost = resultSet.getFloat("click_cost");
            CPA = click_cost / (float) getNumberOfImpressions();
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void calculateCPC() {
        try {
            ResultSet resultSet = statement.executeQuery(" SELECT SUM(click_cost) AS click_cost_total FROM click_log;");
            float click_cost_total = resultSet.getFloat("click_cost_total");
            CPC = click_cost_total / (float) getNumberOfClicks();
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void calculateCPM() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT SUM(impression_cost) AS impression_cost_total FROM impression_log;");
            float impression_cost_total = resultSet.getFloat("impression_cost_total");
            CPM = impression_cost_total / (float) getNumberOfImpressions() * 1000;
            resultSet.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int getNumberOfImpressions(){
        return this.numberOfImpressions;
    }

    public int getNumberOfClicks(){
        return this.numberOfClicks;
    }

    public int getNumberOfUniques(){
        return this.numberOfUniques;
    }

    public int getNumberOfBouncesVisit(){
        return numberOfBouncesVisit;
    }

    public int getNumberOfBouncesTime(){
        return this.numberOfBouncesTime;
    }
    public int getNumberOfConversions(){
        return this.totalConversions;
    }

    public float getTotalCost(){
        return this.totalCost;
    }

    public Float getCPA(){
        return this.CPA;
    }

    public Float getCPC(){
        return this.CPC;
    }

    public Float getCTR(){
        return this.CTR;
    }

    public Float getCPM(){
        return this.CPM;
    }


}








