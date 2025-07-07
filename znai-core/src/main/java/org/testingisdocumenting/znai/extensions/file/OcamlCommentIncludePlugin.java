/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.extensions.file;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.stream.Stream;

public class OcamlCommentIncludePlugin implements IncludePlugin {
    private Path ocamlPath;

    @Override
    public String id() {
        return "ocaml-comment";
    }

    @Override
    public IncludePlugin create() {
        return new OcamlCommentIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        String fileName = pluginParams.getFreeParam();
        ocamlPath = componentsRegistry.resourceResolver().fullPath(fileName);
        String text = componentsRegistry.resourceResolver().textContent(fileName);

        String comments = extractComments(text, pluginParams.getOpts());
        MarkupParserResult parserResult = componentsRegistry.defaultParser().parse(ocamlPath, comments);
        return PluginResult.docElements(parserResult.docElement().getContent().stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(ocamlPath));
    }

    private String extractComments(String text, PluginParamsOpts opts) {
        String commentLine = opts.getRequiredString("commentLine");
        return extractCommentBlock(text, commentLine);
    }

    private String extractCommentBlock(String text, String commentLine) {
        String[] lines = text.split("\n");
        
        int startIndex = -1;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().equals(commentLine.trim())) {
                startIndex = i;
                break;
            }
        }
        
        if (startIndex == -1) {
            throw new RuntimeException("Comment line not found: " + commentLine);
        }

        int commentStart = findCommentStart(lines, startIndex);
        int commentEnd = findCommentEnd(lines, commentStart);
        
        if (commentStart == -1 || commentEnd == -1) {
            throw new RuntimeException("Could not find comment block boundaries for line: " + commentLine);
        }

        StringBuilder commentContent = new StringBuilder();
        for (int i = commentStart; i <= commentEnd; i++) {
            String line = lines[i];
            String cleanedLine = cleanCommentLine(line);
            if (!cleanedLine.isEmpty()) {
                commentContent.append(cleanedLine).append("\n");
            }
        }
        
        return commentContent.toString().trim();
    }

    private int findCommentStart(String[] lines, int startIndex) {
        for (int i = startIndex; i >= 0; i--) {
            String line = lines[i].trim();
            if (line.startsWith("(*")) {
                return i;
            }
            if (!line.isEmpty() && !isCommentLine(line)) {
                break;
            }
        }
        return -1;
    }

    private int findCommentEnd(String[] lines, int startIndex) {
        for (int i = startIndex; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.endsWith("*)")) {
                return i;
            }
            if (!line.isEmpty() && !isCommentLine(line)) {
                break;
            }
        }
        return -1;
    }

    private boolean isCommentLine(String line) {
        String trimmed = line.trim();
        return trimmed.startsWith("(*") || trimmed.startsWith("*") || trimmed.endsWith("*)") ||
               (!trimmed.isEmpty() && !trimmed.startsWith("(*") && !trimmed.endsWith("*)") &&
                trimmed.chars().allMatch(c -> Character.isWhitespace(c) || c == '*'));
    }

    private String cleanCommentLine(String line) {
        String trimmed = line.trim();
        
        if (trimmed.startsWith("(*") && trimmed.endsWith("*)")) {
            return trimmed.substring(2, trimmed.length() - 2).trim();
        }
        
        if (trimmed.startsWith("(*")) {
            return trimmed.substring(2).trim();
        }
        
        if (trimmed.endsWith("*)")) {
            return trimmed.substring(0, trimmed.length() - 2).trim();
        }
        
        if (trimmed.startsWith("*")) {
            return trimmed.substring(1).trim();
        }
        
        return trimmed;
    }
}