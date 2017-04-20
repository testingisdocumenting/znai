package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.AnsiConsoleOutput
import com.twosigma.console.ansi.Color
import com.twosigma.testing.standalone.StandaloneTest
import com.twosigma.testing.standalone.StandaloneTestListener
import com.twosigma.testing.standalone.StandaloneTestRunner

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
        runner = new StandaloneTestRunner(["com.twosigma.testing.webui.WebTestDsl"], this)
    }

    void start() {
        ConsoleOutputs.add(new AnsiConsoleOutput());

        config.print()

        testFiles().forEach {
            runner.process(it)
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
    void afterTest(StandaloneTest test) {
        if (test.isFailed()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "failed")
            ConsoleOutputs.out(test.assertionMessage)
        } else if (test.hasError()) {
            ConsoleOutputs.out(Color.RED, "[x] ", Color.BLUE, "error")
            ConsoleOutputs.out(test.exception)
        }
    }
}
