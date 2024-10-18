package org.example.io;

import java.util.Scanner;

public class ConsoleScanner {
    public final static Scanner sc = new Scanner(System.in);
    public final static ConsolePrinter printer = new ConsolePrinter();

    public static String insertValue(String message, String regex, String error, String menu) {
        printer.printInfo(message);
        if (regex.isEmpty())
            return sc.nextLine();

        return (scanValidate(regex, () -> {
            printer.printInfoAndWaitForReturn(sc, error);
            printer.printInfoLn(menu);
        }));
    }

    private static String scanValidate(String regex, Runnable invalidAction) {
        var input = sc.nextLine();
        while (!input.matches(regex)) {
            invalidAction.run();
            input = sc.nextLine();
        }
        return input;
    }
}
