package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.AnsiConsoleOutput
import com.twosigma.testing.expectation.ExpectationHandlers
import com.twosigma.testing.reporter.ConsoleStepReporter
import com.twosigma.testing.reporter.StepReporters
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestRunner
import com.twosigma.testing.standalone.report.StandardConsoleTestReporter
import com.twosigma.testing.webui.reporter.WebUiExpectationHandler
import com.twosigma.testing.webui.reporter.WebUiMessageBuilder

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class WebUiTestCliApp implements StandaloneTestListener {
    private WebUiTestCliConfig config
    private StandaloneTestRunner runner

    WebUiTestCliApp(String[] args) {
        config = new WebUiTestCliConfig(args)

        runner = new StandaloneTestRunner(["com.twosigma.testing.webui.WebTestDsl"], Paths.get(""))
        runner.addListener(this)
        runner.addListener(new StandardConsoleTestReporter())
    }

    void start() {
        ConsoleOutputs.add(new AnsiConsoleOutput())
        StepReporters.add(new ConsoleStepReporter(WebUiMessageBuilder.converter))
        ExpectationHandlers.add(new WebUiExpectationHandler())

        config.print()

        testFiles().forEach {
            runner.process(it, this)
        }

        runner.runTests()
    }

    private List<Path> testFiles() {
        return [Paths.get(config.getTestFile())]
    }

    static void main(String[] args) {
        new WebUiTestCliApp(args).start()
    }

    @Override
    void beforeFirstTest() {
    }

    @Override
    void beforeScriptParse(Path currentScriptPath) {
    }

    @Override
    void beforeTestRun(StandaloneTest test) {
    }

    @Override
    void afterTestRun(StandaloneTest test) {

    }
}
