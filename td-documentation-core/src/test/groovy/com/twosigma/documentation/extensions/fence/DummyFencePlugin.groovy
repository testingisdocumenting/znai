package com.twosigma.documentation.extensions.fence

import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.extensions.PluginResult
import com.twosigma.documentation.parser.docelement.DocElement

import java.nio.file.Path
import java.util.stream.Stream

/**
 * @author mykola
 */
class DummyFencePlugin implements FencePlugin {
    @Override
    String id() {
        return "dummy"
    }

    @Override
    PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, String content) {
        def dummy = new DocElement("FenceDummy")
        dummy.addProp("content", content)

        return PluginResult.docElements(Stream.of(dummy))
    }
}
