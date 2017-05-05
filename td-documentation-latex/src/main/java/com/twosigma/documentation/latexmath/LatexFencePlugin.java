package com.twosigma.documentation.latexmath;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.fence.FencePlugin;

import java.nio.file.Path;
import java.util.Collections;

/**
 * @author mykola
 */
public class LatexFencePlugin implements FencePlugin {
    @Override
    public String id() {
        return "latex";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        String svg = Latex.toSvg(content);
        return PluginResult.docElement("LatexMath", Collections.singletonMap("svg", svg));
    }
}
