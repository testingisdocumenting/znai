package com.twosigma.znai.extensions.include

import com.twosigma.znai.core.AuxiliaryFile
import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.extensions.Plugins
import com.twosigma.znai.parser.TestComponentsRegistry
import com.twosigma.znai.parser.docelement.DocElement
import com.twosigma.znai.parser.docelement.DocElementCreationParserHandler

import java.nio.file.Paths
import java.util.stream.Stream

class PluginsTestUtils {
    static class IncludePluginAndParserHandler {
        IncludePlugin includePlugin
        DocElementCreationParserHandler parserHandler
    }

    static String processAndGetSimplifiedCodeBlock(String pluginDef) {
        def result = process(pluginDef)
        return result[0].getProps().snippet
    }

    static List<DocElement> process(String pluginDef) {
        def includePluginAndParserHandler = processAndGetPluginAndParserHandler(pluginDef)
        return includePluginAndParserHandler.parserHandler.docElement.content
    }

    static Stream<AuxiliaryFile> processAndGetAuxiliaryFiles(String pluginDef) {
        def includePluginAndParserHandler = processAndGetPluginAndParserHandler(pluginDef)
        return includePluginAndParserHandler.includePlugin.auxiliaryFiles(TestComponentsRegistry.INSTANCE)
    }

    static IncludePlugin processAndGetIncludePlugin(String pluginDef) {
        def includePluginAndParserHandler = processAndGetPluginAndParserHandler(pluginDef)
        return includePluginAndParserHandler.includePlugin
    }

    static IncludePluginAndParserHandler processAndGetPluginAndParserHandler(String pluginDef) {
        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(
                TestComponentsRegistry.INSTANCE,
                Paths.get(""))

        PluginParams includeParams = IncludePluginParser.parse(pluginDef)
        def includePlugin = Plugins.includePluginById(includeParams.pluginId)

        def pluginResult = includePlugin.process(TestComponentsRegistry.INSTANCE, parserHandler, Paths.get(""), includeParams)

        parserHandler.onIncludePlugin(includePlugin, pluginResult)

        return new IncludePluginAndParserHandler(includePlugin: includePlugin, parserHandler: parserHandler)
    }
}
