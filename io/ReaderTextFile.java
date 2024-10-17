package budget.io;

import budget.controller.MainMenuController;
import budget.model.Category;
import budget.model.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReaderTextFile {
    public static String readFile() {
        File file = new File(MainMenuController.nameFile);

        if (file.exists() && file.isFile()) {
            StringBuilder content = new StringBuilder();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(MainMenuController.nameFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                reader.close();
                return content.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return "";
    }
}
