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
    private static final String VALIDATE_EXTERNAL_LINKS = "validate-external-links";

    private static final String HELP_KEY = "help";
    private static final String VERSION_KEY = "version";

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

    private ModifiedTimeStrategy modifiedTimeStrategy;

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
        Options options = createOptions();
        CommandLine commandLine = createCommandLine(args, options);

        if (commandLine.hasOption(HELP_KEY) || args.length < 1) {
            HelpFormatter helpFormatter = new HelpFormatter();
            printVersion();
            helpFormatter.printHelp("znai", options);
            System.exit(1);
        }

        if (commandLine.hasOption(VERSION_KEY)) {
            printVersion();
            System.exit(1);
        }

        port = commandLine.hasOption(PORT_KEY) ? Integer.parseInt(commandLine.getOptionValue(PORT_KEY)) : DEFAULT_PORT;
        mode = determineMode(commandLine);
        specifiedCustomCommands = CliCommandHandlers.registeredCommandNames()
                .filter(commandLine::hasOption)
                .collect(Collectors.toList());

        docId = commandLine.hasOption(DOC_ID_KEY) ? commandLine.getOptionValue(DOC_ID_KEY) : "no-id-specified";
        host = commandLine.hasOption(HOST_KEY) ? commandLine.getOptionValue(HOST_KEY) : "localhost";

        markupType = commandLine.hasOption(MARKUP_TYPE_KEY) ? commandLine.getOptionValue(MARKUP_TYPE_KEY) : MarkupTypes.MARKDOWN;

        isValidateExternalLinks = commandLine.hasOption(VALIDATE_EXTERNAL_LINKS);

        isSourceRootSet = commandLine.hasOption(SOURCE_KEY);
        sourceRoot = Paths.get(isSourceRootSet ? commandLine.getOptionValue(SOURCE_KEY) : "")
                .toAbsolutePath();

        deployRoot = (commandLine.hasOption(DEPLOY_KEY) ? Paths.get(commandLine.getOptionValue(DEPLOY_KEY)) :
                DeployTempDir.prepare(getModeAsString(), port)).toAbsolutePath();

        exportRoot = commandLine.hasOption(EXPORT_KEY) ?
                Paths.get(commandLine.getOptionValue(EXPORT_KEY)):
                Paths.get("");

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
        switch (modifiedTime) {
            case "constant":
                return ModifiedTimeStrategy.CONSTANT;
            case "file":
                return ModifiedTimeStrategy.FILE;
            default:
                throw new IllegalArgumentException("unsupported " + MODIFIED_TIME_KEY + " value: " + modifiedTime);
        }
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

    private Options createOptions() {
        Options options = new Options();
        options.addOption(null, HELP_KEY, false, "print help");
        options.addOption(null, VERSION_KEY, false, "print version");
        options.addOption(null, PORT_KEY, true, "server port");
        options.addOption(null, HOST_KEY, true, "server host");
        options.addOption(null, MARKUP_TYPE_KEY, true, "markup type");
        options.addOption(null, VALIDATE_EXTERNAL_LINKS, false, "validate external links");
        options.addOption(null, SOURCE_KEY, true, "documentation source dir");
        options.addOption(null, DOC_ID_KEY, true, "documentation id");
        options.addOption(null, MODIFIED_TIME_KEY, true,
                "strategy of modified time for each page: constant or file last update time: constant, file (default)");

        Option lookupPaths = Option.builder()
                .desc("additional lookup paths separated by colon(:)")
                .longOpt(LOOKUP_PATHS_KEY)
                .hasArgs()
                .build();
        options.addOption(lookupPaths);

        options.addOption(null, DEPLOY_KEY, true, "documentation deploy root dir");
        options.addOption(null, PREVIEW_KEY, false, "preview mode");
        options.addOption(null, SERVE_KEY, false, "server mode");
        options.addOption(null, EXPORT_KEY, true, "export documentation source including required artifacts");
        options.addOption(null, GENERATE_KEY, false, "create new documentation with minimal necessary files");
        CliCommandHandlers.forEach(h -> options.addOption(null, h.commandName(), false, h.description()));

        return options;
    }

    private void print(String name, Object value) {
        ConsoleOutputs.out(Color.BLUE, name, ": ", Color.YELLOW, value);
    }
}
