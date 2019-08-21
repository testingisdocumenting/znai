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

package com.twosigma.znai.gen;

import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.Paths;

class CliConfig {
    private static final String SECTION_ID = "sectionId";
    private static final String DEST = "znaiDest";
    private static final String ROOT = "readmeRoot";
    private static final String HELP = "help";

    private final CommandLine commandLine;
    private Path readmeRoot;
    private Path znaiDest;
    private String sectionId;

    CliConfig(String[] args) {
        Options options = createOptions();
        commandLine = createCommandLine(args, options);

        if (commandLine.hasOption(HELP) || (args.length < 3)) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("znai-gen", options);
            System.exit(1);
        }

        readmeRoot = retrievePath(ROOT);
        znaiDest = retrievePath(DEST);
        sectionId = commandLine.getOptionValue(SECTION_ID);
    }

    String getSectionId() {
        return sectionId;
    }

    Path getReadmeRoot() {
        return readmeRoot;
    }

    Path getZnaiDest() {
        return znaiDest;
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
        options.addOption(null, SECTION_ID, true, "znai section id where to put content");

        return options;
    }
}
