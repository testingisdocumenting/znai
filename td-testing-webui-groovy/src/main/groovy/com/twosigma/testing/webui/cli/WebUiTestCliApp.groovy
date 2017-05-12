package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutput
import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.AnsiConsoleOutput
import com.twosigma.testing.reporter.ConsoleStepReporter
import com.twosigma.testing.reporter.StepReporter
import com.twosigma.testing.reporter.StepReporters
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestListeners
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
    private static StandaloneTestListener consoleTestReporter = new StandardConsoleTestReporter()
    private static StepReporter stepReporter = new ConsoleStepReporter(WebUiMessageBuilder.converter)
    private static ConsoleOutput consoleOutput = new AnsiConsoleOutput()

    private WebUiTestCliConfig config
    private StandaloneTestRunner runner

    WebUiTestCliApp(String[] args) {
        ConsoleOutputs.add(consoleOutput)

        config = new WebUiTestCliConfig(args)

        runner = new StandaloneTestRunner(["com.twosigma.testing.webui.WebTestDsl"], Paths.get(""))
        StandaloneTestListeners.add(consoleTestReporter)
        WebTestGroovyDsl.initWithTestRunner(runner)
    }

    void start() {
        StepReporters.add(stepReporter)

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
