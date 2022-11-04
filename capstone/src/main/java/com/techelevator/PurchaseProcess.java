package com.techelevator;

import java.text.NumberFormat;
import java.util.*;

public class PurchaseProcess {

    //<editor-fold desc="*DATA MEMBERS*">
    private final SecurityLog securityLog = new SecurityLog();
    private final String salesLogPath;
    private final SalesLog salesLog;
    private double currentBalance;
    private final double endingBalance = 0;


    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    Map<String, Integer> productSales = new HashMap<>();

    //</editor-fold>
    public Map<String, Integer> getProductSales() {
        return productSales;
    }

    public PurchaseProcess(String salesLogPath) {
        this.salesLogPath = salesLogPath;
        salesLog = new SalesLog(salesLogPath);

    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentMoney) {
        this.currentBalance = currentMoney;
    }

    public double feedMoney() {
        Scanner moneyIn = new Scanner(System.in);
        String pleaseEnter = "Please Enter Whole Dollar Amount: \r\n" +
                "Example: (1), (2), (5), (10) or (0) to exit.";
        System.out.println(pleaseEnter);

        try {
            while (moneyIn.hasNextDouble()) {
                double moneyEntered = moneyIn.nextDouble();
                if (moneyEntered == 0){
                    break;
                }
                if (moneyEntered % 1 == 0) {
                    double ONE_DOLLAR_BILL = 1.00;
                    double TWO_DOLLAR_BILL = 2.00;
                    double FIVE_DOLLAR_BILL = 5.00;
                    double TEN_DOLLAR_BILL = 10.00;
                    if (moneyEntered == ONE_DOLLAR_BILL || moneyEntered == TWO_DOLLAR_BILL ||
                            moneyEntered == FIVE_DOLLAR_BILL || moneyEntered == TEN_DOLLAR_BILL) {
                        securityLog.log("FEED MONEY", moneyEntered, currentBalance += moneyEntered);


                    } else {
                        System.err.println("Invalid currency" + "\n");
                        System.err.println(pleaseEnter);
                    }

                }
                System.out.println("Current balance: " + formatter.format(currentBalance));
            }


        } catch (InputMismatchException e) {
            System.err.println(e);
        }
        return currentBalance;
    }

    public void purchaseItem(Inventory inventory) {
        boolean isMatch = false;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter Product Key! ");
        String userInput = scanner.nextLine();

        for (Product product : inventory.getVendingProducts()) {
            if (userInput.equalsIgnoreCase(product.getProductId())) {
                isMatch = true;
                if (product.getProductCount() > 0) {
                    if (currentBalance >= product.getProductPrice()) {
                        securityLog.log(product.getProductName() + " $" + product.getProductPrice(),
                                currentBalance, currentBalance -= product.getProductPrice());
                        System.out.println("Purchased: " + product.getProductName() + " " + formatter.format(product.getProductPrice()));
                        product.setProductCount(product.getProductCount() - 1);
                        SoundEffects soundEffects = new SoundEffects(product);
                        System.out.println("Balance: " + formatter.format(currentBalance));
                        System.out.println(soundEffects);

                        int MAX_COUNT = 5;
                        productSales.put(product.getProductName(), (MAX_COUNT - product.getProductCount()));

                    } else {
                        System.out.println("Insufficient Funds! \nYour current funds are: "
                                + formatter.format(currentBalance));
                    }
                } else {
                    inventory.displayItemCount(product.getProductCount());
                    if (product.getProductCount() == 0) {
                        System.out.println("SOLD OUT! Please select something else!");
                    }
                }
            }
        }
        if (!isMatch) {
            System.err.println("Invalid Product Key!");
        }
    }

    public String completeTransaction(Inventory inventory) {
        salesLog.log(productSales, inventory);
        double balanceBeforeTransaction = currentBalance;

        System.out.println("Dispensing Change: " + formatter.format(currentBalance));
        int PERCENT = 100;
        String greedyCoinString = greedyCoin((int) Math.round(currentBalance * PERCENT));
        securityLog.log("GIVE CHANGE", balanceBeforeTransaction, currentBalance);

        System.out.println(greedyCoinString);
        System.out.println("Vending Machine Balance: " + formatter.format(currentBalance) +
                "\r\nThanks for using the Vendo-Matic 800!");

        return "Dispensing Change: " + formatter.format(balanceBeforeTransaction) + "\r\n" + greedyCoin((int) Math.round(balanceBeforeTransaction * PERCENT)) + "\r\n" +
                "Vending Machine Balance: " + formatter.format(currentBalance) +
                "\r\nThanks for using the Vendo-Matic 800!";
    }

    public String greedyCoin(int changeDue) {
        int NICKEL = 5, DIME = 10, QUARTER = 25;
        int[] coins = {QUARTER, DIME, NICKEL};
        int coinIndex = 0;
        int totalQuarters = 0, totalDimes = 0, totalNickels = 0;

        while (changeDue > 0) {

            if (coins[coinIndex] > changeDue) {
                coinIndex++;
            } else {
                totalQuarters += coinIndex == 0 ? 1 : 0;
                totalDimes += coinIndex == 1 ? 1 : 0;
                totalNickels += coinIndex == 2 ? 1 : 0;
                changeDue = changeDue - coins[coinIndex];
            }
            if (changeDue == 0) {
                break;
            }
        }
        setCurrentBalance(changeDue);
        return "Quarters: " + totalQuarters + "\r\n" + "Dimes: " + totalDimes + "\r\n" + "Nickels: " + totalNickels + "\r\n";
    }
}