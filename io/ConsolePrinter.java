package budget.io;

import java.io.IOException;
import java.util.Scanner;

public class ConsolePrinter {
    public final void printInfo(String message) {
        System.out.print(message);
    }

    public final void printInfoLn(String message) {
        System.out.println(message);
    }

    public final void printInfoAndWaitForReturn(Scanner sc, String message) {
        System.out.print(message);
        sc.nextLine();
    }

    public void clearAndPrint(String message) {
        System.out.println();
        printInfoLn(message);
    }

    private static void clear() {
        try {
            var clearCommand = System.getProperty("os.name").contains("Windows")
                    ? new ProcessBuilder("cmd", "/c", "cls")
                    : new ProcessBuilder("clear");
            clearCommand.inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
        }
    }

}
