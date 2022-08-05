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
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.ParserHandler;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.parser.docelement.DocElement;
import org.testingisdocumenting.znai.utils.CollectionUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

        findOperation(pluginParams);

        renderSection();
        renderUrl();
        renderDescription(operation.getDescription());
        renderParameters();
        renderRequests();
        renderResponses();

        return PluginResult.docElements(Stream.empty());
    }

    private void findOperation(PluginParams pluginParams) {
        String operationId = pluginParams.getOpts().get("operationId");
        operation = spec.findById(operationId);

        if (operation == null) {
            throw new IllegalArgumentException("can't find openapi operation: " + operationId);
        }
    }

    private void renderSection() {
        parserHandler.onSectionStart(operation.getSummary(), HeadingProps.EMPTY);
    }

    private void renderUrl() {
        parserHandler.onCustomNode("OpenApiMethodAndUrl", CollectionUtils.createMap("method", operation.getMethod(), "url", operation.getPath()));
    }

    private void renderRequests() {
        if (operation.getRequest() == null) {
            return;
        }

        parserHandler.onSubHeading(2, "Request", HeadingProps.STYLE_API);
        renderDescription(operation.getRequest().getDescription());
        renderContent(operation.getRequest().getContent());
    }

    private void renderResponses() {
        if (operation.getResponses().isEmpty()) {
            return;
        }

        parserHandler.onSubHeading(2, renderResponsesTitle(), HeadingProps.STYLE_API);

        for (OpenApi3Response response : operation.getResponses()) {
            parserHandler.onSubHeading(4, response.getCode(), HeadingProps.STYLE_API);
            renderDescription(response.getDescription());

            renderContent(response.getContent());
        }
    }

    private String renderResponsesTitle() {
        return "Response" + (operation.getResponses().size() > 1 ? "s" : "");
    }

    private void renderContent(OpenApi3Content content) {
        if (content.getByMimeType().isEmpty()) {
            return;
        }

        List<SchemaWithCollapsed> schemasWithCollapsed = new ArrayList<>();

        Map<String, OpenApi3Schema> byMimeType = content.getByMimeType();
        byMimeType.forEach((mimeType, schema) -> schemasWithCollapsed.add(new SchemaWithCollapsed(mimeType, schema, true)));

        int jsonIdx = IntStream.range(0, schemasWithCollapsed.size())
                .filter(idx -> schemasWithCollapsed.get(idx).mimeType.equals("application/json"))
                .findFirst().orElse(-1);

        if (jsonIdx != -1) {
            Collections.swap(schemasWithCollapsed, jsonIdx, 0);
        }

        schemasWithCollapsed.get(0).collapsed = false;

        schemasWithCollapsed.forEach(this::renderSchema);
    }

    private void renderDescription(String description) {
        DocElement docElement = openApiMarkdownParser.docElementFromDescription(description);
        docElement.getContent().forEach(parserHandler::onDocElement);
    }

    private void renderParameters() {
        List<OpenApi3Parameter> parameters = operation.getParameters();
        renderParametersWithTitle(parameters.stream().filter(p -> p.getIn().equals("path")), "path parameters");
        renderParametersWithTitle(parameters.stream().filter(p -> p.getIn().equals("query")), "query parameters");
    }

    private void renderParametersWithTitle(Stream<OpenApi3Parameter> parametersStream, String title) {
        List<OpenApi3Parameter> parameters = parametersStream.collect(Collectors.toList());
        if (parameters.isEmpty()) {
            return;
        }

        ApiParameters apiParameters = new OpenApi3ParametersToApiParametersConverter(
                openApiMarkdownParser,
                operation.getId(),
                parameters).convert();

        Map<String, Object> props = apiParameters.toMap();
        props.put("title", title);
        props.put("collapsible", true);

        parserHandler.onCustomNode("ApiParameters", props);
    }

    private void renderSchema(SchemaWithCollapsed schemaWithCollapsed) {
        ApiParameters apiParameters = new OpenApi3SchemaToApiParametersConverter(
                openApiMarkdownParser,
                operation.getId() + schemaWithCollapsed.mimeType,
                schemaWithCollapsed.schema).convert();

        Map<String, Object> props = apiParameters.toMap();
        props.put("title", schemaWithCollapsed.mimeType);
        props.put("collapsible", true);
        props.put("collapsed", schemaWithCollapsed.collapsed);

        parserHandler.onCustomNode("ApiParameters", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(specPath));
    }

    private static class SchemaWithCollapsed {
        private final String mimeType;
        private final OpenApi3Schema schema;
        private boolean collapsed;

        private SchemaWithCollapsed(String mimeType, OpenApi3Schema schema, boolean collapsed) {
            this.mimeType = mimeType;
            this.schema = schema;
            this.collapsed = collapsed;
        }
    }
}