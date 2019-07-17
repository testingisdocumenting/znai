package com.twosigma.znai.extensions.include;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.Plugin;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;

public interface IncludePlugin extends Plugin {
    IncludePlugin create();

    PluginResult process(ComponentsRegistry componentsRegistry,
                         ParserHandler parserHandler,
                         Path markupPath,
                         PluginParams pluginParams);
}
