/*
 * Copyright 2021 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.cli;

import org.testingisdocumenting.znai.cli.extension.CliCommandHandler;
import org.testingisdocumenting.znai.cli.extension.CliCommandHandlers;
import org.testingisdocumenting.znai.client.DeployTempDir;
import org.testingisdocumenting.znai.console.ConsoleOutputs;
import org.testingisdocumenting.znai.console.ansi.Color;
import org.testingisdocumenting.znai.parser.MarkupTypes;
import org.apache.commons.cli.*;
import org.testingisdocumenting.znai.server.SslConfig;
import org.testingisdocumenting.znai.version.ZnaiVersion;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ZnaiCliConfig {
    private static final String PREVIEW_KEY = "preview";
    private static final String SERVE_KEY = "serve";
    private static final String GENERATE_KEY = "new";
    private static final String EXPORT_KEY = "export";
    private static final String DEPLOY_KEY = "deploy";
    private static final String MODIFIED_TIME_KEY = "modified-time";
    private static final String DOC_ID_KEY = "doc-id";
    private static final String SOURCE_KEY = "source";
    private static final String MARKUP_TYPE_KEY = "markup-type";
    private static final String HOST_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String ACTOR_KEY = "actor";
    private static final String LOOKUP_PATHS_KEY = "lookup-paths";
    private static final String VALIDATE_EXTERNAL_LINKS_KEY = "validate-external-links";
    private static final String SSL_JKS_PATH_KEY = "jks-path";
    private static final String SSL_JSK_PASSWORD_KEY = "jks-password";
    private static final String SSL_PEM_CERT_PATH_KEY = "pem-cert-path";
    private static final String SSL_PEM_KEY_PATH_KEY = "pem-key-path";

    private static final String HELP_KEY = "help";
    private static final String VERSION_KEY = "version";

    private static final String PREVIEW_COMMAND = "preview";
    private static final String SERVE_COMMAND = "serve";
    private static final String NEW_COMMAND = "new";
    private static final String EXPORT_COMMAND = "export";
    private static final String BUILD_COMMAND = "build";

    private static final int DEFAULT_PORT = 3333;

    public enum Mode {
        BUILD("build"),
        PREVIEW("preview"),
        SERVE("serve"),
        EXPORT("export"),
        SCAFFOLD("scaffold new"),
        CUSTOM("custom");

        private final String label;

        Mode(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public enum ModifiedTimeStrategy {
        FILE,
        CONSTANT,
        NA
    }

    private String docId;
    private String markupType;
    private String host;
    private Path deployRoot;
    private Path exportRoot;
    private Path sourceRoot;
    private boolean isSourceRootSet;
    private Integer port;
    private Mode mode;
    private List<String> specifiedCustomCommands;
    private String actor;
    private List<String> lookupPaths;

    private boolean isValidateExternalLinks;
    private String jksPath;
    private String jksPassword;
    private String pemCertPath;
    private String pemKeyPath;

    private ModifiedTimeStrategy modifiedTimeStrategy;
    private boolean isLegacyMode = false;
    private String commandName = null;

    public ZnaiCliConfig(String... args) {
        parseArgs(args);
    }

    public String getModeAsString() {
        return mode.getLabel();
    }

    public CliCommandHandler getSpecifiedCustomCommand() {
        return CliCommandHandlers.findByCommand(getSpecifiedCustomCommandName());
    }

    private String getSpecifiedCustomCommandName() {
        return specifiedCustomCommands.get(0);
    }

    public boolean isScaffoldMode() {
        return mode == Mode.SCAFFOLD;
    }

    public boolean isPreviewMode() {
        return mode == Mode.PREVIEW;
    }

    public boolean isValidateExternalLinks() {
        return isValidateExternalLinks;
    }

    public boolean isServeMode() {
        return mode == Mode.SERVE;
    }

    public boolean isGenerateOnlyMode() {
        return mode == Mode.BUILD;
    }

    public boolean isExportMode() {
        return mode == Mode.EXPORT;
    }

    public boolean isCustomCommand() {
        return !specifiedCustomCommands.isEmpty();
    }

    public String getMarkupType() {
        return markupType;
    }

    public Path getSourceRoot() {
        return validateIsSet("sourceRoot", sourceRoot);
    }

    public boolean isSourceRootSet() {
        return isSourceRootSet;
    }

    public Path getExportRoot() {
        return exportRoot;
    }

    public Integer getPort() {
        return port;
    }

    public SslConfig createSslConfig() {
        return new SslConfig(jksPath, jksPassword, pemCertPath, pemKeyPath);
    }

    public String getHost() {
        return host;
    }

    public String getDocId() {
        return docId;
    }

    public String getActor() {
        return actor;
    }

    public void setSourceRoot(Path sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    public void setDeployRoot(Path deployRoot) {
        this.deployRoot = deployRoot;
    }

    public List<String> getLookupPaths() {
        return lookupPaths;
    }

    public ModifiedTimeStrategy getModifiedTimeStrategy() {
        return modifiedTimeStrategy;
    }

    public Path getDeployRoot() {
        return validateIsSet("deployRoot", deployRoot);
    }

    public void print() {
        printVersion();

        if (!isScaffoldMode() && !isServeMode()) {
            print("source root", sourceRoot);
        }

        if (isPreviewMode() || isServeMode()) {
            print("deploy root", deployRoot);
            print("       host", host);
            print("       port", port);
        }

        if (isGenerateOnlyMode()) {
            print("     doc id", docId);
        }

        if (isExportMode()) {
            print("export root", exportRoot);
        }
    }

    @Override
    public String toString() {
        return "deployRoot: " + deployRoot + "\n";
    }

    private void parseArgs(String[] args) {
        CommandParseResult parseResult = detectCommandMode(args);
        isLegacyMode = parseResult.isLegacy;
        commandName = parseResult.command;
        String[] effectiveArgs = parseResult.remainingArgs;

        Options options = isLegacyMode ? createLegacyOptions() : createOptionsForCommand(commandName);
        CommandLine commandLine = createCommandLine(effectiveArgs, options);

        if (commandLine.hasOption(HELP_KEY) || (args.length < 1 && !isLegacyMode)) {
            printHelp(commandName, options);
            System.exit(1);
        }

        if (commandLine.hasOption(VERSION_KEY)) {
            printVersion();
            System.exit(1);
        }

        if (isLegacyMode && (commandLine.hasOption(PREVIEW_KEY) || commandLine.hasOption(SERVE_KEY) ||
                commandLine.hasOption(GENERATE_KEY) || commandLine.hasOption(EXPORT_KEY))) {
            showDeprecationWarning(commandLine);
        }

        port = commandLine.hasOption(PORT_KEY) ? Integer.parseInt(commandLine.getOptionValue(PORT_KEY)) : DEFAULT_PORT;
        mode = determineMode(commandLine);
        specifiedCustomCommands = CliCommandHandlers.registeredCommandNames()
                .filter(commandLine::hasOption)
                .collect(Collectors.toList());

        docId = commandLine.hasOption(DOC_ID_KEY) ? commandLine.getOptionValue(DOC_ID_KEY) : "no-id-specified";
        host = commandLine.hasOption(HOST_KEY) ? commandLine.getOptionValue(HOST_KEY) : "localhost";

        markupType = commandLine.hasOption(MARKUP_TYPE_KEY) ? commandLine.getOptionValue(MARKUP_TYPE_KEY) : MarkupTypes.MARKDOWN;

        isValidateExternalLinks = commandLine.hasOption(VALIDATE_EXTERNAL_LINKS_KEY);

        jksPath = commandLine.getOptionValue(SSL_JKS_PATH_KEY);
        jksPassword = commandLine.getOptionValue(SSL_JSK_PASSWORD_KEY);
        pemCertPath = commandLine.getOptionValue(SSL_PEM_CERT_PATH_KEY);
        pemKeyPath = commandLine.getOptionValue(SSL_PEM_KEY_PATH_KEY);

        isSourceRootSet = commandLine.hasOption(SOURCE_KEY);
        sourceRoot = Paths.get(isSourceRootSet ? commandLine.getOptionValue(SOURCE_KEY) : "")
                .toAbsolutePath();

        deployRoot = (commandLine.hasOption(DEPLOY_KEY) ? Paths.get(commandLine.getOptionValue(DEPLOY_KEY)) :
                DeployTempDir.prepare(getModeAsString(), port)).toAbsolutePath();

        // For export mode, handle directory parameter
        if (mode == Mode.EXPORT) {
            if (commandLine.hasOption(EXPORT_KEY)) {
                exportRoot = Paths.get(commandLine.getOptionValue(EXPORT_KEY));
            } else {
                // For command mode, use the first remaining arg as export directory
                String[] remainingArgs = commandLine.getArgs();
                if (remainingArgs.length > 0) {
                    exportRoot = Paths.get(remainingArgs[0]);
                } else {
                    exportRoot = Paths.get("");
                }
            }
        } else {
            exportRoot = commandLine.hasOption(EXPORT_KEY) ?
                    Paths.get(commandLine.getOptionValue(EXPORT_KEY)):
                    Paths.get("");
        }

        actor = commandLine.hasOption(ACTOR_KEY) ? commandLine.getOptionValue(ACTOR_KEY) : "";

        lookupPaths = commandLine.hasOption(LOOKUP_PATHS_KEY) ?
                Arrays.asList(commandLine.getOptionValues(LOOKUP_PATHS_KEY)):
                Collections.emptyList();

        modifiedTimeStrategy = determineModifiedTimeStrategy(commandLine);

        validateMode(commandLine);
    }

    private void printVersion() {
        ConsoleOutputs.out(Color.YELLOW, "znai version: ", Color.CYAN, ZnaiVersion.getVersion(),
                Color.GREEN, " (", ZnaiVersion.getTimeStamp(), ")");
    }

    private Mode determineMode(CommandLine commandLine) {
        if (!isLegacyMode && commandName != null) {
            switch (commandName) {
                case PREVIEW_COMMAND:
                    return Mode.PREVIEW;
                case SERVE_COMMAND:
                    return Mode.SERVE;
                case NEW_COMMAND:
                    return Mode.SCAFFOLD;
                case EXPORT_COMMAND:
                    return Mode.EXPORT;
                case BUILD_COMMAND:
                    return Mode.BUILD;
            }
        }

        if (commandLine.hasOption(PREVIEW_KEY)) {
            return Mode.PREVIEW;
        }

        if (commandLine.hasOption(SERVE_KEY)) {
            return Mode.SERVE;
        }

        if (commandLine.hasOption(GENERATE_KEY)) {
            return Mode.SCAFFOLD;
        }

        if (commandLine.hasOption(EXPORT_KEY)) {
            return Mode.EXPORT;
        }

        if (specifiedCustomCommands != null && !specifiedCustomCommands.isEmpty()) {
            return Mode.CUSTOM;
        }

        return Mode.BUILD;
    }

    private ModifiedTimeStrategy determineModifiedTimeStrategy(CommandLine commandLine) {
        if (!commandLine.hasOption(MODIFIED_TIME_KEY)) {
            return ModifiedTimeStrategy.NA;
        }

        String modifiedTime = commandLine.getOptionValue(MODIFIED_TIME_KEY);
        return switch (modifiedTime) {
            case "constant" -> ModifiedTimeStrategy.CONSTANT;
            case "file" -> ModifiedTimeStrategy.FILE;
            default ->
                    throw new IllegalArgumentException("unsupported " + MODIFIED_TIME_KEY + " value: " + modifiedTime);
        };
    }

    private void validateMode(CommandLine commandLine) {
        long activeModesCount = Stream.of(PREVIEW_KEY, SERVE_KEY, GENERATE_KEY, EXPORT_KEY)
                .filter(commandLine::hasOption)
                .count() + specifiedCustomCommands.size();

        if (activeModesCount > 1) {
            throw new RuntimeException("only one mode can be active");
        }
    }

    private CommandLine createCommandLine(String[] args, Options options) {
        DefaultParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            ConsoleOutputs.out(Color.RED, e.getMessage());
            ConsoleOutputs.out(Color.BLUE, "use --help to list all available parameters");
            System.exit(2);

            return null;
        }
    }

    private <E> E validateIsSet(String name, E v) {
        if (v == null) {
            throw new RuntimeException("<" + name + "> is not set. specify it using args or a configuration set method");
        }

        return v;
    }

    private Options createLegacyOptions() {
        Options options = createCommonOptions();

        options.addOption(null, PREVIEW_KEY, false, "[DEPRECATED] preview mode (use 'znai preview' instead)");
        options.addOption(null, SERVE_KEY, false, "[DEPRECATED] server mode (use 'znai serve' instead)");
        options.addOption(null, EXPORT_KEY, true, "[DEPRECATED] export documentation (use 'znai export' instead)");
        options.addOption(null, GENERATE_KEY, false, "[DEPRECATED] create new documentation (use 'znai new' instead)");

        return options;
    }

    private Options createOptionsForCommand(String command) {
        Options options = createCommonOptions();

        if (EXPORT_COMMAND.equals(command)) {
            options.addOption(null, EXPORT_KEY, true, "export destination directory");
        }

        return options;
    }

    private Options createCommonOptions() {
        Options options = new Options();
        options.addOption(null, HELP_KEY, false, "print help");
        options.addOption(null, VERSION_KEY, false, "print version");
        options.addOption(null, PORT_KEY, true, "server port");
        options.addOption(null, HOST_KEY, true, "server host");
        options.addOption(null, MARKUP_TYPE_KEY, true, "markup type");
        options.addOption(null, VALIDATE_EXTERNAL_LINKS_KEY, false, "validate external links");
        options.addOption(null, SOURCE_KEY, true, "documentation source dir");
        options.addOption(null, DOC_ID_KEY, true, "documentation id");
        options.addOption(null, DEPLOY_KEY, true, "documentation deploy root dir");
        options.addOption(null, ACTOR_KEY, true, "actor name");
        options.addOption(null, MODIFIED_TIME_KEY, true,
                "strategy of modified time for each page: constant or file last update time: constant, file (default)");
        options.addOption(null, SSL_JKS_PATH_KEY, true,
                "path to JKS cert. when specified SSL will be enabled for preview server");
        options.addOption(null, SSL_JSK_PASSWORD_KEY, true,
                "JSK cert password");
        options.addOption(null, SSL_PEM_CERT_PATH_KEY, true,
                "path to PEM cert. when specified SSL will be enabled for preview server");
        options.addOption(null, SSL_PEM_KEY_PATH_KEY, true,
                "path to key pem file. when specified SSL will be enabled for preview server");

        Option lookupPaths = Option.builder()
                .desc("additional lookup paths separated by colon(:)")
                .longOpt(LOOKUP_PATHS_KEY)
                .hasArgs()
                .build();
        options.addOption(lookupPaths);

        CliCommandHandlers.forEach(h -> options.addOption(null, h.commandName(), false, h.description()));

        return options;
    }

    private void print(String name, Object value) {
        ConsoleOutputs.out(Color.BLUE, name, ": ", Color.YELLOW, value);
    }

    private static class CommandParseResult {
        String command;
        String[] remainingArgs;
        boolean isLegacy;

        CommandParseResult(String command, String[] remainingArgs, boolean isLegacy) {
            this.command = command;
            this.remainingArgs = remainingArgs;
            this.isLegacy = isLegacy;
        }
    }

    private CommandParseResult detectCommandMode(String[] args) {
        if (args.length == 0) {
            return new CommandParseResult(null, args, true);
        }

        String firstArg = args[0];

        if (firstArg.startsWith("--") || firstArg.startsWith("-")) {
            return new CommandParseResult(null, args, true);
        }

        if (PREVIEW_COMMAND.equals(firstArg) || SERVE_COMMAND.equals(firstArg) ||
            NEW_COMMAND.equals(firstArg) || EXPORT_COMMAND.equals(firstArg) ||
            BUILD_COMMAND.equals(firstArg)) {
            String[] remainingArgs = new String[args.length - 1];
            System.arraycopy(args, 1, remainingArgs, 0, args.length - 1);
            return new CommandParseResult(firstArg, remainingArgs, false);
        }

        if (CliCommandHandlers.registeredCommandNames().anyMatch(firstArg::equals)) {
            String[] remainingArgs = new String[args.length - 1];
            System.arraycopy(args, 1, remainingArgs, 0, args.length - 1);
            return new CommandParseResult(firstArg, remainingArgs, false);
        }

        return new CommandParseResult(BUILD_COMMAND, args, false);
    }

    private void showDeprecationWarning(CommandLine commandLine) {
        ConsoleOutputs.out(Color.YELLOW, "\nWarning: Using deprecated command-line format.");

        if (commandLine.hasOption(PREVIEW_KEY)) {
            ConsoleOutputs.out(Color.YELLOW, "Please use 'znai preview' instead of 'znai --preview'");
        } else if (commandLine.hasOption(SERVE_KEY)) {
            ConsoleOutputs.out(Color.YELLOW, "Please use 'znai serve' instead of 'znai --serve'");
        } else if (commandLine.hasOption(GENERATE_KEY)) {
            ConsoleOutputs.out(Color.YELLOW, "Please use 'znai new' instead of 'znai --new'");
        } else if (commandLine.hasOption(EXPORT_KEY)) {
            ConsoleOutputs.out(Color.YELLOW, "Please use 'znai export' instead of 'znai --export'");
        }

        ConsoleOutputs.out(Color.YELLOW, "The old format will be removed in a future version.\n");
    }

    private void printHelp(String command, Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        printVersion();

        if (!isLegacyMode) {
            ConsoleOutputs.out(Color.CYAN, "\nUsage:");
            ConsoleOutputs.out("  znai [command] [options]\n");
            ConsoleOutputs.out(Color.CYAN, "Commands:");
            ConsoleOutputs.out("  preview   ", Color.WHITE, "Preview mode with hot reload");
            ConsoleOutputs.out("  serve     ", Color.WHITE, "Serve static documentation");
            ConsoleOutputs.out("  new       ", Color.WHITE, "Create new documentation with minimal necessary files");
            ConsoleOutputs.out("  export    ", Color.WHITE, "Export documentation source including required artifacts");
            ConsoleOutputs.out("  build     ", Color.WHITE, "Build documentation (default)\n");

            if (command != null) {
                ConsoleOutputs.out(Color.CYAN, "Options for 'znai " + command + "':");
                helpFormatter.printHelp("znai " + command + " [options]", options);
            } else {
                ConsoleOutputs.out(Color.CYAN, "\nFor command-specific options, use:");
                ConsoleOutputs.out("  znai [command] --help\n");
            }
        } else {
            helpFormatter.printHelp("znai", options);
        }
    }
}
