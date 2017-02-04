package com.twosigma.documentation.server;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mykola
 */
public class ServerConfig {
    private Path deployRoot;
    private Path sourceRoot;
    private Integer port;

    public ServerConfig(String... args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        Options options = createOptions();
        CommandLine commandLine = createCommandLine(args, options);

        if (commandLine.hasOption("help")) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("mdoc-preview", options);
            System.exit(1);
        }

        sourceRoot = Paths.get(commandLine.hasOption("source") ? commandLine.getOptionValue("source") : ".")
                .toAbsolutePath();
        port = commandLine.hasOption("port") ? Integer.valueOf( commandLine.getOptionValue("port")) : 8080;
    }

    private CommandLine createCommandLine(String[] args, Options options) {
        DefaultParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getSourceRoot() {
        return validateIsSet("sourceRoot", sourceRoot);
    }

    public Integer getPort() {
        return port;
    }

    public void setSourceRoot(Path sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    public void setDeployRoot(Path deployRoot) {
        this.deployRoot = deployRoot;
    }

    public Path getDeployRoot() {
        return validateIsSet("deployRoot", deployRoot);
    }

    private <E> E validateIsSet(String name, E v) {
        if (v == null) {
            throw new RuntimeException("<" + name + "> is not set. specify it using args or a configuration set method");
        }

        return v;
    }

    public void print() {
        print("deploy root", deployRoot);
        print("source root", sourceRoot);
        print("       port", port);
    }

    private void print(String name, Object value) {
        ConsoleOutputs.out(Color.BLUE, name, ": ", Color.YELLOW, value);
    }

    @Override
    public String toString() {
        return "deployRoot: " + deployRoot + "\n";
    }

    private Options createOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "print help");
        options.addOption("p", "port", false, "server port");
        options.addOption("s", "source", true, "documentation source dir");

        return options;
    }
}
