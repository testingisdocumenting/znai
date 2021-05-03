/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PythonDocIncludePlugin implements IncludePlugin {
    private Path fullPath;
    private MarkupParserResult docStringParserResult;

    @Override
    public String id() {
        return "python-doc";
    }

    @Override
    public IncludePlugin create() {
        return new PythonDocIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();

        String givenFilePath = pluginParams.getFreeParam();

        if (!resourcesResolver.canResolve(givenFilePath)) {
            throw new IllegalArgumentException("can't find file: " + givenFilePath);
        }

        fullPath = resourcesResolver.fullPath(givenFilePath);
        String entryName = pluginParams.getOpts().getRequiredString("entry");

        List<Map<String, Object>> pythonParseResult = PythonBasedPythonParser.INSTANCE.parse(fullPath);
        Map<String, Object> entry = pythonParseResult.stream().filter(e -> e.getOrDefault("name", "").equals(entryName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find entry: " + entryName +
                        " in: " + givenFilePath + ", available entries: " +
                        pythonParseResult.stream()
                                .map(e -> e.getOrDefault("name", "").toString())
                                .collect(Collectors.joining(", "))));

        docStringParserResult = componentsRegistry.markdownParser()
                .parse(markupPath, entry.get("doc_string").toString());

        return PluginResult.docElements(docStringParserResult.getDocElement().getContent().stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(docStringParserResult.getAllText());
    }
}
