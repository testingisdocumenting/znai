package com.twosigma.znai.diagrams.plantuml;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

public class PlantUmlIncludePlugin implements IncludePlugin {
    private Path fullPath;

    @Override
    public String id() {
        return "plantuml";
    }

    @Override
    public IncludePlugin create() {
        return new PlantUmlIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();

        fullPath = resourcesResolver.fullPath(pluginParams.getFreeParam());
        return PluginResult.docElement("Svg", Collections.singletonMap("svg",
                PlantUml.generateSvg(resourcesResolver.textContent(fullPath))));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
