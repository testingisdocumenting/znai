package com.twosigma.documentation.extensions.twosides;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;

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
