package uk.ac.soton.comp2211.data.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.util.Pair;
import uk.ac.soton.comp2211.data.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GraphDataTest {
    GraphData gD;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        gD = new GraphData();
    }

    @org.junit.jupiter.api.Test
    void getDates() {
       //1 Month campaign
        ArrayList<String> expectedDates1 = new ArrayList<>();
        expectedDates1.add("2015-01-01");
        expectedDates1.add("2015-01-02");
        expectedDates1.add("2015-01-03");
        expectedDates1.add("2015-01-04");
        expectedDates1.add("2015-01-05");
        expectedDates1.add("2015-01-06");
        expectedDates1.add("2015-01-07");
        expectedDates1.add("2015-01-08");
        expectedDates1.add("2015-01-09");
        expectedDates1.add("2015-01-10");
        expectedDates1.add("2015-01-11");
        expectedDates1.add("2015-01-12");
        expectedDates1.add("2015-01-13");
        expectedDates1.add("2015-01-14");
        ArrayList<String> actualDates1 = gD.getDayDates("2015-01-01", "2015-01-14");
        assertEquals(expectedDates1, actualDates1);

        //2 Month campaign.
        ArrayList<String> expectedDates2 = new ArrayList<>();
        expectedDates2.add("2015-01-01");
        expectedDates2.add("2015-01-02");
        expectedDates2.add("2015-01-03");
        expectedDates2.add("2015-01-04");
        expectedDates2.add("2015-01-05");
        expectedDates2.add("2015-01-06");
        expectedDates2.add("2015-01-07");
        expectedDates2.add("2015-01-08");
        expectedDates2.add("2015-01-09");
        expectedDates2.add("2015-01-10");
        expectedDates2.add("2015-01-11");
        expectedDates2.add("2015-01-12");
        expectedDates2.add("2015-01-13");
        expectedDates2.add("2015-01-14");
        expectedDates2.add("2015-01-15");
        expectedDates2.add("2015-01-16");
        expectedDates2.add("2015-01-17");
        expectedDates2.add("2015-01-18");
        expectedDates2.add("2015-01-19");
        expectedDates2.add("2015-01-20");
        expectedDates2.add("2015-01-21");
        expectedDates2.add("2015-01-22");
        expectedDates2.add("2015-01-23");
        expectedDates2.add("2015-01-24");
        expectedDates2.add("2015-01-25");
        expectedDates2.add("2015-01-26");
        expectedDates2.add("2015-01-27");
        expectedDates2.add("2015-01-28");
        expectedDates2.add("2015-01-29");
        expectedDates2.add("2015-01-30");
        expectedDates2.add("2015-01-31");
        expectedDates2.add("2015-02-01");
        expectedDates2.add("2015-02-02");
        expectedDates2.add("2015-02-03");
        expectedDates2.add("2015-02-04");
        expectedDates2.add("2015-02-05");
        expectedDates2.add("2015-02-06");
        expectedDates2.add("2015-02-07");
        expectedDates2.add("2015-02-08");
        expectedDates2.add("2015-02-09");
        expectedDates2.add("2015-02-10");
        expectedDates2.add("2015-02-11");
        expectedDates2.add("2015-02-12");
        expectedDates2.add("2015-02-13");
        expectedDates2.add("2015-02-14");
        expectedDates2.add("2015-02-15");
        expectedDates2.add("2015-02-16");
        expectedDates2.add("2015-02-17");
        expectedDates2.add("2015-02-18");
        expectedDates2.add("2015-02-19");
        expectedDates2.add("2015-02-20");
        expectedDates2.add("2015-02-21");
        expectedDates2.add("2015-02-22");
        expectedDates2.add("2015-02-23");
        expectedDates2.add("2015-02-24");
        expectedDates2.add("2015-02-25");
        expectedDates2.add("2015-02-26");
        expectedDates2.add("2015-02-27");
        expectedDates2.add("2015-02-28");
        ArrayList<String> actualDates2 = gD.getDayDates("2015-01-01", "2015-02-28");
        assertEquals(expectedDates2, actualDates2);
    }


    @org.junit.jupiter.api.Test
    void filterSQL() {
        // Test case 1: Filtering with all options enabled
        String expectedSQL1 = "WHERE DATETIME('2015-01-01 00:00:01') < date AND DATETIME('2015-01-14 23:59:59') > date";
        String actualSQL1 = gD.filterSQL("2015-01-01", "2015-01-14");
        assertEquals(expectedSQL1, actualSQL1);

        // Test case 2: Filtering with only gender options disabled
        gD.male.set(false);
        gD.female.set(false);
        String expectedSQL2 = "WHERE DATETIME('2015-01-01 00:00:01') < date AND DATETIME('2015-01-14 23:59:59') > date AND gender != 'Male' AND gender != 'Female'";
        String actualSQL2 = gD.filterSQL("2015-01-01", "2015-01-14");
        assertEquals(expectedSQL2, actualSQL2);

        // Test case 3: Filtering with only income options disabled
        gD.male.set(true);
        gD.female.set(true);
        gD.low.set(false);

        String expectedSQL3 = "WHERE DATETIME('2015-01-01 00:00:01') < date AND DATETIME('2015-01-14 23:59:59') > date AND income != 'Low'";
        String actualSQL3 = gD.filterSQL("2015-01-01", "2015-01-14");
        assertEquals(expectedSQL3, actualSQL3);

        //Test case 4: Filtering with age ranges
        gD.low.set(true);
        gD.thirties.set(false);
        gD.under25.set(false);
        String expectedSQL4 = "WHERE DATETIME('2015-01-01 00:00:01') < date AND DATETIME('2015-01-14 23:59:59') > date AND age != '<25' AND age != '35-44'";
        String actualSQL4 = gD.filterSQL("2015-01-01", "2015-01-14");
        assertEquals(expectedSQL4, actualSQL4);

        gD.thirties.set(true);
        gD.under25.set(true);

        //Test case 5: Filtering based on context.
        gD.travel.set(false);
        gD.shopping.set(false);
        String expectedSQL5 = "WHERE DATETIME('2015-01-01 00:00:01') < date AND DATETIME('2015-01-14 23:59:59') > date AND context != 'Shopping' AND context != 'Travel'";
        String actualSQL5 = gD.filterSQL("2015-01-01", "2015-01-14");
        assertEquals(expectedSQL5, actualSQL5);

        gD.travel.set(true);
        gD.shopping.set(true);


        //Test case 6: Filtering from all categories.
        gD.female.set(false);
        gD.low.set(false);
        gD.medium.set(false);
        gD.above54.set(false);
        gD.blog.set(false);
        String expectedSQL6 = "WHERE DATETIME('2015-01-01 00:00:01') < date AND DATETIME('2015-01-14 23:59:59') > date AND gender != 'Female' AND income != 'Low' AND income != 'Medium' AND age != '>54' AND context != 'Blog'";
        String actualSQL6 = gD.filterSQL("2015-01-01", "2015-01-14");
        assertEquals(expectedSQL6, actualSQL6);


    }

    @org.junit.jupiter.api.Test
    void calculateMetricsTwoWeeks() throws SQLException {
        gD.calculateMetrics("2015-01-01", "2015-01-14");
        IntegerProperty impressionNum = gD.impressionsNum;
        IntegerProperty uniqueNum = gD.uniqueNum;
        IntegerProperty clicksNum = gD.clicksNum;
        IntegerProperty conversionsNum = gD.conversionsNum;
        IntegerProperty bounceNum = gD.bounceNum;
        DoubleProperty ctrNum = gD.ctrNum;
        DoubleProperty cpcNum = gD.cpcNum;
        DoubleProperty totalNum = gD.totalNum;
        DoubleProperty cpaNum = gD.cpaNum;
        DoubleProperty bounceRateNum = gD.bounceRateNum;

        assertAll("Verify metrics",
                () -> assertEquals(486104, impressionNum.get()),
                () -> assertEquals(23806, uniqueNum.get()),
                () -> assertEquals(23923, clicksNum.get()),
                () -> assertEquals(2026, conversionsNum.get()),
                () -> assertEquals(6482, bounceNum.get()),
                () -> assertEquals(0.04921, ctrNum.get()),
                () -> assertEquals(4.92, cpcNum.get()),
                () -> assertEquals(118097.92, totalNum.get()),
                () -> assertEquals(58.29, cpaNum.get()),
                () -> assertEquals(0.27095, bounceRateNum.get())
        );

    }
}