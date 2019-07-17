package com.twosigma.znai.gen;

import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;

class CliConfig {
    private static final String SECTION_ID = "sectionId";
    private static final String DEST = "mdocDest";
    private static final String ROOT = "readmeRoot";
    private static final String HELP = "help";

    private final CommandLine commandLine;
    private Path readmeRoot;
    private Path mdocDest;
    private String sectionId;

    CliConfig(String[] args) {
        Options options = createOptions();
        commandLine = createCommandLine(args, options);

        if (commandLine.hasOption(HELP) || (args.length < 3)) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("mdoc-gen", options);
            System.exit(1);
        }

        readmeRoot = retrievePath(ROOT);
        mdocDest = retrievePath(DEST);
        sectionId = commandLine.getOptionValue(SECTION_ID);
    }

    String getSectionId() {
        return sectionId;
    }

    Path getReadmeRoot() {
        return readmeRoot;
    }

    Path getMdocDest() {
        return mdocDest;
    }

    private Path retrievePath(String optName) {
        return Paths.get(commandLine.getOptionValue(optName));
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
        options.addOption(null, HELP, false, "print help");
        options.addOption(null, ROOT, true, "location of the readme root dir");
        options.addOption(null, DEST, true, "output location");
        options.addOption(null, SECTION_ID, true, "mdoc section id where to put content");

        return options;
    }
}
