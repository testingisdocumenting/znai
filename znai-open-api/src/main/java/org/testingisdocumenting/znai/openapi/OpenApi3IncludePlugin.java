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

package org.testingisdocumenting.znai.openapi;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class OpenApi3IncludePlugin implements IncludePlugin {
    private Path specPath;
    private OpenApi3Operation operation;
    private ParserHandler parserHandler;
    private OpenApi3Spec spec;
    private MarkdownParser markdownParser;
    private OpenApiMarkdownParser openApiMarkdownParser;

    @Override
    public String id() {
        return "open-api3";
    }

    @Override
    public IncludePlugin create() {
        return new OpenApi3IncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        this.markdownParser = componentsRegistry.markdownParser();
        this.parserHandler = parserHandler;

        openApiMarkdownParser = description -> markdownParser.parse(markupPath, description).getDocElement();

        specPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        String specContent = componentsRegistry.resourceResolver().textContent(specPath);

        spec = OpenApi3Spec.parse(specContent);
        operation = spec.findById(pluginParams.getOpts().get("operationId"));

        renderDescription();
        renderParameters();

        return PluginResult.docElements(Stream.empty());
    }

    private void renderDescription() {
        DocElement docElement = openApiMarkdownParser.docElementFromDescription(operation.getDescription());
        docElement.getContent().forEach(parserHandler::onDocElement);
    }

    private void renderParameters() {
        List<OpenApi3Parameter> parameters = operation.getParameters();
        if (parameters.isEmpty()) {
            return;
        }

        ApiParameters apiParameters = new OpenApi3ParametersToApiParametersConverter(openApiMarkdownParser, operation.getId(), parameters).convert();
        parserHandler.onCustomNode("ApiParameters", apiParameters.toMap());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(specPath));
    }
}
