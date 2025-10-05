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
import org.apache.commons.cli.help.HelpFormatter;
import org.testingisdocumenting.znai.server.SslConfig;
import org.testingisdocumenting.znai.version.ZnaiVersion;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Set;
import java.util.EnumSet;
import java.util.Map;
import java.util.EnumMap;

public class ZnaiCliConfig {
    public enum Command {
        PREVIEW("preview", "Preview mode with hot reload"),
        SERVE("serve", "Serve static documentation"),
        NEW("new", "Create new documentation with minimal necessary files"),
        EXPORT("export", "Export documentation source including required artifacts"),
        BUILD("build", "Build documentation (default)");

        private final String name;
        private final String description;

        Command(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static Command fromName(String name) {
            for (Command cmd : values()) {
                if (cmd.name.equals(name)) {
                    return cmd;
                }
            }
            return null;
        }
    }

    public enum OptionKey {
        // Common options
        HELP("help", "print help", false, false),
        VERSION("version", "print version", false, false),
        SOURCE("source", "documentation source dir", true, false),
        MARKUP_TYPE("markup-type", "markup type", true, false),
        VALIDATE_EXTERNAL_LINKS("validate-external-links", "validate external links", false, false),
        ACTOR("actor", "actor name", true, false),
        MODIFIED_TIME("modified-time", "strategy of modified time for each page: constant or file last update time: constant, file (default)", true, false),
        LOOKUP_PATHS("lookup-paths", "additional lookup paths separated by colon(:)", true, true),

        // Server-related options
        HOST("host", "server host", true, false),
        PORT("port", "server port", true, false),
        DEPLOY("deploy", "documentation deploy root dir", true, false),
        SSL_JKS_PATH("jks-path", "path to JKS cert. when specified SSL will be enabled for preview server", true, false),
        SSL_JKS_PASSWORD("jks-password", "JSK cert password", true, false),
        SSL_PEM_CERT_PATH("pem-cert-path", "path to PEM cert. when specified SSL will be enabled for preview server", true, false),
        SSL_PEM_KEY_PATH("pem-key-path", "path to key pem file. when specified SSL will be enabled for preview server", true, false),

        // Build-specific options
        DOC_ID("doc-id", "documentation id", true, false),

        // Export-specific options
        EXPORT("export", "export destination directory", true, false),

        // Legacy options (deprecated)
        PREVIEW_LEGACY("preview", "[DEPRECATED] preview mode (use 'znai preview' instead)", false, false),
        SERVE_LEGACY("serve", "[DEPRECATED] server mode (use 'znai serve' instead)", false, false),
        NEW_LEGACY("new", "[DEPRECATED] create new documentation (use 'znai new' instead)", false, false);

        private final String key;
        private final String description;
        private final boolean hasArg;
        private final boolean hasMultipleArgs;

        OptionKey(String key, String description, boolean hasArg, boolean hasMultipleArgs) {
            this.key = key;
            this.description = description;
            this.hasArg = hasArg;
            this.hasMultipleArgs = hasMultipleArgs;
        }

        public String getKey() {
            return key;
        }

        public String getDescription() {
            return description;
        }

        public boolean hasArg() {
            return hasArg;
        }

        public boolean hasMultipleArgs() {
            return hasMultipleArgs;
        }
    }

    private static final Map<Command, Set<OptionKey>> COMMAND_OPTIONS = new EnumMap<>(Command.class);

