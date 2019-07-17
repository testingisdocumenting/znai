package com.twosigma.znai.parser.sphinx.python;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.extensions.PluginParams;
import com.twosigma.znai.extensions.PluginResult;
import com.twosigma.znai.extensions.include.IncludePlugin;
import com.twosigma.znai.parser.ParserHandler;

import java.nio.file.Path;

public class PythonFunctionIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "python-function";
    }

    @Override
    public IncludePlugin create() {
        return new PythonFunctionIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        return PluginResult.docElement("LangFunction", pluginParams.getOpts().toMap());
    }
}
