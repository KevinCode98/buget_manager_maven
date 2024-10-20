package org.example.io;

public class ConsolePrinter {
    public static void printInfo(String message) {
        System.out.print(message);
    }

    public static void printInfoLn(String message) {
        System.out.println(message);
    }

    public static void printInfoAndWaitForReturn(String message) {
        System.out.print(message);
        ConsoleScanner.insertValue("", "", "", "");
    }
}
