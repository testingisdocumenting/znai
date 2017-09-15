package com.twosigma.documentation.extensions.svg;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class SvgIncludePlugin implements IncludePlugin {
    private Path svgPath;

    @Override
    public String id() {
        return "svg";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
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
