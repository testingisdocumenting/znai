package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.plantuml.PlantUml;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.Plugin;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * @author mykola
 */
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
