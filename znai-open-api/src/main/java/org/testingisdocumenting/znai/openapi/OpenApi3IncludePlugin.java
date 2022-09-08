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
import org.testingisdocumenting.znai.extensions.PluginParamType;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginParamsDefinition;
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
    private static final String OPERATION_ID_KEY = "operationId";
    private static final String METHOD_KEY = "method";
    private static final String PATH_KEY = "path";
    private static final String AUTO_SECTION_KEY = "autoSection";
    private static final String TAGS_KEY = "tags";

    private Path specPath;
    private ParserHandler parserHandler;
    private OpenApi3Spec spec;
    private MarkdownParser markdownParser;
    private OpenApiMarkdownParser openApiMarkdownParser;

    @Override
    public String id() {
        return "open-api";
    }

    @Override
    public IncludePlugin create() {
        return new OpenApi3IncludePlugin();
    }

    @Override
    public PluginParamsDefinition parameters() {
        return new PluginParamsDefinition()
                .add(OPERATION_ID_KEY, PluginParamType.STRING, "operation ID to find operation", "findUserById")
                .add(METHOD_KEY, PluginParamType.STRING, "method to find operation", "post")
                .add(PATH_KEY, PluginParamType.STRING, "path to find operation", "/user")
                .add(TAGS_KEY, PluginParamType.STRING, "tags to find operation", "[\"pet\"]")
                .add(AUTO_SECTION_KEY, PluginParamType.BOOLEAN, "auto generate page section for the operation", "true");
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        this.markdownParser = componentsRegistry.markdownParser();
        this.parserHandler = parserHandler;

        openApiMarkdownParser = description -> markdownParser.parse(markupPath, description).getDocElement();

        specPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        String specContent = componentsRegistry.resourceResolver().textContent(specPath);

        spec = OpenApi3Spec.parse(specContent);

        List<OpenApi3Operation> operations = findOperations(pluginParams);
        validateOperations(operations);

        operations.forEach(operation -> renderOperation(operation, pluginParams));

        return PluginResult.docElements(Stream.empty());
    }

    private void validateOperations(List<OpenApi3Operation> operations) {
        if (operations.isEmpty()) {
            throw new IllegalArgumentException("can't find open api operation(s), make sure provided " +
                    OPERATION_ID_KEY + ", " + METHOD_KEY + ", " + PATH_KEY + ", " + TAGS_KEY + " are set correctly");
        }
    }

    private void renderOperation(OpenApi3Operation operation, PluginParams pluginParams) {
        renderSectionIfRequired(operation, pluginParams);
        renderUrl(operation);
        renderDescription(operation.getDescription());
        renderParameters(operation);
        renderRequests(operation);
        renderResponses(operation);
    }

    private List<OpenApi3Operation> findOperations(PluginParams pluginParams) {
        String operationId = pluginParams.getOpts().get(OPERATION_ID_KEY);
        if (operationId != null) {
            return findOperationByOperationId(operationId);
        }

        List<String> tags = pluginParams.getOpts().getList(TAGS_KEY);
        if (!tags.isEmpty()) {
            return findOperationsByTags(tags);
        }

        return findOperationByMethodAndPath(pluginParams);
    }

    private List<OpenApi3Operation> findOperationByOperationId(String operationId) {
        OpenApi3Operation result = spec.findById(operationId);

        if (result == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(result);
    }

    private List<OpenApi3Operation> findOperationByMethodAndPath(PluginParams pluginParams) {
        String method = pluginParams.getOpts().get(METHOD_KEY, "");
        String path = pluginParams.getOpts().get(PATH_KEY, "");

        OpenApi3Operation result = spec.findByMethodAndPath(method, path);
        if (result == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(result);
    }

    private List<OpenApi3Operation> findOperationsByTags(List<String> tags) {
        return spec.findOperationsByTags(tags);
    }

    private void renderSectionIfRequired(OpenApi3Operation operation, PluginParams pluginParams) {
        if (pluginParams.getOpts().get(AUTO_SECTION_KEY, false)) {
            parserHandler.onSectionStart(operation.getSummary(), HeadingProps.EMPTY);
        }
    }

    private void renderUrl(OpenApi3Operation operation) {
        parserHandler.onCustomNode("OpenApiMethodAndUrl", CollectionUtils.createMap("method", operation.getMethod(), "url", operation.getPath()));
    }

    private void renderRequests(OpenApi3Operation operation) {
        if (operation.getRequest() == null) {
            return;
        }

        parserHandler.onSubHeading(2, "Request", HeadingProps.STYLE_API);
        renderDescription(operation.getRequest().getDescription());
        renderContent(operation, operation.getRequest().getContent());
    }

    private void renderResponses(OpenApi3Operation operation) {
        if (operation.getResponses().isEmpty()) {
            return;
        }

        parserHandler.onSubHeading(2, renderResponsesTitle(operation), HeadingProps.STYLE_API);

        for (OpenApi3Response response : operation.getResponses()) {
            parserHandler.onSubHeading(4, response.getCode(), HeadingProps.STYLE_API);
            renderDescription(response.getDescription());

            renderContent(operation, response.getContent());
        }
    }

    private String renderResponsesTitle(OpenApi3Operation operation) {
        return "Response" + (operation.getResponses().size() > 1 ? "s" : "");
    }

    private void renderContent(OpenApi3Operation operation, OpenApi3Content content) {
        if (content.getSchemaByMimeType().isEmpty()) {
            return;
        }

        List<SchemaWithCollapsedAndExample> schemasWithCollapsed = new ArrayList<>();

        Map<String, OpenApi3Schema> byMimeType = content.getSchemaByMimeType();
        byMimeType.forEach((mimeType, schema) -> schemasWithCollapsed.add(
                new SchemaWithCollapsedAndExample(mimeType, schema, true, content.exampleByMimeType(mimeType))));

        int jsonIdx = IntStream.range(0, schemasWithCollapsed.size())
                .filter(idx -> schemasWithCollapsed.get(idx).mimeType.equals("application/json"))
                .findFirst().orElse(-1);

        if (jsonIdx != -1) {
            Collections.swap(schemasWithCollapsed, jsonIdx, 0);
        }

        schemasWithCollapsed.get(0).collapsed = false;

        schemasWithCollapsed.forEach(schemaWithCollapsedAndExample -> renderSchema(operation, schemaWithCollapsedAndExample));
    }

    private void renderDescription(String description) {
        if (description == null) {
            return;
        }

        DocElement docElement = openApiMarkdownParser.docElementFromDescription(description);
        docElement.getContent().forEach(parserHandler::onDocElement);
    }

    private void renderParameters(OpenApi3Operation operation) {
        List<OpenApi3Parameter> parameters = operation.getParameters();
        renderParametersWithTitle(operation, parameters.stream().filter(p -> p.getIn().equals("path")), "path parameters");
        renderParametersWithTitle(operation, parameters.stream().filter(p -> p.getIn().equals("query")), "query parameters");
    }

    private void renderParametersWithTitle(OpenApi3Operation operation, Stream<OpenApi3Parameter> parametersStream, String title) {
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
        props.put("collapsed", false);
        props.put("noGap", true);

        parserHandler.onCustomNode("ApiParameters", props);
    }

    private void renderSchema(OpenApi3Operation operation, SchemaWithCollapsedAndExample schemaWithCollapsedAndExample) {
        ApiParameters apiParameters = new OpenApi3SchemaToApiParametersConverter(
                openApiMarkdownParser,
                operation.getId() + schemaWithCollapsedAndExample.mimeType,
                schemaWithCollapsedAndExample.schema).convert();

        if (!schemaWithCollapsedAndExample.example.isEmpty()) {
            apiParameters.setExample(schemaWithCollapsedAndExample.example);
        }

        Map<String, Object> props = apiParameters.toMap();
        props.put("title", schemaWithCollapsedAndExample.mimeType);
        props.put("collapsed", schemaWithCollapsedAndExample.collapsed);
        props.put("noGap", true);

        parserHandler.onCustomNode("ApiParameters", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(specPath));
    }

    private static class SchemaWithCollapsedAndExample {
        private final String mimeType;
        private final OpenApi3Schema schema;
        private boolean collapsed;
        private final String example;

        private SchemaWithCollapsedAndExample(String mimeType, OpenApi3Schema schema, boolean collapsed, String example) {
            this.mimeType = mimeType;
            this.schema = schema;
            this.collapsed = collapsed;
            this.example = example;
        }
    }
}
