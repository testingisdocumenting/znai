package com.twosigma.znai.diagrams.plantuml;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.fence.FencePlugin;

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
