package uk.ac.soton.comp2211.data.Export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class ReportExporter {

    public static File getReportCSV(String outputDirectory, List<Number> numbers) {

        String fileName = "report.csv";


        String filePath = outputDirectory + File.separator + fileName;

        File outputFile = new File(filePath);

        try {

            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));


            writer.println("Num of impressions, Num of uniques, Num of clicks, Num of bounces, Num of conversions, CTR, CPA(£), CPC(£), CPM(£), Bounce Rate, Total Cost(£)");


            String numbersString = numbers.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));


            writer.println(numbersString);


            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + filePath, e);
        }

        return outputFile;
    }
}
