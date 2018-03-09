package com.twosigma.testing.webtau.cli

import com.twosigma.console.ConsoleOutput
import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.AnsiConsoleOutput
import com.twosigma.console.ansi.Color
import com.twosigma.documentation.DocumentationArtifactsLocation
import com.twosigma.testing.http.HttpRequestHeader
import com.twosigma.testing.http.HttpValidationResult
import com.twosigma.testing.http.config.HttpConfiguration
import com.twosigma.testing.http.config.HttpConfigurations
import com.twosigma.testing.reporter.ConsoleStepReporter
import com.twosigma.testing.reporter.IntegrationTestsMessageBuilder
import com.twosigma.testing.reporter.StepReporter
import com.twosigma.testing.reporter.StepReporters
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestListeners
import com.twosigma.testing.standalone.StandaloneTestRunner
import com.twosigma.testing.standalone.report.StandardConsoleTestListener
import com.twosigma.testing.webtau.WebTauGroovyDsl
import com.twosigma.testing.webtau.cfg.WebTauConfig
import com.twosigma.testing.webtau.driver.WebDriverCreator
import com.twosigma.testing.webtau.reporter.HtmlReportGenerator
import com.twosigma.testing.webtau.reporter.ScreenshotStepPayload
import com.twosigma.testing.webtau.reporter.ScreenshotStepReporter
import com.twosigma.utils.FileUtils
import com.twosigma.utils.JsonUtils

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class WebTauCliApp implements StandaloneTestListener {
    private static WebTauConfig cfg = WebTauConfig.INSTANCE
    private static StandardConsoleTestListener consoleTestReporter = new StandardConsoleTestListener()
    private static StepReporter stepReporter = new ConsoleStepReporter(IntegrationTestsMessageBuilder.converter)
    private static ScreenshotStepReporter screenshotStepReporter = new ScreenshotStepReporter()
    private static ConsoleOutput consoleOutput = new AnsiConsoleOutput()

    private WebTauTestCliConfig config
    private StandaloneTestRunner runner

    private List<StandaloneTest> tests = []

    WebTauCliApp(String[] args) {
        ConsoleOutputs.add(consoleOutput)

        config = new WebTauTestCliConfig(args)
        DocumentationArtifactsLocation.setRoot(cfg.getDocArtifactsPath())

        runner = new StandaloneTestRunner(["com.twosigma.testing.webtau.WebTauDsl"], cfg.getWorkingDir())

        StandaloneTestListeners.add(consoleTestReporter)
        StandaloneTestListeners.add(this)
        WebTauGroovyDsl.initWithTestRunner(runner)
    }

    void start() {
        StepReporters.add(stepReporter)
        StepReporters.add(screenshotStepReporter)

        config.print()

        initHttpConfigurationFromConfig()

        testFiles().forEach {
            runner.process(it, this)
        }

        runner.runTests()
        WebDriverCreator.closeAll()
    }

    private List<Path> testFiles() {
        return config.getTestFiles().collect { Paths.get(it) }
    }

    void initHttpConfigurationFromConfig() {
        def headersProvider = config.httpHeadersProvider()
        if (! headersProvider) {
            return
        }

        HttpConfigurations.add(new HttpConfiguration() {
            @Override
            String fullUrl(String url) {
                return url
            }

            @Override
            HttpRequestHeader fullHeader(HttpRequestHeader given) {
                try {
                    HttpConfigurations.disable()
                    return headersProvider.call(given) as HttpRequestHeader
                } finally {
                    HttpConfigurations.enable()
                }
            }
        })
    }

    static void main(String[] args) {
        new WebTauCliApp(args).start()
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
        def steps = test.steps
        def listOfMaps = steps.collect { it.toMap() }

        test.addResultPayload({ [steps: listOfMaps ]})

        def payloads = steps.combinedPayloads.flatten()

        def screenshotsPayloads = payloads.findAll { it instanceof ScreenshotStepPayload }
        if (! screenshotsPayloads.isEmpty()) {
            test.addResultPayload({ [screenshot: screenshotsPayloads[0].base64png] })
        }

        def httpPayloads = payloads.findAll { it instanceof HttpValidationResult }
        if (! httpPayloads.isEmpty()) {
            test.addResultPayload({ [httpCalls: httpPayloads*.toMap()] })
        }

        tests.add(test)
    }

    @Override
    void afterAllTests() {
        generateReport()
    }

    void generateReport() {
        def summary = [
                total: consoleTestReporter.total,
                passed: consoleTestReporter.passed,
                failed: consoleTestReporter.failed,
                skipped: consoleTestReporter.skipped,
                errored: consoleTestReporter.errored]

        def report = [summary: summary, tests: tests*.toMap()]
        def json = JsonUtils.serializePrettyPrint(report)

        def reportPath = cfg.getReportPath().toAbsolutePath()
        FileUtils.writeTextContent(reportPath, new HtmlReportGenerator().generate(json))

        ConsoleOutputs.out(Color.BLUE, "report is generated: ", Color.PURPLE, " ", reportPath)
    }
}
