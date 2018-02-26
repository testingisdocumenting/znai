package com.twosigma.documentation.website;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;

class ProgressReporter {
    static void reportPhase(String phase) {
        ConsoleOutputs.out(Color.BLUE, phase);
    }
}
