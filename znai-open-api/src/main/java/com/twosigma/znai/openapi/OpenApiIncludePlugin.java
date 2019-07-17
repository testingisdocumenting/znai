package com.twosigma.znai.openapi;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OpenApiIncludePlugin implements IncludePlugin {
    private Path specPath;
    private List<OpenApiOperation> operations = new ArrayList<>();
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
            parserHandler.onSectionStart(operation.getSummary());
        }

        Map<String, Object> props = pluginParams.getOpts().toMap();
        props.put("operation", operation.toMap());

        parserHandler.onCustomNode("OpenApiOperation", props);
    }
}
