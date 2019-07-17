package com.twosigma.znai.extensions.meta;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;

public class MetaIncludePlugin implements IncludePlugin {
    public static final String ID = "meta";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public IncludePlugin create() {
        return new MetaIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        return PluginResult.docElement("Meta", pluginParams.getOpts().toMap());
    }
}
