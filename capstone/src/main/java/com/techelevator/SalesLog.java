package com.techelevator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SalesLog extends GenerateLogTime {

    private Map<String, Integer> salesMap = new HashMap<>();
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private String logPath = "log\\";
    private String salesLogPath;

    public SalesLog(String salesLogPath) {
        this.salesLogPath = salesLogPath;
    }


    public void generateFileName() {
        int num = 1;
        String newReportDate = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        String reportFileName = logPath + "report-" + newReportDate + ".log";
        File reportFile = new File(reportFileName);
        while (reportFile.exists()) {
            reportFileName = logPath + "report-" + newReportDate + " (" + num++ + ").log";
            reportFile = new File(reportFileName);
        }
        if (!reportFile.exists()) {
            try {
                Path originalSales = Paths.get(salesLogPath);
                Path datesSales = Paths.get(reportFileName);
                Files.copy(originalSales, datesSales);

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void readSalesLog() {
        File file = new File(salesLogPath);
        if (file.exists()) {
            try (Scanner scanIn = new Scanner(file)) {
                while (scanIn.hasNextLine()) {
                    String line = scanIn.nextLine();
                    if (line.contains("|")) {
                        String[] items = line.split("\\|");
                        int temp = Integer.parseInt(items[1]);
                        salesMap.put(items[0], temp);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            //Creating the file.
            try {
                Files.createFile(Path.of(salesLogPath));
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        try (PrintWriter writer = new PrintWriter(salesLogPath)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            System.out.println(e);

        }

    }

    public void log(Map<String, Integer> currentSales, Inventory inventory) {
        readSalesLog();
        currentSales.forEach((key, value) -> salesMap.merge(key, value, Integer::sum));
        try (PrintWriter salesOutput = new PrintWriter(new FileOutputStream(salesLogPath, true))) {
            double totalSales = 0;
            for (String key : salesMap.keySet()) {
                salesOutput.print(key + " | " + salesMap.get(key) + "\n");
                for (Product i : inventory.getVendingProducts()) {
                    if (i.getProductName().equals(key)) {
                        totalSales += i.getProductPrice() * salesMap.get(key);
                    }
                }
            }
            salesOutput.print("Total Sales: " + formatter.format(totalSales));
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
}