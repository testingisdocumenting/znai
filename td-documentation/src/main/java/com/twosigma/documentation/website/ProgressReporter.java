package com.twosigma.documentation.website;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;

public class ProgressReporter {
    public static void reportPhase(String phase) {
        ConsoleOutputs.out(Color.BLUE, phase);
    }

    static void reportWarning(String warning) {
        ConsoleOutputs.out(Color.YELLOW, warning);
    }
}
