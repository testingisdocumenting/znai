package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.extensions.PluginParams
import com.twosigma.documentation.extensions.PluginResult
import com.twosigma.documentation.extensions.Plugins
import com.twosigma.documentation.parser.TestComponentsRegistry

import java.nio.file.Paths

/**
 * @author mykola
 */
class PluginsTestUtils {
    static private TestComponentsRegistry testComponentsRegistry

    static String processAndGetSimplifiedCodeBlock(String pluginDef) {
        def result = process(pluginDef)
        return result.docElements.get(0).getProps().tokens[0].content
    }

    static PluginResult process(String pluginDef) {
        PluginParams includeParams = IncludePluginParser.parse(pluginDef)
        def includePlugin = Plugins.includePluginById(includeParams.pluginId)

        testComponentsRegistry = new TestComponentsRegistry()
        return includePlugin.process(testComponentsRegistry, Paths.get(""), includeParams)
    }
}
