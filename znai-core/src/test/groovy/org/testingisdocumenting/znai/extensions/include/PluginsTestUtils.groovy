/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.extensions.include

import org.testingisdocumenting.znai.core.AuxiliaryFile
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.testingisdocumenting.znai.extensions.Plugins
import org.testingisdocumenting.znai.extensions.PluginsRegexp
import org.testingisdocumenting.znai.extensions.fence.FencePlugin
import org.testingisdocumenting.znai.extensions.inlinedcode.InlinedCodePlugin
import org.testingisdocumenting.znai.parser.docelement.DocElement
import org.testingisdocumenting.znai.parser.docelement.DocElementCreationParserHandler

import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class PluginsTestUtils {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    static class IncludePluginAndParserHandler {
        IncludePlugin includePlugin
        DocElementCreationParserHandler parserHandler
    }

    static class FencePluginAndParserHandler {
        FencePlugin fencePlugin
        DocElementCreationParserHandler parserHandler
    }

    static class InlinedCodePluginAndParserHandler {
        InlinedCodePlugin inlinedCodePlugin
        DocElementCreationParserHandler parserHandler
    }

    static Map<String, Object> processIncludeAndGetProps(String pluginDef, Path markupPath = Paths.get("")) {
        def result = processInclude(pluginDef, markupPath)
        return result[0].getProps()
    }

    static Map<String, Object> processInlinedCodeAndGetProps(String pluginDef) {
        def result = processInlinedCode(pluginDef)
        return result[0].getProps()
    }

    static String processAndGetSimplifiedCodeBlock(String pluginDef) {
        return processIncludeAndGetProps(pluginDef).snippet
    }

    static List<DocElement> processInclude(String pluginDef, Path markupPath = Paths.get("")) {
        def includePluginAndParserHandler = processAndGetIncludePluginAndParserHandler(pluginDef, markupPath)
        return includePluginAndParserHandler.parserHandler.docElement.content
    }

    static List<DocElement> processInlinedCode(String pluginDef) {
        def pluginAndParserHandler = processAndGetInlinedCodePluginAndParserHandler(pluginDef)
        return pluginAndParserHandler.parserHandler.docElement.content
    }

    static Stream<AuxiliaryFile> processIncludeAndGetAuxiliaryFiles(String pluginDef) {
        def includePluginAndParserHandler = processAndGetIncludePluginAndParserHandler(pluginDef)
        return includePluginAndParserHandler.includePlugin.auxiliaryFiles(TEST_COMPONENTS_REGISTRY)
    }

    static IncludePlugin processAndGetIncludePlugin(String pluginDef) {
        def includePluginAndParserHandler = processAndGetIncludePluginAndParserHandler(pluginDef)
        return includePluginAndParserHandler.includePlugin
    }

    static IncludePluginAndParserHandler processAndGetIncludePluginAndParserHandler(String pluginDef, Path markupPath = Paths.get("")) {
        DocElementCreationParserHandler parserHandler = createParserHandler()

        def idAndParams = PluginsRegexp.parseIncludePlugin(pluginDef)
        PluginParams includeParams = pluginParamsFactory.create(idAndParams.id, idAndParams.params)
        def includePlugin = Plugins.includePluginById(includeParams.pluginId)


        includePlugin.preprocess(TEST_COMPONENTS_REGISTRY, markupPath, includeParams)
        includePlugin.parameters().validate(includeParams)

        def pluginResult = includePlugin.process(TEST_COMPONENTS_REGISTRY, parserHandler, markupPath, includeParams)

        parserHandler.onIncludePlugin(includePlugin, pluginResult)

        return new IncludePluginAndParserHandler(includePlugin: includePlugin, parserHandler: parserHandler)
    }

    static Map<String, Object> processFenceAndGetProps(PluginParams pluginParams,
                                                       String textContent) {
        def pluginAndHandler = processAndGetFencePluginAndParserHandler(pluginParams, textContent)
        return pluginAndHandler.parserHandler.docElement.content[0].getProps()
    }

    static Stream<AuxiliaryFile> processFenceAndGetAuxiliaryFiles(PluginParams pluginParams,
                                                                  String textContent) {
        def pluginAndHandler = processAndGetFencePluginAndParserHandler(pluginParams, textContent)
        return pluginAndHandler.fencePlugin.auxiliaryFiles(TEST_COMPONENTS_REGISTRY)
    }

    static FencePluginAndParserHandler processAndGetFencePluginAndParserHandler(PluginParams pluginParams,
                                                                                String textContent) {
        DocElementCreationParserHandler parserHandler = createParserHandler()

        def fencePlugin = Plugins.fencePluginById(pluginParams.pluginId)
        fencePlugin.preprocess(TEST_COMPONENTS_REGISTRY, Paths.get(""),  pluginParams, textContent)

        fencePlugin.parameters().validate(pluginParams)

        def pluginResult = fencePlugin.process(TEST_COMPONENTS_REGISTRY, Paths.get(""),  pluginParams, textContent)
        parserHandler.onFencePlugin(fencePlugin, pluginResult)

        return new FencePluginAndParserHandler(fencePlugin: fencePlugin, parserHandler: parserHandler)
    }

    static InlinedCodePluginAndParserHandler processAndGetInlinedCodePluginAndParserHandler(String pluginDef) {
        DocElementCreationParserHandler parserHandler = createParserHandler()

        def idAndParams = PluginsRegexp.parseInlinedCodePlugin(pluginDef)
        PluginParams pluginParams = pluginParamsFactory.create(idAndParams.id, idAndParams.params)

        def inlinedCodePlugin = Plugins.inlinedCodePluginById(idAndParams.id)
        inlinedCodePlugin.parameters().validate(pluginParams)

        def result = inlinedCodePlugin.process(TEST_COMPONENTS_REGISTRY, Paths.get(""), pluginParams)
        parserHandler.onInlinedCodePlugin(inlinedCodePlugin, result)

        return new InlinedCodePluginAndParserHandler(inlinedCodePlugin: inlinedCodePlugin, parserHandler: parserHandler)
    }

    private static DocElementCreationParserHandler createParserHandler() {
        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(
                TEST_COMPONENTS_REGISTRY,
                Paths.get(""))
        return parserHandler
    }
}
