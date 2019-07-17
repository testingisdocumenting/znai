package com.twosigma.znai.extensions.svg;

import com.twosigma.znai.core.AuxiliaryFile;
import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SvgIncludePlugin implements IncludePlugin {
    private Path svgPath;

    @Override
    public String id() {
        return "svg";
    }

    @Override
    public IncludePlugin create() {
        return new SvgIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        svgPath = resourcesResolver.fullPath(pluginParams.getFreeParam());

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("svg", resourcesResolver.textContent(svgPath));
        props.putAll(pluginParams.getOpts().toMap());

        return PluginResult.docElement("Svg", props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(svgPath));
    }
}
