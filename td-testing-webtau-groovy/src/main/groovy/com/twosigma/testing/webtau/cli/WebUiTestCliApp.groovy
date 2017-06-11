package com.twosigma.testing.webtau.cli

import com.twosigma.console.ConsoleOutput
import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.AnsiConsoleOutput
import com.twosigma.testing.reporter.ConsoleStepReporter
import com.twosigma.testing.reporter.StepReporter
import com.twosigma.testing.reporter.StepReporters
import com.twosigma.testing.reporter.TestStep
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestListeners
import com.twosigma.testing.standalone.StandaloneTestRunner
import com.twosigma.testing.standalone.report.StandardConsoleTestListener
import com.twosigma.testing.webtau.WebTauGroovyDsl
import com.twosigma.testing.webtau.cfg.WebUiTestConfig
import com.twosigma.testing.webtau.driver.WebDriverCreator
import com.twosigma.testing.webtau.reporter.WebReportStepReporter
import com.twosigma.testing.webtau.reporter.WebUiMessageBuilder
import com.twosigma.utils.JsonUtils

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class WebUiTestCliApp implements StandaloneTestListener {
    private static WebUiTestConfig cfg = WebUiTestConfig.INSTANCE
    private static StandaloneTestListener consoleTestReporter = new StandardConsoleTestListener()
    private static StepReporter stepReporter = new ConsoleStepReporter(WebUiMessageBuilder.converter)
    private static WebReportStepReporter webReportStepReporter = new WebReportStepReporter()
    private static ConsoleOutput consoleOutput = new AnsiConsoleOutput()

    private WebUiTestCliConfig config
    private StandaloneTestRunner runner

    WebUiTestCliApp(String[] args) {
        ConsoleOutputs.add(consoleOutput)

        config = new WebUiTestCliConfig(args)

        runner = new StandaloneTestRunner(["com.twosigma.testing.webtau.WebTauDsl"], cfg.getWorkingDir())

        StandaloneTestListeners.add(consoleTestReporter)
        StandaloneTestListeners.add(this)
        WebTauGroovyDsl.initWithTestRunner(runner)
    }

    void start() {
        StepReporters.add(stepReporter)
        StepReporters.add(webReportStepReporter)

        config.print()

        testFiles().forEach {
            runner.process(it, this)
        }

        runner.runTests()
        WebDriverCreator.closeAll()
    }

    private List<Path> testFiles() {
        return config.getTestFiles().collect { Paths.get(it) }
    }

    static void main(String[] args) {
        new WebUiTestCliApp(args).start()
    }

    @Override
    void beforeFirstTest() {
    }

    @Override
    void beforeScriptParse(Path scriptPath) {
    }

    @Override
    void beforeTestRun(StandaloneTest test) {
    }

    @Override
    void afterTestRun(StandaloneTest test) {
        def steps = webReportStepReporter.getStepsAndReset()
        def listOfMaps = steps.collect { it.toMap() }

        println JsonUtils.serializePrettyPrint(listOfMaps)
    }

    @Override
    void afterAllTests() {
    }
}
