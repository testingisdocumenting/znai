package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.AnsiConsoleOutput
import com.twosigma.testing.standalone.StandaloneTestRunner
import com.twosigma.testing.standalone.report.StandardConsoleTestReporter

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
        runner = new StandaloneTestRunner(["com.twosigma.testing.webui.WebTestDsl"])
        runner.addListener(new StandardConsoleTestReporter())
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
}
