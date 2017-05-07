package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.Color
import com.twosigma.testing.webui.cfg.Configuration
import org.apache.commons.cli.*

/**
 * @author mykola
 */
class WebUiTestCliConfig {
    private Configuration cfg = Configuration.INSTANCE

    private List<String> testFiles

    WebUiTestCliConfig(String... args) {
        parseArgs(args)
    }

    List<String> getTestFiles() {
        return testFiles
    }

    void print() {
        def p = { k, v -> ConsoleOutputs.out(Color.BLUE, k, ": ", Color.YELLOW, v) }

        p(" base url", cfg.baseUrl);
    }

    private void parseArgs(String[] args) {
        Options options = createOptions();
        CommandLine commandLine = createCommandLine(args, options);

        if (commandLine.hasOption("help") || args.length < 1) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("webuit", options);
            System.exit(1);
        }

        def url = commandLine.getOptionValue("url")
        if (url != null) {
            cfg.baseUrl = url
        }

        testFiles = new ArrayList<>(commandLine.argList)
    }

    private static CommandLine createCommandLine(String[] args, Options options) {
        DefaultParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption(null, "help", false, "print help");
        options.addOption(null, "file", true, "test file");
        options.addOption(null, "url", true, "base url");

        return options;
    }
}
