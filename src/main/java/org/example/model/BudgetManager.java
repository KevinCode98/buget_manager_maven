package org.example.model;

import org.example.controller.Sort;
import org.example.io.ConsoleScanner;

import java.util.*;

public class BudgetManager {
    private Map<Category, LinkedList<Item>> mapItems;
    private double balance;
    private double total;


    public BudgetManager() {
        this.mapItems = new HashMap<>();
        this.balance = 0;
        this.total = 0;
    }

    public String addToBalance(double income) {
        balance += income;
        return "Income was added!";
    }

    public Map<Category, LinkedList<Item>> getMapItems() {
        return this.mapItems;
    }

    public double getBalance() {
        return this.balance;
    }

    public String showBalance() {
        return ("Balance: $%.2f".formatted(balance));
    }

    public String addItem(String name, double price, Category category, boolean printMessage) {
        if (!mapItems.containsKey(category))
            mapItems.put(category, new LinkedList<>());

        mapItems.get(category).add(new Item(name, price, category));
        balance = Math.max(balance - price, 0.0f);
        total += price;

        if (printMessage)
            return "Purchase was added!";

        return "";
    }

    public String showPurchaseList(Category category) {
        StringBuilder str = new StringBuilder();

        if (category == Category.ALL)
            return showPurchaseList();
        else {
            double totalCategory = 0;
            str.append("Category: ").append(category.name());

            if (!mapItems.containsKey(category)) {
                str.append("The purchase list is empty");
            } else {
                for (Item item : mapItems.get(category)) {
                    str.append("%s $%.2f".formatted(item.name(), item.price()));
                    totalCategory += item.price();
                }
                str.append("Total sum: $%.2f".formatted(totalCategory));
            }
        }

        return str.toString();
    }

    private String showPurchaseList() {
        double totalCategory = 0;
        StringBuilder str = new StringBuilder();
        str.append("All: ");

        if (mapItems.isEmpty()) {
            str.append("The purchase list is empty");
        } else {
            for (Category category : Category.values()) {
                if (mapItems.containsKey(category)) {
                    for (Item item : mapItems.get(category)) {
                        str.append("%s $%.2f".formatted(item.name(), item.price()));
                        totalCategory += item.price();
                    }
                }
            }
            str.append("Total sum: $%.2f".formatted(totalCategory));
        }

        return str.toString();
    }

    public boolean pushItems(String list) {
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
                return false;
            }
        }

        return true;
    }

    public String sortList(Sort sort) {
        String result = "";
        switch (sort) {
            case ALL -> {
                result = sortList();
            }
            case TYPE -> {
                result = getMenuTotalCategories();
            }
            case CERTAIN -> {
                result = menuCertain();
            }
        }

        return result;
    }

    private String menuCertain() {
        String MENU = """
                Choose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other
                """;

        int menuIndex = Integer.parseInt(ConsoleScanner.insertValue(MENU, "[1-4]", "Incorrect option", ""));
        return (getListSortedByCategory(Category.values()[menuIndex - 1]));
    }

    private String sortList() {
        if (mapItems.isEmpty())
            return "The purchase list is empty!";

        PriorityQueue<Item> myQueue = new PriorityQueue<>(new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return Double.compare(item2.price(), item1.price());
            }
        });

        for (Category category : Category.values()) {
            if (mapItems.containsKey(category)) {
                myQueue.addAll(mapItems.get(category));
            }
        }

        StringBuilder str = new StringBuilder();
        while (!myQueue.isEmpty()) {
            Item item = myQueue.poll();
            str.append(item.name()).append(" $%.2f".formatted(item.price())).append('\n');
        }

        return str.toString();
    }


    private double getTotalCategory(Category category) {
        if (!mapItems.containsKey(category))
            return 0.0;

        double totalCategory = 0;
        for (Item item : mapItems.get(category))
            totalCategory += item.price();

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

            myQueue.add(new Pair(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(),
                    getTotalCategory(category)));
        }

        StringBuilder result = new StringBuilder("Types:\n");
        while (!myQueue.isEmpty()) {
            Pair pair = myQueue.poll();
            result.append(pair.name()).append(" - $%.2f".formatted(pair.total())).append("\n");
        }
        result.append("Total - $%.2f".formatted(total));

        return result.toString();
    }

    private String getListSortedByCategory(Category category) {
        if (!mapItems.containsKey(category))
            return "The purchase list is empty!";


        PriorityQueue<Item> myQueue = new PriorityQueue<>(new Comparator<Item>() {
            @Override
            public int compare(Item pair1, Item pair2) {
                return Double.compare(pair2.price(), pair1.price());
            }
        });

        StringBuilder str = new StringBuilder();
        myQueue.addAll(mapItems.get(category));

        while (!myQueue.isEmpty()) {
            Item item = myQueue.poll();
            str.append(item.name()).append(" $%.2f".formatted(item.price())).append('\n');
        }

        return str.toString();
    }
}