package com.twosigma.znai.extensions.inlinedcode

import com.twosigma.znai.core.ComponentsRegistry
import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.extensions.PluginResult
import com.twosigma.znai.parser.docelement.DocElement

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
