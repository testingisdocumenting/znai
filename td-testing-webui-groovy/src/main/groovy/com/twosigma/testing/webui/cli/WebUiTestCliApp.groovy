package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.AnsiConsoleOutput
import com.twosigma.console.ansi.Color
import com.twosigma.testing.expectation.ExpectationHandlers
import com.twosigma.testing.reporter.ConsoleStepReporter
import com.twosigma.testing.reporter.StepReporters
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestRunner
import com.twosigma.testing.standalone.report.StandardConsoleTestReporter
import com.twosigma.testing.webui.WebTestGroovyDsl
import com.twosigma.testing.webui.reporter.WebUiMessageBuilder

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class WebUiTestCliApp {
    private WebUiTestCliConfig config
    private StandaloneTestRunner runner

    WebUiTestCliApp(String[] args) {
        config = new WebUiTestCliConfig(args)

        runner = new StandaloneTestRunner(["com.twosigma.testing.webui.WebTestDsl"], Paths.get(""))
        runner.addListener(new StandardConsoleTestReporter())
        WebTestGroovyDsl.initWithTestRunner(runner)
    }

    void start() {
        ConsoleOutputs.add(new AnsiConsoleOutput())
        StepReporters.add(new ConsoleStepReporter(WebUiMessageBuilder.converter))

        config.print()

        testFiles().forEach {
            runner.process(it, this)
        }

        runner.runTests()
    }

    private List<Path> testFiles() {
        return config.getTestFiles().collect { Paths.get(it) }
    }

    static void main(String[] args) {
        new WebUiTestCliApp(args).start()
    }
}
