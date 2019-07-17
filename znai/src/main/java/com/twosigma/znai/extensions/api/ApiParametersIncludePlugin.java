package com.twosigma.znai.extensions.api;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

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
