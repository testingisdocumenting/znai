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
        ConsoleOutputs.out(Color.GREEN, test.description)
        ConsoleOutputs.out(Color.PURPLE, test.filePath)
    }

    @Override
    void afterTestRun(StandaloneTest test) {
        if (test.isFailed()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "failed")
            ConsoleOutputs.out(TraceUtils.stackTrace(test.exception))
            failed++
        } else if (test.hasError()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "error")
            ConsoleOutputs.out(TraceUtils.stackTrace(test.exception))
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
}
