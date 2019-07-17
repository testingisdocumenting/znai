package com.twosigma.znai.extensions.icons;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.inlinedcode.InlinedCodePlugin;
import com.twosigma.znai.parser.docelement.DocElement;

import java.nio.file.Path;

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
