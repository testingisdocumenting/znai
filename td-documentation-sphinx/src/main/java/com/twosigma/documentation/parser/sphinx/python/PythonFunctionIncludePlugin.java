package com.twosigma.documentation.parser.sphinx.python;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;

/**
 * @author mykola
 */
public class PythonFunctionIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "python-function";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        return PluginResult.docElement("LangFunction", pluginParams.getOpts().toMap());
    }
}
