package com.twosigma.znai.extensions.include

import com.twosigma.znai.core.ComponentsRegistry
import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.extensions.PluginResult
import com.twosigma.znai.parser.ParserHandler
import com.twosigma.znai.parser.docelement.DocElement

import java.nio.file.Path

class DummyIncludePlugin implements IncludePlugin {
    @Override
    String id() {
        return "dummy"
    }

    @Override
    IncludePlugin create() {
        return new DummyIncludePlugin()
    }

    @Override
    PluginResult process(ComponentsRegistry componentsRegistry,
                         ParserHandler parserHandler,
                         Path markupPath,
                         PluginParams includeParams) {
        def dummy = new DocElement("IncludeDummy")
        dummy.addProp("ff", includeParams.getFreeParam())
        dummy.addProp("opts", includeParams.getOpts().toMap())

        return PluginResult.docElements([dummy].stream())
    }
}
