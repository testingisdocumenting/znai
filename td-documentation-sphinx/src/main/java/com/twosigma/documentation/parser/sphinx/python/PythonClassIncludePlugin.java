package com.twosigma.documentation.parser.sphinx.python;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class PythonClassIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "python-class";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        return PluginResult.docElement("LangClass", pluginParams.getOpts().toMap());
    }
}
