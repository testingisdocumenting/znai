package com.twosigma.documentation.extensions.include;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.Plugin;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;

/**
 *
 * @author mykola
 */
public interface IncludePlugin extends Plugin {
    /**
     * gets called at the beginning of every page before rendering
     * @param context context of the page
     */
    default void reset(IncludeContext context) {}

    PluginResult process(ComponentsRegistry componentsRegistry,
                         ParserHandler parserHandler,
                         Path markupPath,
                         PluginParams pluginParams);
}
