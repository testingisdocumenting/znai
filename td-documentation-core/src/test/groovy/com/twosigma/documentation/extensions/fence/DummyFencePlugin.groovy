package com.twosigma.documentation.extensions.fence

import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.extensions.PluginParams
import com.twosigma.documentation.extensions.PluginResult
import com.twosigma.documentation.parser.docelement.DocElement

import java.nio.file.Path

/**
 * @author mykola
 */
class DummyFencePlugin implements FencePlugin {
    @Override
    String id() {
        return "dummy"
    }

    @Override
    FencePlugin create() {
        return new DummyFencePlugin()
    }

    @Override
    PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams, String content) {
        def dummy = new DocElement("FenceDummy")
        if (pluginParams.freeParam) {
            dummy.addProp("freeParam", pluginParams.freeParam)
        }

        dummy.addProp("content", content)

        pluginParams.opts.forEach { k, v -> dummy.addProp(k, v)}

        return PluginResult.docElements([dummy].stream())
    }
}
