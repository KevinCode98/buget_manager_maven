package budget.model;

import budget.controller.Sort;
import budget.io.ConsolePrinter;

import java.util.*;

public class BudgetManager {
    private final ConsolePrinter printer;
    private final Scanner sc;
    private Map<Category, LinkedList<Item>> mapItems;
    private double balance;
    private double total;


    public BudgetManager(ConsolePrinter printer, Scanner sc) {
        this.sc = sc;
        this.printer = printer;
        this.mapItems = new HashMap<>();
        this.balance = 0;
        this.total = 0;
    }

    public void addToBalance(double income) {
        balance += income;
        printer.printInfoLn("Income was added!");
    }

    public Map<Category, LinkedList<Item>> getMapItems() {
        return this.mapItems;
    }

    public double getBalance() {
        return this.balance;
    }

    public void showBalance() {
        printer.printInfoLn("Balance: $%.2f".formatted(balance));
    }

    public void addItem(String name, double price, Category category, boolean printMessage) {
        if (!mapItems.containsKey(category))
            mapItems.put(category, new LinkedList<>());

        mapItems.get(category).add(new Item(name, price, category));
        balance = Math.max(balance - price, 0.0f);
        total += price;

        if (printMessage)
            printer.printInfoLn("Purchase was added!");
    }

    public void showPurchaseList(Category category) {
        printer.printInfoLn("");
        if (category == Category.ALL)
            showPurchaseList();
        else {
            double totalCategory = 0;
            printer.printInfoLn("Category: " + category.name());

            if (!mapItems.containsKey(category)) {
                printer.printInfoLn("The purchase list is empty");
            } else {
                for (Item item : mapItems.get(category)) {
                    printer.printInfoLn("%s $%.2f".formatted(item.getName(), item.getPrice()));
                    totalCategory += item.getPrice();
                }
                printer.printInfoLn("Total sum: $%.2f".formatted(totalCategory));
            }
        }
    }

    private void showPurchaseList() {
        double totalCategory = 0;
        printer.printInfoLn("All: ");

        if (mapItems.isEmpty()) {
            printer.printInfoLn("The purchase list is empty");
        } else {
            for (Category category : Category.values()) {
                if (mapItems.containsKey(category)) {
                    for (Item item : mapItems.get(category)) {
                        printer.printInfoLn("%s $%.2f".formatted(item.getName(), item.getPrice()));
                        totalCategory += item.getPrice();
                    }
                }
            }
            printer.printInfoLn("Total sum: $%.2f".formatted(totalCategory));
        }
    }

    public void pushItems(String list) {
        if (!list.isEmpty()) {
            String[] result = list.split("\n");
            for (int i = 1; i < result.length; i++) {
                String[] parts = result[i].split("-:-");
                addItem(parts[1], Double.parseDouble(parts[2]), Category.valueOf(parts[0]), false);
            }

            try {
                if (!result[0].isEmpty())
                    balance = Double.parseDouble(result[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: The string does not contain a valid double.");
            }
        }
    }

    public void sortList(Sort sort) {
        printer.printInfoLn("");
        switch (sort) {
            case ALL -> sortList();
            case TYPE -> System.out.println(getMenuTotalCategories());
            case CERTAIN -> menuCertain();
        }
    }

    private void menuCertain() {
        String MENU = """
                Choose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other""";

        Category category = getMenuCategory(MENU, "[1-4]");
        getListSortedByCategory(category);
    }

    private Category getMenuCategory(String menu, String regex) {
        printer.printInfoLn(menu);
        int menuIndex = Integer.parseInt(scanValidate(regex, () -> {
            printer.printInfoAndWaitForReturn(sc, "Incorrect option");
            printer.clearAndPrint(menu);
        }));

        return Category.values()[menuIndex - 1];
    }

    private String scanValidate(String regex, Runnable invalidAction) {
        var input = sc.nextLine();
        while (!input.matches(regex)) {
            invalidAction.run();
            input = sc.nextLine();
        }
        return input;
    }

    private void sortList() {
        if (mapItems.isEmpty()) {
            printer.printInfoLn("The purchase list is empty!");
            return;
        }

        PriorityQueue<Item> myQueue = new PriorityQueue<>(new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return Double.compare(item2.getPrice(), item1.getPrice());
            }
        });

        for (Category category : Category.values()) {
            if (mapItems.containsKey(category)) {
                myQueue.addAll(mapItems.get(category));
            }
        }
        while (!myQueue.isEmpty()) {
            Item item = myQueue.poll();
            System.out.println(item.getName() + " $%.2f".formatted(item.getPrice()));
        }
    }


    private double getTotalCategory(Category category) {
        if (!mapItems.containsKey(category))
            return 0.0;

        double totalCategory = 0;
        for (Item item : mapItems.get(category))
            totalCategory += item.getPrice();

        return totalCategory;
    }

    private String getMenuTotalCategories() {
        PriorityQueue<Pair> myQueue = new PriorityQueue<>(new Comparator<Pair>() {
            @Override
            public int compare(Pair pair1, Pair pair2) {
                return Double.compare(pair2.total(), pair1.total());
            }
        });


        for (Category category : Category.values()) {
            if (category == Category.ALL || category == Category.BACK)
                continue;

            String name = category.name();

            myQueue.add(new Pair(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(), getTotalCategory(category)));
        }

        StringBuilder result = new StringBuilder("Types:\n");
        while (!myQueue.isEmpty()) {
            Pair pair = myQueue.poll();
            result.append(pair.name()).append(" - $%.2f".formatted(pair.total())).append("\n");
        }
        result.append("Total - $%.2f".formatted(total));

        return result.toString();
    }

    private void getListSortedByCategory(Category category) {
        printer.printInfoLn("");

        if (!mapItems.containsKey(category)) {
            printer.printInfoLn("The purchase list is empty!");
            return;
        }


        PriorityQueue<Item> myQueue = new PriorityQueue<>(new Comparator<Item>() {
            @Override
            public int compare(Item pair1, Item pair2) {
                return Double.compare(pair2.getPrice(), pair1.getPrice());
            }
        });

        for (Item item : mapItems.get(category)) {
            myQueue.add(item);
        }

        while (!myQueue.isEmpty()) {
            Item item = myQueue.poll();
            System.out.println(item.getName() + " $%.2f".formatted(item.getPrice()));
        }
    }
}