package com.techelevator;

import com.techelevator.view.Menu;
import com.techelevator.view.PurchaseMenu;

import java.nio.file.Path;
import java.nio.file.Paths;

public class VendingMachineCLI {

    //<editor-fold desc="*DATA MEMBERS">
    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    private static final String EXIT_MENU_OPTION = "Exit";
    private static final String SALES_REPORT = "Sales Report";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, EXIT_MENU_OPTION, SALES_REPORT};
    Inventory inventory = new Inventory();
    private String salesFilePath = "log\\sales.log";
    SalesLog salesLog = new SalesLog(salesFilePath);
    private Menu menu;
    PurchaseMenu purchaseMenu;
    GenerateTime generateTime = new GenerateTime();

    //</editor-fold>

    public VendingMachineCLI(Menu menu) {
        generateTime.logFolder();
        this.menu = menu;
        inventory.createInventory("vendingmachine.csv");
    }


    public void run() {
        while (true) {
            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

            if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
                // display vending machine items
                inventory.displayAvailableProducts();

            } else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
                // do purchase
                purchaseMenu = new PurchaseMenu(menu, inventory, salesFilePath);
                purchaseMenu.run();

            } else if (choice.equals(EXIT_MENU_OPTION)) {
                break;
            } else if (choice.equals(SALES_REPORT)) {
                Path salesLogCheck = Paths.get(salesFilePath);
                if (salesLogCheck.toFile().isFile()) {
                    salesLog.generateFileName();
                } else {
                    System.out.println("Sales Log is Empty!");
                }
            }
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu(System.in, System.out);
        VendingMachineCLI cli = new VendingMachineCLI(menu);
        cli.run();
    }
}
