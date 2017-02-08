package com.twosigma.documentation.cli;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.documentation.client.DeployTempDir;
import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class CliConfig {
    private String docId;
    private String host;
    private Path deployRoot;
    private Path sourceRoot;
    private Integer port;
    private boolean isPreview;
    private boolean isUpload;
    private boolean isServe;

    public CliConfig(String... args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        Options options = createOptions();
        CommandLine commandLine = createCommandLine(args, options);

        if (commandLine.hasOption("help") || args.length < 1) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("mdoc", options);
            System.exit(1);
        }

        port = commandLine.hasOption("port") ? Integer.valueOf( commandLine.getOptionValue("port")) : 8080;
        isPreview = commandLine.hasOption("preview");
        isUpload = commandLine.hasOption("upload");
        isServe = commandLine.hasOption("serve");

        docId = commandLine.hasOption("doc-id") ? commandLine.getOptionValue("doc-id") : "no-id-specified";
        host = commandLine.hasOption("host") ? commandLine.getOptionValue("host") : "localhost";

        sourceRoot = Paths.get(commandLine.hasOption("source") ? commandLine.getOptionValue("source") : "")
                .toAbsolutePath();

        deployRoot = (commandLine.hasOption("deploy") ? Paths.get(commandLine.getOptionValue("deploy")) :
                DeployTempDir.prepare(getModeAsString()))
                .toAbsolutePath();

        validateMode();
    }

    public String getModeAsString() {
        if (isPreview()) {
            return "preview";
        }

        if (isUpload()) {
            return "upload";
        }

        if (isServe()) {
            return "serve";
        }

        return "";
    }

    public boolean isPreview() {
        return isPreview;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public boolean isServe() {
        return isServe;
    }

    private void validateMode() {
        long activeModesCount = Stream.of(isPreview, isServe, isUpload).filter(m -> m).count();
        if (activeModesCount > 1) {
            throw new RuntimeException("only one mode can be active");
        }
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

    public String getHost() {
        return host;
    }

    public String getDocId() {
        return docId;
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
        print("     doc id", docId);
        print("       host", host);
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
        options.addOption(null, "help", false, "print help");
        options.addOption(null, "port", true, "server port");
        options.addOption(null, "host", true, "server host");
        options.addOption(null, "source", true, "documentation source dir");
        options.addOption(null, "deploy", true, "documentation deploy root dir");
        options.addOption(null, "preview", false, "preview mode");
        options.addOption(null, "upload", false, "upload mode");
        options.addOption(null, "serve", false, "server mode");
        options.addOption(null, "doc-id", true, "documentation id");

        return options;
    }
}
