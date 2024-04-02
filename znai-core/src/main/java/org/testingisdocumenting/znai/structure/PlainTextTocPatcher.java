/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.structure;

import java.util.Arrays;

public class PlainTextTocPatcher {
    private final TableOfContents toc;

    public PlainTextTocPatcher(TableOfContents toc) {
        this.toc = toc;
    }

    public void patch(String patchInstructions) {
        Arrays.stream(patchInstructions.split("\n"))
                .map(String::trim)
                .forEach(this::apply);
    }

    private void apply(String instruction) {
        String[] commandAndArgs = instruction.split("\\s+");
        if (commandAndArgs.length < 2) {
            throw new IllegalArgumentException("wrong patch file format. \n" +
                    "expect: command arg1 [arg2]\n" +
                    "received: " + instruction);
        }

        String command = commandAndArgs[0];
        DirNameFileName arg1 = new DirNameFileName(commandAndArgs[1]);
        DirNameFileName arg2 = new DirNameFileName(commandAndArgs.length > 2 ? commandAndArgs[2] : null);

        apply(command, arg1, arg2);
    }

    private void apply(String command, DirNameFileName arg1, DirNameFileName arg2) {
        TocNameAndOpts chapter1 = new TocNameAndOpts(arg1.dirName);
        TocNameAndOpts page1 = new TocNameAndOpts(arg1.fileName);
        TocNameAndOpts chapter2 = new TocNameAndOpts(arg2.dirName);
        TocNameAndOpts page2 = new TocNameAndOpts(arg2.fileName);

        switch (command) {
            case "remove" -> toc.removeTocItem(arg1.dirName, arg1.fileName);
            case "add" -> toc.addTocItem(chapter1, page1);
            case "replace" -> toc.replaceTocItem(arg1.dirName, arg1.fileName,
                    chapter2, page2);
            default -> throw new IllegalArgumentException("unrecognized command: " + command);
        }
    }

    private static class DirNameFileName {
        final String dirName;
        final String fileName;

        DirNameFileName(String arg) {
            if (arg == null) {
                dirName = "";
                fileName = "";
                return;
            }

            String[] dirNameFileName = arg.split("/");
            if (dirNameFileName.length != 2) {
                throw new IllegalArgumentException("wrong command argument.\n" +
                        "expect: dir-name/file-name\n" +
                        "received: " + arg);
            }
            this.dirName = dirNameFileName[0];
            this.fileName = dirNameFileName[1];
        }
    }
}