    static {
        // Common options for all commands
        Set<OptionKey> commonOptions = EnumSet.of(
            OptionKey.HELP,
            OptionKey.VERSION,
            OptionKey.SOURCE,
            OptionKey.MARKUP_TYPE,
            OptionKey.VALIDATE_EXTERNAL_LINKS,
            OptionKey.ACTOR,
            OptionKey.MODIFIED_TIME,
            OptionKey.LOOKUP_PATHS
        );

        // Preview command options
        Set<OptionKey> previewOptions = EnumSet.copyOf(commonOptions);
        previewOptions.addAll(EnumSet.of(
            OptionKey.HOST,
            OptionKey.PORT,
            OptionKey.DEPLOY,
            OptionKey.SSL_JKS_PATH,
            OptionKey.SSL_JKS_PASSWORD,
            OptionKey.SSL_PEM_CERT_PATH,
            OptionKey.SSL_PEM_KEY_PATH
        ));
        COMMAND_OPTIONS.put(Command.PREVIEW, previewOptions);

        // Serve command options (same as preview)
        COMMAND_OPTIONS.put(Command.SERVE, previewOptions);

        // Build command options
        Set<OptionKey> buildOptions = EnumSet.copyOf(commonOptions);
        buildOptions.add(OptionKey.DOC_ID);
        buildOptions.add(OptionKey.DEPLOY);
        COMMAND_OPTIONS.put(Command.BUILD, buildOptions);

        // Export command options
        Set<OptionKey> exportOptions = EnumSet.copyOf(commonOptions);
        exportOptions.add(OptionKey.EXPORT);
        COMMAND_OPTIONS.put(Command.EXPORT, exportOptions);

        // New command options (just common options)
        COMMAND_OPTIONS.put(Command.NEW, commonOptions);
    }

    private static final int DEFAULT_PORT = 3333;
    private final Consumer<Integer> exit;

    private static Option createOptionFromKey(OptionKey key) {
        if (key.hasMultipleArgs()) {
            return Option.builder()
                    .desc(key.getDescription())
                    .longOpt(key.getKey())
                    .hasArgs()
                    .get();
        } else if (key.hasArg()) {
            return Option.builder()
                    .desc(key.getDescription())
                    .longOpt(key.getKey())
                    .hasArg()
                    .get();
        } else {
            return Option.builder()
                    .desc(key.getDescription())
                    .longOpt(key.getKey())
                    .get();
        }
    }

    private static Set<OptionKey> getOptionsForCommand(Command command) {
        return COMMAND_OPTIONS.getOrDefault(command, EnumSet.noneOf(OptionKey.class));
    }

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
    private Command command = null;

    public ZnaiCliConfig(Consumer<Integer> exit, String... args) {
        this.exit = exit;
        parseArgs(args);
    }

    public String getModeAsString() {
        return mode != null ? mode.getLabel() : "unknown";
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
        command = parseResult.command;
        String[] effectiveArgs = parseResult.remainingArgs;

        Options options = isLegacyMode ? createLegacyOptions() : createOptionsForCommand(command);
        CommandLine commandLine = createCommandLine(effectiveArgs, options);

        if (commandLine == null) {
            return; // Exit already called in createCommandLine
        }

        if (commandLine.hasOption(OptionKey.HELP.getKey()) || (args.length < 1 && !isLegacyMode)) {
            printHelp(command, options);
            exit.accept(1);
        }

        if (commandLine.hasOption(OptionKey.VERSION.getKey())) {
            printVersion();
            exit.accept(1);
        }

        if (isLegacyMode && (commandLine.hasOption(OptionKey.PREVIEW_LEGACY.getKey()) || commandLine.hasOption(OptionKey.SERVE_LEGACY.getKey()) ||
                commandLine.hasOption(OptionKey.NEW_LEGACY.getKey()) || commandLine.hasOption(OptionKey.EXPORT.getKey()))) {
            showDeprecationWarning(commandLine);
        }

        port = commandLine.hasOption(OptionKey.PORT.getKey()) ? Integer.parseInt(commandLine.getOptionValue(OptionKey.PORT.getKey())) : DEFAULT_PORT;
        mode = determineMode(commandLine);
        specifiedCustomCommands = CliCommandHandlers.registeredCommandNames()
                .filter(commandLine::hasOption)
                .collect(Collectors.toList());

        docId = commandLine.hasOption(OptionKey.DOC_ID.getKey()) ? commandLine.getOptionValue(OptionKey.DOC_ID.getKey()) : "no-id-specified";
        host = commandLine.hasOption(OptionKey.HOST.getKey()) ? commandLine.getOptionValue(OptionKey.HOST.getKey()) : "localhost";

        markupType = commandLine.hasOption(OptionKey.MARKUP_TYPE.getKey()) ? commandLine.getOptionValue(OptionKey.MARKUP_TYPE.getKey()) : MarkupTypes.MARKDOWN;

        isValidateExternalLinks = commandLine.hasOption(OptionKey.VALIDATE_EXTERNAL_LINKS.getKey());

        jksPath = commandLine.getOptionValue(OptionKey.SSL_JKS_PATH.getKey());
        jksPassword = commandLine.getOptionValue(OptionKey.SSL_JKS_PASSWORD.getKey());
        pemCertPath = commandLine.getOptionValue(OptionKey.SSL_PEM_CERT_PATH.getKey());
        pemKeyPath = commandLine.getOptionValue(OptionKey.SSL_PEM_KEY_PATH.getKey());

        isSourceRootSet = commandLine.hasOption(OptionKey.SOURCE.getKey());
        sourceRoot = Paths.get(isSourceRootSet ? commandLine.getOptionValue(OptionKey.SOURCE.getKey()) : "")
                .toAbsolutePath();

        deployRoot = (commandLine.hasOption(OptionKey.DEPLOY.getKey()) ? Paths.get(commandLine.getOptionValue(OptionKey.DEPLOY.getKey())) :
                DeployTempDir.prepare(getModeAsString(), port)).toAbsolutePath();

        Path defaultRoot = Paths.get("");
        if (mode == Mode.EXPORT) {
            if (commandLine.hasOption(OptionKey.EXPORT.getKey())) {
                exportRoot = Paths.get(commandLine.getOptionValue(OptionKey.EXPORT.getKey()));
            } else {
                // For command mode, use the first remaining arg as export directory
                String[] remainingArgs = commandLine.getArgs();
                if (remainingArgs.length > 0) {
                    exportRoot = Paths.get(remainingArgs[0]);
                } else {
                    exportRoot = defaultRoot;
                }
            }
        } else {
            exportRoot = commandLine.hasOption(OptionKey.EXPORT.getKey()) ?
                    Paths.get(commandLine.getOptionValue(OptionKey.EXPORT.getKey())):
                    defaultRoot;
        }

        actor = commandLine.hasOption(OptionKey.ACTOR.getKey()) ? commandLine.getOptionValue(OptionKey.ACTOR.getKey()) : "";

        lookupPaths = commandLine.hasOption(OptionKey.LOOKUP_PATHS.getKey()) ?
                Arrays.asList(commandLine.getOptionValues(OptionKey.LOOKUP_PATHS.getKey())):
                Collections.emptyList();

        modifiedTimeStrategy = determineModifiedTimeStrategy(commandLine);

        validateMode(commandLine);
    }

