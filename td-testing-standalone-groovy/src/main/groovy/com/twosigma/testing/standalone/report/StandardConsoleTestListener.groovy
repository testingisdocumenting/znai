package com.twosigma.testing.standalone.report

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.Color
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListenerAdapter

/**
 * @author mykola
 */
class StandardConsoleTestListener extends StandaloneTestListenerAdapter {
    private int passed
    private int failed
    private int errored

    @Override
    void beforeTestRun(StandaloneTest test) {
        ConsoleOutputs.out(Color.GREEN, test.description.trim())
        ConsoleOutputs.out(Color.PURPLE, test.filePath, "\n")
    }

    @Override
    void afterTestRun(StandaloneTest test) {
        if (test.isFailed()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "failed")
            displayStackTrace(test.exception)
            failed++
        } else if (test.hasError()) {
            ConsoleOutputs.out(Color.RED, "[~] ", Color.BLUE, "error")
            displayStackTrace(test.exception)
            errored++
        } else {
            ConsoleOutputs.out(Color.GREEN, "[.] ", Color.BLUE, "passed")
            passed++
        }
    }

    int getTotal() {
        return passed + failed + errored
    }

    int getPassed() {
        return passed
    }

    int getFailed() {
        return failed
    }

    int getErrored() {
        return errored
    }

    @Override
    void afterAllTests() {
        ConsoleOutputs.out()
        ConsoleOutputs.out("Total: ", (passed + failed + errored), ", ",
                Color.GREEN, " Passed: ", passed, ", ",
                Color.RED, " Failed: ", failed, ", ",
                " Errored: ", errored)
    }

    private static void displayStackTrace(Throwable t) {
        ConsoleOutputs.out(GroovyStackTraceUtils.renderStackTraceWithoutLibCalls(t), "\n\n")
    }
}
