package com.techelevator.view;

import com.techelevator.Inventory;
import com.techelevator.PurchaseProcess;

public class PurchaseMenu {

    //<editor-fold desc="*DATA MEMBERS*">
    private static final String FEED_MONEY = "Feed Money";
    private static final String SELECT_PRODUCT = "Select Product";
    private static final String FINISH_TRANSACTION = "Finish Transaction";
    private static final String[] PURCHASE_MENU_DISPLAY = {FEED_MONEY, SELECT_PRODUCT, FINISH_TRANSACTION};
    private Menu menu;
    private Inventory inventory;
    private PurchaseProcess purchaseProcess;
    private String salesPathLog;
    //</editor-fold>

    public PurchaseMenu(Menu menu, Inventory inventory, String salesPathLog) {
        this.menu = menu;
        this.inventory = inventory;
        this.salesPathLog = salesPathLog;
        purchaseProcess = new PurchaseProcess(salesPathLog);
    }


    public void run() {
        purchaseLoop:
        while (true) {
            String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_DISPLAY);

            switch (choice) {
                case FEED_MONEY:
                    purchaseProcess.feedMoney();

                    break;
                case SELECT_PRODUCT:
                    inventory.displayAvailableProducts();
                    purchaseProcess.purchaseItem(inventory);

                    break;
                case FINISH_TRANSACTION:
                    purchaseProcess.completeTransaction(inventory);
                    break purchaseLoop;
            }
        }
    }
}