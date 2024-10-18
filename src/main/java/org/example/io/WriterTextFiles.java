package org.example.io;

import org.example.controller.MainMenuController;
import org.example.model.Category;
import org.example.model.Item;

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
                    writer.write(entry.getKey() + "-:-" + item.name() + "-:-" + item.price() + "\n");
                }
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
