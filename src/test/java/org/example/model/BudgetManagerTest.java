package org.example.model;

import org.example.controller.Sort;
import org.example.io.ReaderTextFile;
import org.example.io.WriterTextFiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BudgetManagerTest {
    private BudgetManager budgetManager;

    @BeforeEach
    void setUp() {
        budgetManager = new BudgetManager();
    }

    @Test
    void addingBalanceTest() {
        double income = 100;
        double incomeExpected = 70;

        assertEquals("Income was added!", budgetManager.addToBalance(income));
        assertEquals(income, budgetManager.getBalance());
        assertEquals("Balance: $100.00", budgetManager.showBalance());

        // Adding products in the list
        assertEquals("Purchase was added!", budgetManager.addItem("Milk", 10.0, Category.FOOD, true));
        assertTrue(budgetManager.addItem("Pizza", 20.0, Category.FOOD, false).isEmpty());

        assertEquals(incomeExpected, budgetManager.getBalance());
        assertEquals("Balance: $70.00", budgetManager.showBalance());
    }


    @Test
    void showingListTest() {
        String listExpectedEmpty = """
                %s
                The purchase list is empty
                """;
        assertEquals(budgetManager.showPurchaseList(Category.FOOD), "Category: " + listExpectedEmpty.formatted(Category.FOOD.name()));
        assertEquals(budgetManager.sortList(Sort.ALL), "The purchase list is empty!");

        // Adding products in the list
        assertEquals("Purchase was added!", budgetManager.addItem("Milk", 10.0, Category.FOOD, true));
        assertTrue(budgetManager.addItem("Pizza", 20.0, Category.FOOD, false).isEmpty());

        String listExpected = """
                %s: %s
                Milk $10.00
                Pizza $20.00
                Total sum: $30.00
                """;

        assertEquals(budgetManager.showPurchaseList(Category.FOOD), listExpected.formatted("Category", Category.FOOD.name()));
        String name = Category.ALL.name();
        String categoryName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        assertEquals(budgetManager.showPurchaseList(Category.ALL), listExpected.formatted(categoryName, ""));
    }

    @Test
    void shortListTest() {
        // Adding products in the list
        assertEquals("Purchase was added!", budgetManager.addItem("Milk", 10.0, Category.FOOD, true));
        assertTrue(budgetManager.addItem("Pizza", 20.0, Category.FOOD, false).isEmpty());

        String listExpected = """
                %s: %s
                Milk $10.00
                Pizza $20.00
                Total sum: $30.00
                """;

        assertEquals(budgetManager.showPurchaseList(Category.FOOD), listExpected.formatted("Category", Category.FOOD.name()));
        String name = Category.ALL.name();
        String categoryName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        assertEquals(budgetManager.showPurchaseList(Category.ALL), listExpected.formatted(categoryName, ""));


        // Start sorting products
        String listExpectedSorted = """
                Pizza $20.00
                Milk $10.00
                """;

        assertEquals(budgetManager.sortList(Sort.ALL), listExpectedSorted);
        assertEquals(budgetManager.getListSortedByCategory(Category.FOOD), listExpectedSorted);
    }

    @Test
    void checkTotalCategoriesTest(){
        String menuExpected = """
                Types:
                Food - $0.00
                Other - $0.00
                Entertainment - $0.00
                Clothes - $0.00
                Total - $0.00""";

        assertEquals(budgetManager.sortList(Sort.TYPE), menuExpected);

        // Adding products in the list
        assertEquals("Purchase was added!", budgetManager.addItem("Milk", 10.0, Category.FOOD, true));
        assertTrue(budgetManager.addItem("Pizza", 20.0, Category.FOOD, false).isEmpty());
        assertEquals("Purchase was added!", budgetManager.addItem("T-shirt", 25.0, Category.CLOTHES, true));
        assertTrue(budgetManager.addItem("Movie", 15.0, Category.ENTERTAINMENT, false).isEmpty());
        assertEquals("Purchase was added!", budgetManager.addItem("Tv", 499.0, Category.ENTERTAINMENT, true));
        assertTrue(budgetManager.addItem("Glasses", 50.0, Category.OTHER, false).isEmpty());

        String menuExpectedAddedItems = """
                Types:
                Entertainment - $514.00
                Other - $50.00
                Food - $30.00
                Clothes - $25.00
                Total - $619.00""";

        assertEquals(budgetManager.sortList(Sort.TYPE), menuExpectedAddedItems);
    }


    @Test
    void addingItemsFileTest() {
        double income = 1000;

        assertEquals("Income was added!", budgetManager.addToBalance(income));
        assertEquals(income, budgetManager.getBalance());
        assertEquals("Balance: $1000.00", budgetManager.showBalance());
        // Adding products in the list

        assertEquals("Purchase was added!", budgetManager.addItem("Milk", 10.0, Category.FOOD, true));
        assertTrue(budgetManager.addItem("Pizza", 20.0, Category.FOOD, false).isEmpty());
        assertEquals("Purchase was added!", budgetManager.addItem("T-shirt", 25.0, Category.CLOTHES, true));
        assertTrue(budgetManager.addItem("Movie", 15.0, Category.ENTERTAINMENT, false).isEmpty());
        assertEquals("Purchase was added!", budgetManager.addItem("Tv", 499.0, Category.ENTERTAINMENT, true));
        assertTrue(budgetManager.addItem("Glasses", 50.0, Category.OTHER, false).isEmpty());


        WriterTextFiles.writeInFile(budgetManager.getMapItems(), budgetManager.getBalance());

        budgetManager.deleteItems();
        assertNull(budgetManager.pushItems(ReaderTextFile.readFile()));

        // Creating Map to compare
        Map<Category, LinkedList<Item>> map = new HashMap<>();
        for(Category category : Category.values()){
            if(category == Category.ALL || category == Category.BACK)
                continue;

            map.put(category, new LinkedList<>());
        }

        map.get(Category.FOOD).add(new Item("Milk", 10, Category.FOOD));
        map.get(Category.FOOD).add(new Item("Pizza", 20, Category.FOOD));
        map.get(Category.CLOTHES).add(new Item("T-shirt", 25, Category.CLOTHES));
        map.get(Category.ENTERTAINMENT).add(new Item("Movie", 15, Category.ENTERTAINMENT));
        map.get(Category.ENTERTAINMENT).add(new Item("Tv", 499, Category.ENTERTAINMENT));
        map.get(Category.OTHER).add(new Item("Glasses", 50, Category.OTHER));


        assertEquals(map, budgetManager.getMapItems());
    }
}