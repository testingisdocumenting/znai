package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.core.AuxiliaryFile
import com.twosigma.documentation.extensions.PluginParams
import com.twosigma.documentation.extensions.Plugins
import com.twosigma.documentation.parser.TestComponentsRegistry
import com.twosigma.documentation.parser.docelement.DocElement
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler

import java.nio.file.Paths
import java.util.stream.Stream

/**
 * @author mykola
 */
class PluginsTestUtils {
    static String processAndGetSimplifiedCodeBlock(String pluginDef) {
        def result = process(pluginDef)
        return result[0].getProps().snippet
    }

    static List<DocElement> process(String pluginDef) {
        def (includePlugin, parserHandler) = processAndGetPluginAndHandler(pluginDef)
        return parserHandler.docElement.content
    }

    static Stream<AuxiliaryFile> processAndGetAuxiliaryFiles(String pluginDef) {
        def (includePlugin) = processAndGetPluginAndHandler(pluginDef)
        return includePlugin.auxiliaryFiles(TestComponentsRegistry.INSTANCE)
    }

    private static def processAndGetPluginAndHandler(String pluginDef) {
        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(
                TestComponentsRegistry.INSTANCE,
                Paths.get(""))

        PluginParams includeParams = IncludePluginParser.parse(pluginDef)
        def includePlugin = Plugins.includePluginById(includeParams.pluginId)

        def pluginResult = includePlugin.process(TestComponentsRegistry.INSTANCE, parserHandler, Paths.get(""), includeParams)

        parserHandler.onIncludePlugin(includePlugin, pluginResult)

        return [includePlugin, parserHandler]
    }
}
