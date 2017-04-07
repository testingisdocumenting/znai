package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.extensions.PluginResult
import com.twosigma.documentation.extensions.Plugins
import com.twosigma.documentation.parser.TestComponentsRegistry

import java.nio.file.Paths

/**
 * @author mykola
 */
class PluginsTestUtils {
    static private TestComponentsRegistry testComponentsRegistry

    static PluginResult process(String pluginDef) {
        IncludeParams includeParams = IncludePluginParser.parse(pluginDef)
        def includePlugin = Plugins.includePluginById(includeParams.pluginId)

        testComponentsRegistry = new TestComponentsRegistry()
        return includePlugin.process(testComponentsRegistry, Paths.get(""), includeParams)
    }
}
