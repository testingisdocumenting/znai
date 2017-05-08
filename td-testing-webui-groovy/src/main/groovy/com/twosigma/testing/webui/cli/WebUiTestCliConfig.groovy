package com.twosigma.testing.webui.cli

import com.twosigma.console.ConsoleOutputs
import com.twosigma.console.ansi.Color
import com.twosigma.testing.webui.cfg.WebUiTestConfig
import com.twosigma.utils.FileUtils
import org.apache.commons.cli.*

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author mykola
 */
class WebUiTestCliConfig {
    private WebUiTestConfig cfg = WebUiTestConfig.INSTANCE

    private List<String> testFiles
    private String env
    private Path configFile
    private CommandLine commandLine

    WebUiTestCliConfig(String... args) {
        parseArgs(args)
        parseConfig()
    }

    List<String> getTestFiles() {
        return testFiles
    }

    void print() {
        def p = { k, v -> ConsoleOutputs.out(Color.BLUE, k, ": ", Color.YELLOW, v) }

        p("         env", env)
        p(" config file", configFile)
        p("    base url", cfg.baseUrl)
    }

    private void parseArgs(String[] args) {
        Options options = createOptions()
        commandLine = createCommandLine(args, options)

        if (commandLine.hasOption("help") || args.length < 1) {
            HelpFormatter helpFormatter = new HelpFormatter()
            helpFormatter.printHelp("webuit", options)
            System.exit(1)
        }

        def url = commandLine.getOptionValue("url")
        if (url != null) {
            cfg.baseUrl = url
        }

        testFiles = new ArrayList<>(commandLine.argList)
        configFile = Paths.get(cliValue("config", "test.cfg"))
        env = Paths.get(cliValue("env", "local"))
    }

    private void parseConfig() {
        ConfigSlurper configSlurper = new ConfigSlurper(env)
        if (! Files.exists(configFile)) {
            ConsoleOutputs.out("skipping config file as it is not found: ", Color.CYAN, configFile)
            return
        }

        def configObject = configSlurper.parse(FileUtils.fileTextContent(configFile))
        if (configObject.get("url")) {
            cfg.setBaseUrl(configObject.get("url").toString())
        }
    }

    private static CommandLine createCommandLine(String[] args, Options options) {
        DefaultParser parser = new DefaultParser()
        try {
            return parser.parse(options, args)
        } catch (ParseException e) {
            throw new RuntimeException(e)
        }
    }

    private def cliValue(String name, defaultValue) {
        return commandLine.hasOption(name) ? commandLine.getOptionValue("config") :
                defaultValue
    }

    private static Options createOptions() {
        def options = new Options()
        options.addOption(null, "help", false, "print help")
        options.addOption(null, "file", true, "test file")
        options.addOption(null, "url", true, "base url")

        return options
    }
}
