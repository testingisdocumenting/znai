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
import org.testingisdocumenting.znai.extensions.*;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.stream.Stream;

public class OcamlCommentIncludePlugin implements IncludePlugin {
    protected static final String COMMENT_LINE_KEY = "commentLine";
    private Path ocamlPath;
    private MarkupParserResult parserResult;

    @Override
    public String id() {
        return "ocaml-comment";
    }

    @Override
    public IncludePlugin create() {
        return new OcamlCommentIncludePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        PluginParamsDefinition params = new PluginParamsDefinition();
        params.add(COMMENT_LINE_KEY, PluginParamType.STRING, "text within a comment to match and identify the ocaml comment block", "\"to use this function\"");

        return params;
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        String fileName = pluginParams.getFreeParam();
        ocamlPath = componentsRegistry.resourceResolver().fullPath(fileName);
        String text = componentsRegistry.resourceResolver().textContent(fileName);
        String commentLine = pluginParams.getOpts().getRequiredString(COMMENT_LINE_KEY);

        parserResult = new OcamlCommentExtractor(text).extractCommentBlockAsDocElements(componentsRegistry, markupPath, commentLine);
        return PluginResult.docElements(parserResult.docElement().getContent().stream());
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(parserResult.getAllText());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(ocamlPath));
    }
}