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

package org.testingisdocumenting.znai.cpp.extensions;

import org.testingisdocumenting.znai.codesnippets.CodeSnippetsProps;
import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.cpp.parser.CodePart;
import org.testingisdocumenting.znai.cpp.parser.CppSourceCode;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsOpts;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CppIncludePlugin implements IncludePlugin {
    private MarkupParser markupParser;
    private Path cppPath;

    @Override
    public String id() {
        return "cpp";
    }

    @Override
    public IncludePlugin create() {
        return new CppIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        markupParser = componentsRegistry.defaultParser();
        String fileName = pluginParams.getFreeParam();
        cppPath = componentsRegistry.resourceResolver().fullPath(fileName);

        PluginParamsOpts opts = pluginParams.getOpts();
        String commentsType = opts.has("comments") ? opts.get("comments") : "";

        String text = componentsRegistry.resourceResolver().textContent(fileName);

        String snippet = extractSnippet(text, opts);

        List<CodePart> codeParts = commentsType.equals("inline") ?
                CppSourceCode.splitOnComments(snippet):
                Collections.singletonList(new CodePart(false, snippet));

        return PluginResult.docElements(codeParts.stream().flatMap(this::convertToDocElement));
    }

    private Stream<DocElement> convertToDocElement(CodePart codePart) {
        return codePart.isComment() ?
                parseComments(codePart.getData()) :
                createSnippet(codePart.getData());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(cppPath));
    }

    private Stream<DocElement> parseComments(String data) {
        MarkupParserResult parserResult = markupParser.parse(cppPath, data);
        return parserResult.docElement().getContent().stream();
    }

    private Stream<DocElement> createSnippet(String snippet) {
        DocElement docElement = new DocElement(DocElementType.SNIPPET);
        Map<String, Object> props = CodeSnippetsProps.create("cpp", snippet);
        docElement.addProps(props);

        return Stream.of(docElement);
    }

    private String extractSnippet(String text, PluginParamsOpts opts) {
        String entry = opts.get("entry");
        if (entry == null) {
            return text;
        }

        Boolean bodyOnly = opts.get("bodyOnly");
        return bodyOnly != null && bodyOnly ?
                CppSourceCode.entryBodyOnly(text, entry):
                CppSourceCode.entryDefinition(text, entry);
    }
}
