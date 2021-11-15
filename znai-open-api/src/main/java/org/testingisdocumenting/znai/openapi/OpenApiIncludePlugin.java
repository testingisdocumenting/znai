/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.openapi;

import org.testingisdocumenting.znai.core.AuxiliaryFile;
import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParams;
import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.parser.HeadingProps;
import org.testingisdocumenting.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OpenApiIncludePlugin implements IncludePlugin {
    private Path specPath;
    private final List<OpenApiOperation> operations = new ArrayList<>();
    private PluginParams pluginParams;
    private OpenApiSpec openApiSpec;
    private ParserHandler parserHandler;

    @Override
    public String id() {
        return "open-api";
    }

    @Override
    public IncludePlugin create() {
        return new OpenApiIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        this.pluginParams = pluginParams;
        this.parserHandler = parserHandler;

        specPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        String specContent = componentsRegistry.resourceResolver().textContent(specPath);

        openApiSpec = isJson(specContent) ?
                OpenApiSpec.fromJson(componentsRegistry.markdownParser(), specContent):
                OpenApiSpec.fromYaml(componentsRegistry.markdownParser(), specContent);

        findOperations();
        processOperations();

        return PluginResult.docElements(Stream.empty());
    }

    private boolean isJson(String specContent) {
        return specContent.trim().startsWith("{");
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(specPath));
    }

    private void findOperations() {
        String operationId = pluginParams.getOpts().get("operationId");
        String method = pluginParams.getOpts().get("method", "");
        String path = pluginParams.getOpts().get("path", "");
        List<String> tags = pluginParams.getOpts().getList("tags");

        if ((path.isEmpty() && !method.isEmpty()) || (!path.isEmpty() && method.isEmpty())) {
            throw new IllegalArgumentException("both method and path needs to be specified to find Open API operation");
        }

        if (!path.isEmpty()) {
            operations.add(openApiSpec.findOperationByMethodAndPath(method, path));
        }

        if (operationId != null) {
            operations.add(openApiSpec.findOperationById(operationId));
        }

        if (! tags.isEmpty()) {
            operations.addAll(openApiSpec.findOperationsByTags(tags));
        }
    }

    private void processOperations() {
        boolean isAutoSection = pluginParams.getOpts().get("autoSection", false);

        operations.forEach(operation -> this.processOperation(operation, isAutoSection));
    }

    private void processOperation(OpenApiOperation operation, boolean isAutoSection) {
        if (isAutoSection) {
            parserHandler.onSectionStart(operation.getSummary(), HeadingProps.EMPTY);
        }

        Map<String, Object> props = pluginParams.getOpts().toMap();
        props.put("operation", operation.toMap());

        parserHandler.onCustomNode("OpenApiOperation", props);
    }
}
