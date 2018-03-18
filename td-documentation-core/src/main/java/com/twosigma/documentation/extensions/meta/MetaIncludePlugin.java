package com.twosigma.documentation.extensions.meta;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class MetaIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "meta";
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
