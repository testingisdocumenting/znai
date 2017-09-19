package com.twosigma.documentation.extensions.diagrams;

import com.twosigma.diagrams.plantuml.PlantUml;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;

import java.nio.file.Path;
import java.util.Collections;

/**
 * @author mykola
 */
public class PlantUmlFencePlugin implements FencePlugin {
    @Override
    public String id() {
        return "plantuml";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        return PluginResult.docElement("Svg", Collections.singletonMap("svg", PlantUml.generateSvg(content)));
    }
}
