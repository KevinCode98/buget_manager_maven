package org.example.io;

import org.example.controller.MainMenuController;
import org.example.model.Category;
import org.example.model.Item;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WriterTextFilesTest {
    @Test
    void writingInTextFileTest() throws IOException {
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

        WriterTextFiles.writeInFile(map, 619);
        assertTrue(Files.exists(Paths.get(MainMenuController.nameFile)));
    }
}