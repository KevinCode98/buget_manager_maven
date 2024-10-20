package org.example.io;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsoleScannerTest {

//    public static MockedStatic<ConsolePrinter> mockPrinter = mockStatic(ConsolePrinter.class);

    @Test
    void validInputTest() {
//        Scanner mockScanner = mock(Scanner.class);
//        when(mockScanner.nextLine()).thenReturn("hola", "200"); // Primero una entrada inválida, luego una válida
//        ConsoleScanner.sc = mockScanner; // Asignar el Scanner mockeado
//
//        String result = ConsoleScanner.insertValue("Input: ",
//                "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?", "Incorrect Input. Try again", "");
//
//        mockPrinter.verify(() -> ConsolePrinter.printInfoAndWaitForReturn("Incorrect Input. Try again"), times(1));
//        verify(mockScanner, times(2)).nextLine(); // Se llamó dos veces porque la primera fue inválida
//        assertEquals("200", result);
    }
}