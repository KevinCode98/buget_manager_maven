package budget.controller;

import budget.io.ConsolePrinter;
import budget.io.ReaderTextFile;
import budget.io.WriterTextFiles;
import budget.model.BudgetManager;
import budget.model.Category;
import budget.model.Item;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class MainMenuController implements Runnable {
    public final static String nameFile = "purchases.txt";

    private static String MENU = """
            Choose your action:
            1) Add income
            2) Add purchase
            3) Show list of purchases
            4) Balance
            5) Save
            6) Load
            7) Analyze (Sort)
            0) Exit""";

    private static String MENU_PURCHASES = """
            Choose the type of purchases
            1) Food
            2) Clothes
            3) Entertainment
            4) Other
            5) All
            6) Back""";

    private static String ADD_PURCHASES = """
            1) Food
            2) Clothes
            3) Entertainment
            4) Other
            5) Back""";

    private static String MENU_SORT = """
            1) Sort all purchases
            2) Sort by type
            3) Sort certain type
            4) Back""";


    private final Scanner sc;
    private final ConsolePrinter printer;
    private final BudgetManager manager;

    public MainMenuController(Scanner sc, ConsolePrinter printer) {
        this.sc = sc;
        this.printer = printer;
        this.manager = new BudgetManager(printer, sc);
    }

    @Override
    public void run() {
        mainMenuLoop();
        printer.printInfoLn("");
        printer.printInfo("Bye!\n");
    }

    private String scanValidate(String regex, Runnable invalidAction) {
        var input = sc.nextLine();
        while (!input.matches(regex)) {
            invalidAction.run();
            input = sc.nextLine();
        }
        return input;
    }

    private void mainMenuLoop() {
        Choice choice = getMenuChoice();
        while (choice != Choice.EXIT) {
            getMenuAction(choice).run();
            printer.printInfoLn("");
            choice = getMenuChoice();
        }
    }

    private Category getMenuCategory(String menu, String regex) {
        printer.printInfoLn("");
        printer.printInfoLn(menu);
        int menuIndex = Integer.parseInt(scanValidate(regex, () -> {
            printer.printInfoAndWaitForReturn(sc, "Incorrect option");
            printer.clearAndPrint(menu);
        }));

        return Category.values()[menuIndex - 1];
    }

    private Sort getMenuSort(String menu, String regex) {
        printer.printInfoLn("");
        printer.printInfoLn(menu);
        int menuIndex = Integer.parseInt(scanValidate(regex, () -> {
            printer.printInfoAndWaitForReturn(sc, "Incorrect option");
            printer.clearAndPrint(menu);
        }));

        return Sort.values()[menuIndex - 1];
    }

    private Choice getMenuChoice() {
        printer.printInfoLn(MENU);
        int menuIndex = Integer.parseInt(scanValidate("[0-7]", () -> {
            printer.printInfoAndWaitForReturn(sc, "Incorrect option");
            printer.clearAndPrint(MENU);
        }));
        return Choice.values()[menuIndex];
    }

    private Runnable getMenuAction(Choice choice, Object... parameters) {
        return Map.<Choice, Runnable>of(
                Choice.ADD_INCOME, this::addIncome,
                Choice.ADD_PURCHASE, this::addPurchase,
                Choice.SHOW, this::showList,
                Choice.BALANCE, this::balance,
                Choice.SAVE, this::saveFile,
                Choice.LOAD, this::loadFile,
                Choice.SORT, this::sort
        ).get(choice);
    }

    // Methods in the map
    private void addIncome() {
        printer.printInfoLn("");
        printer.printInfoLn("Enter income: ");
        double income = Double.parseDouble(scanValidate("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", () ->
                printer.printInfoLn("Incorrect Input. Try again: ")));

        manager.addToBalance(income);
    }

    private void addPurchase() {
        Category category = getMenuCategory(ADD_PURCHASES, "[1-5]");
        while (category != Category.ALL) {
            printer.printInfoLn("");
            printer.printInfoLn("Enter purchase name: ");
            String name = sc.nextLine();
            printer.printInfoLn("Enter its price: ");
            double price = Double.parseDouble(scanValidate("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", () ->
                    printer.printInfoLn("Incorrect Input. Try again: ")));

            manager.addItem(name, price, category, true);
            category = getMenuCategory(ADD_PURCHASES, "[1-5]");
        }
    }

    private void showList() {
        Category category = getMenuCategory(MENU_PURCHASES, "[1-6]");
        while (category != Category.BACK) {
            manager.showPurchaseList(category);
            category = getMenuCategory(MENU_PURCHASES, "[1-6]");
        }
    }

    private void balance() {
        printer.printInfoLn("");
        manager.showBalance();
    }

    private void saveFile() {
        printer.printInfoLn("");
        WriterTextFiles.writeInFile(manager.getMapItems(), manager.getBalance());
        printer.printInfoLn("Purchases were saved!");
    }

    private void loadFile() {
        printer.printInfoLn("");
        manager.pushItems(ReaderTextFile.readFile());
        printer.printInfoLn("Purchases were loaded!");
    }

    private void sort() {
        Sort sort = getMenuSort(MENU_SORT, "[1-4]");
        while (sort != Sort.BACK) {
            manager.sortList(sort);
            sort = getMenuSort(MENU_SORT, "[1-4]");
        }
    }
}
