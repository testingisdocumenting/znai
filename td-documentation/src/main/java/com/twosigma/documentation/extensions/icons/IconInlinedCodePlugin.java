package com.twosigma.documentation.extensions.icons;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.inlinedcode.InlinedCodePlugin;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class IconInlinedCodePlugin implements InlinedCodePlugin {
    @Override
    public String id() {
        return "icon";
    }

    @Override
    public InlinedCodePlugin create() {
        return new IconInlinedCodePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        return PluginResult.docElement(new DocElement("Icon", "id", pluginParams.getFreeParam()));
    }
}
