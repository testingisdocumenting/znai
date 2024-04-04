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
import java.util.List;

public class PlainTextTocGenerator implements TocGenerator {
    public PlainTextTocGenerator(String defaultExtension) {
        this.defaultExtension = defaultExtension;
    }

    private final String defaultExtension;

    @Override
    public TableOfContents generate(String textContent) {
        return new Parser(defaultExtension, textContent).parse();
    }

    private static class Parser {
        public static final String INDENTATION = "    ";
        private final List<String> lines;
        private TocNameAndOpts currentChapter;
        private final TableOfContents toc;

        public Parser(String defaultExtension, String nestedText) {
            lines = Arrays.asList(nestedText.replace("\r", "").split("\n"));
            toc = new TableOfContents(defaultExtension);
        }

        public TableOfContents parse() {
            lines.forEach(this::parse);
            return toc;
        }

        private void parse(final String line) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty())
                return;

            if (line.startsWith(INDENTATION)) {
                handlePageEntry(trimmedLine);
            } else if (line.startsWith(" ")) {
                handleSyntaxError();
            } else {
                handleChapterEntry(trimmedLine);
            }
        }

        private void handleSyntaxError() {
            throw new IllegalArgumentException(
                    "toc line should either start with " + INDENTATION.length() + " spaces to denote " +
                            "page file name, or start without spaces to denote chapter dir name");
        }

        private void handleChapterEntry(final String trimmedLine) {
            currentChapter = new TocNameAndOpts(trimmedLine);
        }

        private void handlePageEntry(final String line) {
            if (currentChapter == null) {
                throw new IllegalArgumentException(
                        "chapter is not specified, use a line without indentation to specify a chapter");
            } else {
                toc.addTocItem(currentChapter, new TocNameAndOpts(line));
            }
        }
    }
}
