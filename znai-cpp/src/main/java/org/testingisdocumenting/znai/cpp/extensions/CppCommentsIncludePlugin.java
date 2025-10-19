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

package org.testingisdocumenting.znai.cpp.extensions;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.cpp.parser.CodePart;
import org.testingisdocumenting.znai.cpp.parser.CppSourceCode;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CppCommentsIncludePlugin implements IncludePlugin {
    private static final String ZNAI_PREFIX = "@znai";

    private Path cppPath;
    private MarkupParserResult parserResult;

    @Override
    public String id() {
        return "cpp-comments";
    }

    @Override
    public IncludePlugin create() {
        return new CppCommentsIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        String fileName = pluginParams.getFreeParam();
        cppPath = componentsRegistry.resourceResolver().fullPath(fileName);
        String text = componentsRegistry.resourceResolver().textContent(fileName);

        String comments = extractComments(text, pluginParams.getOpts());
        parserResult = componentsRegistry.defaultParser().parse(cppPath, comments);
        return PluginResult.docElements(parserResult.docElement().getContent().stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(cppPath));
    }

    @Override
    public List<SearchText> textForSearch() {
        return parserResult != null ?
                List.of(SearchScore.STANDARD.text(parserResult.getAllText())) :
                List.of();
    }

    private String extractComments(String text, PluginParamsOpts opts) {
        String entry = opts.getRequiredString("entry");
        String body = CppSourceCode.entryBodyOnly(text, entry);

        List<CodePart> parts = CppSourceCode.splitOnComments(body);
        return parts.stream().filter(CodePart::isComment)
                .map(cp -> cp.getData().trim())
                .filter(c -> c.startsWith(ZNAI_PREFIX))
                .map(c -> c.replaceAll("^" + ZNAI_PREFIX, "").trim())
                .collect(Collectors.joining("\n\n"));
    }

}
