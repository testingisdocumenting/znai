package com.twosigma.documentation.extensions.inlinedcode

import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.extensions.PluginParams
import com.twosigma.documentation.extensions.PluginResult
import com.twosigma.documentation.parser.docelement.DocElement

import java.nio.file.Path

class DummyInlinedCodePlugin implements InlinedCodePlugin {
    @Override
    String id() {
        return "dummy"
    }

    @Override
    InlinedCodePlugin create() {
        return new DummyInlinedCodePlugin()
    }

    @Override
    PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        def dummy = new DocElement("InlinedCodeDummy", "ff", pluginParams.freeParam,
                "opts", pluginParams.opts.toMap())

        return PluginResult.docElements([dummy].stream())
    }
}
