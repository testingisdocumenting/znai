package com.twosigma.znai.extensions.twosides;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;

public class EmptyBlockIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "empty-block";
    }

    @Override
    public IncludePlugin create() {
        return new EmptyBlockIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, ParserHandler parserHandler, Path markupPath, PluginParams pluginParams) {
        return PluginResult.docElement("EmptyBlock", pluginParams.getOpts().toMap());
    }
}
