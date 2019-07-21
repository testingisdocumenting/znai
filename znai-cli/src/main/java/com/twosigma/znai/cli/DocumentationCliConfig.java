/*
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

package com.twosigma.znai.cli;

import com.twosigma.console.ConsoleOutputs;
import com.twosigma.console.ansi.Color;
import com.twosigma.znai.cli.extension.CliCommandHandler;
import com.twosigma.znai.cli.extension.CliCommandHandlers;
import com.twosigma.znai.client.DeployTempDir;
import com.twosigma.znai.parser.MarkupTypes;
import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentationCliConfig {
    private String docId;
    private String markupType;
    private String host;
    private Path deployRoot;
    private Path sourceRoot;
    private Integer port;
    private boolean isPreview;
    private boolean isServe;
    private boolean isNew;
    private List<String> specifiedCustomCommands;

    public DocumentationCliConfig(String... args) {
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

        port = commandLine.hasOption("port") ? Integer.valueOf( commandLine.getOptionValue("port")) : 3333;
        isPreview = commandLine.hasOption("preview");
        isServe = commandLine.hasOption("serve");
        isNew = commandLine.hasOption("new");
        specifiedCustomCommands = CliCommandHandlers.registeredCommandNames()
                .filter(commandLine::hasOption)
                .collect(Collectors.toList());

        docId = commandLine.hasOption("doc-id") ? commandLine.getOptionValue("doc-id") : "no-id-specified";
        host = commandLine.hasOption("host") ? commandLine.getOptionValue("host") : "localhost";

        markupType = commandLine.hasOption("markup-type") ? commandLine.getOptionValue("markup-type") : MarkupTypes.MARKDOWN;

        sourceRoot = Paths.get(commandLine.hasOption("source") ? commandLine.getOptionValue("source") : "")
                .toAbsolutePath();

        deployRoot = (commandLine.hasOption("deploy") ? Paths.get(commandLine.getOptionValue("deploy")) :
                DeployTempDir.prepare(getModeAsString())).toAbsolutePath();

        validateMode();
    }

    public String getModeAsString() {
        if (isPreview()) {
            return "preview";
        }

        if (isServe()) {
            return "serve";
        }

        if (isCustom()) {
            return getSpecifiedCustomCommandName();
        }

        return "generate";
    }

    public CliCommandHandler getSpecifiedCustomCommand() {
        return CliCommandHandlers.findByCommand(getSpecifiedCustomCommandName());
    }

    private String getSpecifiedCustomCommandName() {
        return specifiedCustomCommands.get(0);
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isPreview() {
        return isPreview;
    }

    public boolean isServe() {
        return isServe;
    }

    public boolean isCustom() {
        return ! specifiedCustomCommands.isEmpty();
    }

    private void validateMode() {
        long activeModesCount = Stream.of(isPreview, isServe).filter(m -> m).count() +
                specifiedCustomCommands.size();
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

    public String getMarkupType() {
        return markupType;
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
        options.addOption(null, "markup-type", true, "markup type");
        options.addOption(null, "source", true, "documentation source dir");
        options.addOption(null, "doc-id", true, "documentation id");

        options.addOption(null, "deploy", true, "documentation deploy root dir");
        options.addOption(null, "preview", false, "preview mode");
        options.addOption(null, "serve", false, "server mode");
        options.addOption(null, "new", false, "create new documentation with minimal necessary files");
        CliCommandHandlers.forEach(h -> options.addOption(null, h.commandName(), false, h.description()));

        return options;
    }
}
