package com.twosigma.documentation.extensions.api;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;
import java.util.stream.Stream;

public class ApiParametersIncludePlugin implements IncludePlugin {
    private Path fullPath;

    @Override
    public String id() {
        return "api-parameters";
    }

    @Override
    public IncludePlugin create() {
        return new ApiParametersIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        fullPath = resourcesResolver.fullPath(pluginParams.getFreeParam());

        ApiParameters apiParameters = ApiParametersJsonParser.parse(componentsRegistry.markdownParser(),
                resourcesResolver.textContent(fullPath));

        return PluginResult.docElement("ApiParameters", apiParameters.toMap());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
