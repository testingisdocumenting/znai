package com.twosigma.testing.standalone.report

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.Color
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestListenerAdapter
import com.twosigma.utils.TraceUtils

/**
 * @author mykola
 */
class StandardConsoleTestReporter extends StandaloneTestListenerAdapter {
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
            renderStackTrace(test.exception)
            failed++
        } else if (test.hasError()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "error")
            renderStackTrace(test.exception)
            errored++
        } else {
            ConsoleOutputs.out(Color.GREEN, "[.] ", Color.BLUE, "passed")
            passed++
        }
    }

    @Override
    void afterAllTests() {
        ConsoleOutputs.out()
        ConsoleOutputs.out("Total: ", (passed + failed + errored), ", ",
                Color.GREEN, " Passed: ", passed, ", ",
                Color.RED, " Failed: ", failed, ", ",
                " Errored: ", errored)
    }

    private static void renderStackTrace(Throwable t) {
        if (! (t instanceof AssertionError)) {
            ConsoleOutputs.out(t.getClass().canonicalName, ': ', t.message)
        }

        ConsoleOutputs.out(removeLibsCalls(TraceUtils.stackTrace(t)), "\n\n")
    }

    private static String removeLibsCalls(String stackTrace) {
        return stackTrace.split("\n").findAll {
            ! it.contains("com.twosigma.") &&
            ! it.contains("org.codehaus.groovy")}.join("\n")
    }
}
