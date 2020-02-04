/*
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

package com.twosigma.znai.extensions.include

import com.twosigma.znai.core.AuxiliaryFile
import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.extensions.Plugins
import com.twosigma.znai.parser.docelement.DocElement
import com.twosigma.znai.parser.docelement.DocElementCreationParserHandler

import java.nio.file.Paths
import java.util.stream.Stream

import static com.twosigma.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class PluginsTestUtils {
    static class IncludePluginAndParserHandler {
        IncludePlugin includePlugin
        DocElementCreationParserHandler parserHandler
    }

    static Map<String, Object> processAndGetProps(String pluginDef) {
        def result = process(pluginDef)
        return result[0].getProps()
    }

    static String processAndGetSimplifiedCodeBlock(String pluginDef) {
        return processAndGetProps(pluginDef).snippet
    }

    static List<DocElement> process(String pluginDef) {
        def includePluginAndParserHandler = processAndGetPluginAndParserHandler(pluginDef)
        return includePluginAndParserHandler.parserHandler.docElement.content
    }

    static Stream<AuxiliaryFile> processAndGetAuxiliaryFiles(String pluginDef) {
        def includePluginAndParserHandler = processAndGetPluginAndParserHandler(pluginDef)
        return includePluginAndParserHandler.includePlugin.auxiliaryFiles(TEST_COMPONENTS_REGISTRY)
    }

    static IncludePlugin processAndGetIncludePlugin(String pluginDef) {
        def includePluginAndParserHandler = processAndGetPluginAndParserHandler(pluginDef)
        return includePluginAndParserHandler.includePlugin
    }

    static IncludePluginAndParserHandler processAndGetPluginAndParserHandler(String pluginDef) {
        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(
                TEST_COMPONENTS_REGISTRY,
                Paths.get(""))

        PluginParams includeParams = IncludePluginParser.parse(pluginDef)
        def includePlugin = Plugins.includePluginById(includeParams.pluginId)

        def pluginResult = includePlugin.process(TEST_COMPONENTS_REGISTRY, parserHandler, Paths.get(""), includeParams)

        parserHandler.onIncludePlugin(includePlugin, pluginResult)

        return new IncludePluginAndParserHandler(includePlugin: includePlugin, parserHandler: parserHandler)
    }
}
