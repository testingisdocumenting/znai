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

package org.testingisdocumenting.znai.extensions.ocaml;

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
        OcamlCommentExtractor extractor = new OcamlCommentExtractor(text);
        String rawComment = extractor.extractCommentBlock(commentLine);
        return cleanCommentBlock(rawComment);
    }

    private String cleanCommentBlock(String rawComment) {
        String[] lines = rawComment.split("\n");
        StringBuilder result = new StringBuilder();
        
        for (String line : lines) {
            String cleaned = cleanCommentLine(line);
            if (!cleaned.isEmpty()) {
                result.append(cleaned).append("\n");
            }
        }
        
        return result.toString().trim();
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