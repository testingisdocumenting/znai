package com.twosigma.testing.http.cli

import com.twosigma.testing.http.runner.HttpTestRunner

import java.nio.file.Paths

/**
 * @author mykola
 */
class CliMain {
    static void main(String[] args) {
        def cli = createCliBuilder()
        def options = cli.parse(args)

        if (!options || options.h) {
            cli.usage()
            System.exit(1)
        }

        runTests(options.arguments())
    }

    static void runTests(final List<String> testFiles) {
        def testRunner = new HttpTestRunner()
        testFiles.each {
            testRunner.register(Paths.get(it))
        }

        testRunner.run()
    }

    private static CliBuilder createCliBuilder() {
        def cli = new CliBuilder(usage: 'http-test test-file1 [test-file2...]\nruns http tests written in groovy')
        cli.with {
            h longOpt: 'help', 'show help'
        }

        return cli
    }
}
