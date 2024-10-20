package org.example.io;

import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

public class ConsolePrinterTest {
    public final static MockedStatic<ConsolePrinter> mockScannerStatic = mockStatic(ConsolePrinter.class);

}