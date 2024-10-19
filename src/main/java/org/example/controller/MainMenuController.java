package org.example.controller;

import org.example.io.ConsolePrinter;
import org.example.io.ConsoleScanner;
import org.example.io.ReaderTextFile;
import org.example.io.WriterTextFiles;
import org.example.model.BudgetManager;
import org.example.model.Category;

import java.util.Map;
import java.util.Objects;

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


    private final BudgetManager manager;

    public MainMenuController() {
        this.manager = new BudgetManager();
    }

    @Override
    public void run() {
        mainMenuLoop();
    }

    private void mainMenuLoop() {
        Choice choice;
        do {
            choice = (Choice) getMenu(Choice.class.getName(), MENU, "[0-7]");
            ConsolePrinter.printInfoLn("");
            getMenuAction(choice).run();
            ConsolePrinter.printInfoLn("");
        } while (choice != Choice.EXIT);
    }


    private Enum getMenu(String type, String menu, String regex) {
        ConsolePrinter.printInfoLn(menu);
        int menuIndex = Integer.parseInt(ConsoleScanner.insertValue("", regex, "Incorrect option", menu));

        if (type.equals(Sort.class.getName()))
            return Sort.values()[menuIndex - 1];
        else if (type.equals(Category.class.getName()))
            return Category.values()[menuIndex - 1];
        else
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
                Choice.SORT, this::sort,
                Choice.EXIT, this::exit
        ).get(choice);
    }

    // Methods in the map
    private void addIncome() {
        double income = Double.parseDouble(ConsoleScanner.insertValue("Enter income:\n",
                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", "Incorrect Input. Try again", ""));

        ConsolePrinter.printInfoLn(manager.addToBalance(income));
    }

    private void addPurchase() {
        Category category = (Category) getMenu(Category.class.getName(), ADD_PURCHASES, "[1-5]");
        while (category != Category.ALL) {
            String name = ConsoleScanner.insertValue("Enter purchase name:\n", "", "", "");

            double price = Double.parseDouble(ConsoleScanner.insertValue("Enter its price:\n",
                    "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", "Incorrect Input. Try again", ""));

            ConsolePrinter.printInfoLn(manager.addItem(name, price, category, true));
            category = (Category) getMenu(Category.class.getName(), ADD_PURCHASES, "[1-5]");
        }
    }

    private void showList() {
        Category category = (Category) getMenu(Category.class.getName(), MENU_PURCHASES, "[1-6]");
        while (category != Category.BACK) {
            ConsolePrinter.printInfoLn(manager.showPurchaseList(category));
            category = (Category) getMenu(Category.class.getName(), MENU_PURCHASES, "[1-6]");
        }
    }

    private void balance() {
        ConsolePrinter.printInfoLn(manager.showBalance());
    }

    private void saveFile() {
        WriterTextFiles.writeInFile(manager.getMapItems(), manager.getBalance());
        ConsolePrinter.printInfoLn("Purchases were saved!");
    }

    private void loadFile() {
        String result = manager.pushItems(ReaderTextFile.readFile());
        ConsolePrinter.printInfoLn(Objects.requireNonNullElse(result, "Purchases were loaded!"));
    }

    private void sort() {
        Sort sort = (Sort) getMenu(Sort.class.getName(), MENU_SORT, "[1-4]");
        while (sort != Sort.BACK) {
            ConsolePrinter.printInfoLn(manager.sortList(sort));
            sort = (Sort) getMenu(Sort.class.getName(), MENU_SORT, "[1-4]");
            ConsolePrinter.printInfoLn("");
        }
    }

    private void exit() {
        ConsolePrinter.printInfo("Bye!");
    }
}