    private void printVersion() {
        ConsoleOutputs.out(Color.YELLOW, "znai version: ", Color.CYAN, ZnaiVersion.getVersion(),
                Color.GREEN, " (", ZnaiVersion.getTimeStamp(), ")");
    }

    private Mode determineMode(CommandLine commandLine) {
        if (!isLegacyMode && command != null) {
            return switch (command) {
                case PREVIEW -> Mode.PREVIEW;
                case SERVE -> Mode.SERVE;
                case NEW -> Mode.SCAFFOLD;
                case EXPORT -> Mode.EXPORT;
                case BUILD -> Mode.BUILD;
            };
        }

        if (commandLine.hasOption(OptionKey.PREVIEW_LEGACY.getKey())) {
            return Mode.PREVIEW;
        }

        if (commandLine.hasOption(OptionKey.SERVE_LEGACY.getKey())) {
            return Mode.SERVE;
        }

        if (commandLine.hasOption(OptionKey.NEW_LEGACY.getKey())) {
            return Mode.SCAFFOLD;
        }

        if (commandLine.hasOption(OptionKey.EXPORT.getKey())) {
            return Mode.EXPORT;
        }

        if (specifiedCustomCommands != null && !specifiedCustomCommands.isEmpty()) {
            return Mode.CUSTOM;
        }

        return Mode.BUILD;
    }

    private ModifiedTimeStrategy determineModifiedTimeStrategy(CommandLine commandLine) {
        if (!commandLine.hasOption(OptionKey.MODIFIED_TIME.getKey())) {
            return ModifiedTimeStrategy.NA;
        }

        String modifiedTime = commandLine.getOptionValue(OptionKey.MODIFIED_TIME.getKey());
        return switch (modifiedTime) {
            case "constant" -> ModifiedTimeStrategy.CONSTANT;
            case "file" -> ModifiedTimeStrategy.FILE;
            default ->
                    throw new IllegalArgumentException("unsupported " + OptionKey.MODIFIED_TIME.getKey() + " value: " + modifiedTime);
        };
    }

