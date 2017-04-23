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
    @Override
    void afterTestRun(StandaloneTest test) {
        if (test.isFailed()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "failed")
            ConsoleOutputs.out(test.assertionMessage)
        } else if (test.hasError()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "error")
            ConsoleOutputs.out(TraceUtils.stackTrace(test.exception))
        } else {
            ConsoleOutputs.out(Color.GREEN, "[.] ", Color.BLUE, "passed")
        }
    }
}
