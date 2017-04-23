package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.AnsiConsoleOutput
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestRunner
import com.twosigma.testing.standalone.report.StandardConsoleTestReporter
import com.twosigma.testing.webui.page.PageObject
import com.twosigma.testing.webui.page.PageObjectLoader
import com.twosigma.utils.FileUtils

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class WebUiTestCliApp implements StandaloneTestListener {
    private WebUiTestCliConfig config
    private StandaloneTestRunner runner
    private PageObjectLoader pageObjectLoader

    WebUiTestCliApp(String[] args) {
        config = new WebUiTestCliConfig(args)

        runner = new StandaloneTestRunner(["com.twosigma.testing.webui.WebTestDsl"])
        runner.addListener(this)
        runner.addListener(new StandardConsoleTestReporter())
        pageObjectLoader = new PageObjectLoader(runner.groovy)
    }

    void start() {
        ConsoleOutputs.add(new AnsiConsoleOutput());

        config.print()

        testFiles().forEach {
            runner.processScriptWithPath(it, pageObjectLoader)
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
        pageObjectLoader.currentScriptPath = currentScriptPath
    }

    @Override
    void beforeTestRun(StandaloneTest test) {
    }

    @Override
    void afterTestRun(StandaloneTest test) {

    }
}
