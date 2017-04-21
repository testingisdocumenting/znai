package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.Color
import org.apache.commons.cli.*

/**
 * @author mykola
 */
class WebUiTestCliConfig {
    private String baseUrl
    private String testFile

    WebUiTestCliConfig(String... args) {
        parseArgs(args)
    }

    String getBaseUrl() {
        return baseUrl
    }

    String getTestFile() {
        return testFile
    }

    void print() {
        def p = {k, v -> ConsoleOutputs.out(Color.BLUE, k, ": ", Color.YELLOW, v)}

        p(" base url", baseUrl);
        p("test file", testFile);
    }

    private void parseArgs(String[] args) {
        Options options = createOptions();
        CommandLine commandLine = createCommandLine(args, options);

        if (commandLine.hasOption("help") || args.length < 1) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("webuit", options);
            System.exit(1);
        }

        baseUrl = commandLine.getOptionValue("url");
        testFile = commandLine.getOptionValue("file");
    }

    private CommandLine createCommandLine(String[] args, Options options) {
        DefaultParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Options createOptions() {
        Options options = new Options();
        options.addOption(null, "help", false, "print help");
        options.addOption(null, "file", true, "test file");
        options.addOption(null, "url", true, "base url");

        return options;
    }
}
