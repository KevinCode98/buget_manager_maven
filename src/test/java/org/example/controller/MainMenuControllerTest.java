package org.example.controller;

import org.example.io.ConsolePrinter;
import org.example.io.ConsolePrinterTest;
import org.example.io.ConsoleScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@MockitoSettings
class MainMenuControllerTest {
    MainMenuController controller;

    @BeforeEach
    void setUp() {
        controller = new MainMenuController();
    }

    @Test
    void correctPrintingTest() {
        MockedStatic<ConsolePrinter> mockPrinter = ConsolePrinterTest.mockScannerStatic;
        try (MockedStatic<ConsoleScanner> mockScanner = mockStatic(ConsoleScanner.class)) {
            mockScanner.when(() -> ConsoleScanner.insertValue(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn("1", "1000", "2", "1", "Milk", "10.00", "1", "Pizza", "20.00", "4", "Tv", "299", "5", "3", "5",
                            "6", "7", "3", "1", "4", "4", "0");

            controller.run();
            mockPrinter.verify(() -> ConsolePrinter.printInfoLn("""
                    Choose your action:
                    1) Add income
                    2) Add purchase
                    3) Show list of purchases
                    4) Balance
                    5) Save
                    6) Load
                    7) Analyze (Sort)
                    0) Exit"""), times(6));

            mockPrinter.verify(() -> ConsolePrinter.printInfoLn("Income was added!"));

            mockPrinter.verify(() -> ConsolePrinter.printInfoLn("""
                    1) Sort all purchases
                    2) Sort by type
                    3) Sort certain type
                    4) Back"""), times(2));

            mockPrinter.verify(() -> ConsolePrinter.printInfoLn("""
                    1) Food
                    2) Clothes
                    3) Entertainment
                    4) Other
                    5) Back"""), times(4));

            mockPrinter.verify(() -> ConsolePrinter.printInfoLn("""
                    All:\s
                    Milk $10.00
                    Pizza $20.00
                    Tv $299.00
                    Total sum: $329.00
                    """));

            mockPrinter.verify(() -> ConsolePrinter.printInfoLn("Pizza $20.00\nMilk $10.00\n"));

            mockPrinter.verify(() -> ConsolePrinter.printInfoLn("Balance: $671.00"));

            mockPrinter.verify(() -> ConsolePrinter.printInfo("Bye!"));
        }
    }
}