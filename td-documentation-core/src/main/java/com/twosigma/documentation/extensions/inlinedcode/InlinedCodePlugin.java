package com.twosigma.documentation.extensions.inlinedcode;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.Plugin;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface InlinedCodePlugin extends Plugin {
    InlinedCodePlugin create();

    PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams);
}
