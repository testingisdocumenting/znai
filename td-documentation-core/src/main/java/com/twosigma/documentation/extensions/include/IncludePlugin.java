package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.Plugin;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;

public interface IncludePlugin extends Plugin {
    IncludePlugin create();

    PluginResult process(ComponentsRegistry componentsRegistry,
                         ParserHandler parserHandler,
                         Path markupPath,
                         PluginParams pluginParams);
}
