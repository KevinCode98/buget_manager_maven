package budget.io;

import budget.controller.MainMenuController;
import budget.model.Category;
import budget.model.Item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

public class WriterTextFiles {
    public static void writeInFile(Map<Category, LinkedList<Item>> mapItems, double balance) {
        try {
            FileWriter writer = new FileWriter(MainMenuController.nameFile, false);
            writer.write(balance + "\n");

            for (Map.Entry<Category, LinkedList<Item>> entry : mapItems.entrySet()) {
                for (Item item : entry.getValue()) {
                    writer.write(entry.getKey() + "-:-" + item.getName() + "-:-" + item.getPrice() + "\n");
                }
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
