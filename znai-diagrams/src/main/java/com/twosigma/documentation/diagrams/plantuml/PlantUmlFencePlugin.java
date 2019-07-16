package com.twosigma.documentation.diagrams.plantuml;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;

import java.nio.file.Path;
import java.util.Collections;

public class PlantUmlFencePlugin implements FencePlugin {
    @Override
    public String id() {
        return "plantuml";
    }

    @Override
    public FencePlugin create() {
        return new PlantUmlFencePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        return PluginResult.docElement("Svg", Collections.singletonMap("svg", PlantUml.generateSvg(content)));
    }
}
