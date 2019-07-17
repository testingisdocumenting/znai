package com.twosigma.znai.extensions.inlinedcode;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.Plugin;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;

import java.nio.file.Path;

public interface InlinedCodePlugin extends Plugin {
    InlinedCodePlugin create();

    PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams);
}