    private void validateMode(CommandLine commandLine) {
        long activeModesCount = Stream.of(OptionKey.PREVIEW_LEGACY.getKey(), OptionKey.SERVE_LEGACY.getKey(),
                OptionKey.NEW_LEGACY.getKey(), OptionKey.EXPORT.getKey())
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
            exit.accept(2);

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
        Options options = new Options();

        // Add all legacy options
        for (OptionKey key : OptionKey.values()) {
            options.addOption(createOptionFromKey(key));
        }

        // Add custom command options
        CliCommandHandlers.forEach(h -> options.addOption(null, h.commandName(), false, h.description()));

        return options;
    }

    protected Options createOptionsForCommand(Command command) {
        Options options = new Options();

        if (command != null) {
            Set<OptionKey> commandOptions = getOptionsForCommand(command);
            for (OptionKey key : commandOptions) {
                options.addOption(createOptionFromKey(key));
            }
        }

        // Add custom command options
        CliCommandHandlers.forEach(h -> options.addOption(null, h.commandName(), false, h.description()));

        return options;
    }



    private void print(String name, Object value) {
        ConsoleOutputs.out(Color.BLUE, name, ": ", Color.YELLOW, value);
    }

    private static class CommandParseResult {
        Command command;
        String[] remainingArgs;
        boolean isLegacy;

        CommandParseResult(Command command, String[] remainingArgs, boolean isLegacy) {
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

        Command cmd = Command.fromName(firstArg);
        if (cmd != null) {
            String[] remainingArgs = new String[args.length - 1];
            System.arraycopy(args, 1, remainingArgs, 0, args.length - 1);
            return new CommandParseResult(cmd, remainingArgs, false);
        }

        if (CliCommandHandlers.registeredCommandNames().anyMatch(firstArg::equals)) {
            String[] remainingArgs = new String[args.length - 1];
            System.arraycopy(args, 1, remainingArgs, 0, args.length - 1);
            return new CommandParseResult(null, remainingArgs, false);
        }

        return new CommandParseResult(Command.BUILD, args, false);
    }

    private void showDeprecationWarning(CommandLine commandLine) {
        ConsoleOutputs.out(Color.YELLOW, "\nWarning: Using deprecated command-line format.");

        if (commandLine.hasOption(OptionKey.PREVIEW_LEGACY.getKey())) {
            ConsoleOutputs.out(Color.YELLOW, "Please use 'znai preview' instead of 'znai --preview'");
        } else if (commandLine.hasOption(OptionKey.SERVE_LEGACY.getKey())) {
            ConsoleOutputs.out(Color.YELLOW, "Please use 'znai serve' instead of 'znai --serve'");
        } else if (commandLine.hasOption(OptionKey.NEW_LEGACY.getKey())) {
            ConsoleOutputs.out(Color.YELLOW, "Please use 'znai new' instead of 'znai --new'");
        } else if (commandLine.hasOption(OptionKey.EXPORT.getKey())) {
            ConsoleOutputs.out(Color.YELLOW, "Please use 'znai export' instead of 'znai --export'");
        }

        ConsoleOutputs.out(Color.YELLOW, "The old format will be removed in a future version.\n");
    }

    private void printHelp(Command command, Options options) {
        HelpFormatter helpFormatter = HelpFormatter.builder().get();
        printVersion();

        if (!isLegacyMode) {
            ConsoleOutputs.out(Color.CYAN, "\nUsage:");
            ConsoleOutputs.out("  znai [command] [options]\n");
            ConsoleOutputs.out(Color.CYAN, "Commands:");
            for (Command cmd : Command.values()) {
                ConsoleOutputs.out(String.format("  %-10s", cmd.getName()), Color.WHITE, cmd.getDescription());
            }
            ConsoleOutputs.out();

            if (command != null) {
                ConsoleOutputs.out(Color.CYAN, "Options for 'znai " + command.getName() + "':");
                try {
                    helpFormatter.printHelp("znai " + command.getName() + " [options]", "", options, "", true);
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                ConsoleOutputs.out(Color.CYAN, "\nFor command-specific options, use:");
                ConsoleOutputs.out("  znai [command] --help\n");
            }
        } else {
            try {
                helpFormatter.printHelp("znai", "", options, "", true);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
