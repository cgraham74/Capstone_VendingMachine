package com.techelevator;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class PurchaseProcessTest extends TestCase {


    private ByteArrayOutputStream output;
    private String salesPath = "log\\sales.log";
    PurchaseProcess purchaseProcess = new PurchaseProcess(salesPath);
    Inventory testInventory = new Inventory();

    @Before
    public void setup() {
        output = new ByteArrayOutputStream();
        testInventory.createInventory("vendingmachine.csv");
    }

    @Test
    public void testFeedMoney_floating_point_number_should_be_ZERO() {
        InputStream clearOut = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("7.32".getBytes());
        System.setIn(in);
        purchaseProcess.feedMoney();
        Assert.assertEquals(0.00, purchaseProcess.getCurrentBalance(), 0);
        System.setIn(clearOut);
    }

    @Test
    public void testFeedMoney_Whole_Number_Should_Be_Accepted() {
        InputStream clearOut = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("5.00".getBytes());
        System.setIn(in);
        purchaseProcess.feedMoney();
        Assert.assertEquals(5.00, purchaseProcess.getCurrentBalance(), 0);
        System.setIn(clearOut);
    }

    @Test
    public void testHappyPathPurchaseItem() {
        testInventory.createInventory("vendingmachine.csv");
        InputStream clearOut = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("A1".getBytes());
        System.setIn(in);
        purchaseProcess.setCurrentBalance(5.00);
        purchaseProcess.purchaseItem(testInventory);
        Assert.assertEquals(1.95, purchaseProcess.getCurrentBalance(), 0.001);
        System.setIn(clearOut);
    }

    @Test
    public void testSadPathPurchaseItem() {
        InputStream clearOut = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("Z9".getBytes());
        System.setIn(in);
        purchaseProcess.setCurrentBalance(3.05);
        purchaseProcess.purchaseItem(testInventory);
        Assert.assertEquals(3.05, purchaseProcess.getCurrentBalance(), 0);
        System.setIn(clearOut);
    }

    @Test
    public void testSoldOutPurchaseItem() {
        testInventory.createInventory("vendingmachine.csv");
        InputStream clearOut = System.in;
        purchaseProcess.setCurrentBalance(7.00);
        for (int i = 0; i < 6; i++) {
            ByteArrayInputStream in = new ByteArrayInputStream("D4".getBytes());
            System.setIn(in);
            purchaseProcess.purchaseItem(testInventory);
        }
        Assert.assertEquals(3.25, purchaseProcess.getCurrentBalance(), 0.001);
        System.setIn(clearOut);
    }

    @Test
    public void testHappySadPathPurchaseItem() {
        InputStream clearOut = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("A1".getBytes());
        System.setIn(in);
        purchaseProcess.setCurrentBalance(1.00);
        purchaseProcess.purchaseItem(testInventory);
        Assert.assertEquals(1.00, purchaseProcess.getCurrentBalance(), 0.001);
        System.setIn(clearOut);
    }

    @Test
    public void testcompleteTransaction() {
        purchaseProcess.setCurrentBalance(1.00);
        String testResults = purchaseProcess.completeTransaction(testInventory);
        assertEquals("Dispensing Change: $1.00\r\n" +
                "Quarters: 4\r\nDimes: 0\r\nNickels: 0\r\n\r\nVending Machine Balance: $0.00\r\n" +
                "Thanks for using the Vendo-Matic 800!", testResults);
    }

    @Test
    public void testOneTwentycompleteTransaction() {
        purchaseProcess.setCurrentBalance(1.20);
        String testResults = purchaseProcess.completeTransaction(testInventory);
        assertEquals("Dispensing Change: $1.20\r\n" +
                "Quarters: 4\r\nDimes: 2\r\nNickels: 0\r\n\r\nVending Machine Balance: $0.00\r\n" +
                "Thanks for using the Vendo-Matic 800!", testResults);
    }

    @Test
    public void completeTransactionTwoPopcornsAndACola() {
        testInventory.createInventory("vendingmachine.csv");
        InputStream clearOut = System.in;
        purchaseProcess.setCurrentBalance(10.00);
        for (int i = 0; i < 2; i++) {
            ByteArrayInputStream in = new ByteArrayInputStream("a4".getBytes());
            System.setIn(in);
            purchaseProcess.purchaseItem(testInventory);
        }
        ByteArrayInputStream cola = new ByteArrayInputStream("c2".getBytes());
        System.setIn(cola);
        purchaseProcess.purchaseItem(testInventory);
        String testResults = purchaseProcess.completeTransaction(testInventory);
        assertEquals("Dispensing Change: $1.20\r\n" +
                "Quarters: 4\r\nDimes: 2\r\nNickels: 0\r\nVending Machine Balance: $0.00\r\n" +
                "Thanks for using the Vendo-Matic 800!", testResults);
        System.setIn(clearOut);
    }
    @Test
    public void testGreedyCoin(){
        purchaseProcess.greedyCoin(125);
        String testResults = purchaseProcess.greedyCoin(125);
        assertEquals("Quarters: " + 5 + "\r\n" + "Dimes: " + 0 + "\r\n" + "Nickels: " + 0 + "\r\n",testResults);
    }

    @Test
    public void testGreedyCoinZero() {
        purchaseProcess.greedyCoin(0);
        String testResults = purchaseProcess.greedyCoin(0);
        assertEquals("Quarters: " + 0 + "\r\n" + "Dimes: " + 0 + "\r\n" + "Nickels: " + 0 + "\r\n",testResults);

    }
}