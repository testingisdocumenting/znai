/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.markup;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.Plugin;
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
import org.testingisdocumenting.znai.extensions.file.ManipulatedSnippetContentProvider;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.MarkupParserResult;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.search.SearchScore;
import org.testingisdocumenting.znai.search.SearchText;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public abstract class MarkdownBasePlugin implements Plugin {
    private static final String FIRST_AVAILABLE_PARAM_KEY = "firstAvailable";
    private static final String USAGE_MESSAGE = "use either <" + FIRST_AVAILABLE_PARAM_KEY + "> or free " +
            "form param to specify file to include";
    protected Path markdownPathUsed;
    protected MarkupParserResult parserResult;

    @Override
    public String id() {
        return "markdown";
    }

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition().add(FIRST_AVAILABLE_PARAM_KEY, PluginParamType.LIST_OR_SINGLE_STRING,
                        "path(s) of files to consider to include, first one will be included." +
                                " Use this to provide an alternative docs for on-prem documentation")
                .add(ManipulatedSnippetContentProvider.paramsDefinition);
    }

    @Override
    public void preprocess(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        MarkupParser parser = componentsRegistry.defaultParser();

        markdownPathUsed = selectMarkdown(componentsRegistry.resourceResolver(), pluginParams);
        String content = modifiedContent(markdownPathUsed.toString(),
                resourcesResolver.textContent(markdownPathUsed), pluginParams);
        parserResult = parser.parse(markupPath, content);

    }

    private String modifiedContent(String id, String fullContent, PluginParams pluginParams) {
        ManipulatedSnippetContentProvider contentProvider = new ManipulatedSnippetContentProvider(id,
                fullContent,
                pluginParams);

        return contentProvider.snippetContent();
    }

    private Path selectMarkdown(ResourcesResolver resourcesResolver, PluginParams pluginParams) {
        if (pluginParams.getOpts().has(FIRST_AVAILABLE_PARAM_KEY) && !pluginParams.getFreeParam().isEmpty()) {
            throw new IllegalArgumentException(USAGE_MESSAGE + ", but not both");
        }

        List<Object> optionalPaths = pluginParams.getOpts().getList(FIRST_AVAILABLE_PARAM_KEY);

        if (pluginParams.getFreeParam().isEmpty() && optionalPaths.isEmpty()) {
            throw new IllegalArgumentException(USAGE_MESSAGE + ", but none was specified");
        }

        return optionalPaths.stream()
                .filter(p -> resourcesResolver.canResolve(p.toString()))
                .findFirst()
                .map(p -> resourcesResolver.fullPath(p.toString()))
                .orElseGet(() -> resourcesResolver.fullPath(pluginParams.getFreeParam()));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(
                Stream.of(AuxiliaryFile.builtTime(markdownPathUsed)),
                parserResult.auxiliaryFiles().stream());
    }

    @Override
    public SearchText textForSearch() {
        return SearchScore.STANDARD.text(parserResult.getAllText());
    }
}
