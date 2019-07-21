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

package com.twosigma.znai.groovy.extensions

import com.twosigma.znai.codesnippets.CodeSnippetsProps
import com.twosigma.znai.core.AuxiliaryFile
import com.twosigma.znai.core.ComponentsRegistry
import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.extensions.PluginResult
import com.twosigma.znai.extensions.include.IncludePlugin
import com.twosigma.znai.groovy.parser.GroovyCode
import com.twosigma.znai.parser.ParserHandler
import com.twosigma.znai.parser.docelement.DocElementType

import java.nio.file.Path
import java.util.stream.Stream

class GroovyIncludePlugin implements IncludePlugin {
    private Path fullPath

    @Override
    String id() {
        return "groovy"
    }

    @Override
    IncludePlugin create() {
        return new GroovyIncludePlugin()
    }

    @Override
    PluginResult process(ComponentsRegistry componentsRegistry,
                         ParserHandler parserHandler,
                         Path markupPath,
                         PluginParams pluginParams) {
        fullPath = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam())
        String fileContent = componentsRegistry.resourceResolver().textContent(fullPath)
        String entry = pluginParams.getOpts().get("entry")

        GroovyCode groovyCode = new GroovyCode(componentsRegistry, fullPath, fileContent)

        Boolean bodyOnly = pluginParams.getOpts().has("bodyOnly") ? pluginParams.getOpts().get("bodyOnly") : false

        Map<String, Object> props = CodeSnippetsProps.create("groovy",
                extractContent(groovyCode, entry, bodyOnly))
        props.putAll(pluginParams.getOpts().toMap())

        return PluginResult.docElement(DocElementType.SNIPPET, props)
    }

    @Override
    Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return [AuxiliaryFile.builtTime(fullPath)].stream()
    }

    private static String extractContent(GroovyCode groovyCode, String entry, Boolean bodyOnly) {
        if (entry == null) {
            return groovyCode.getFileContent()
        }

        return groovyCode.hasTypeDetails(entry) ?
                extractTypeContent(groovyCode, entry, bodyOnly):
                extractMethodContent(groovyCode, entry, bodyOnly)
    }

    private static String extractTypeContent(GroovyCode groovyCode, String entry, boolean bodyOnly) {
        def type = groovyCode.findType(entry)

        return bodyOnly ?
                type.bodyOnly :
                type.fullBody
    }

    private static String extractMethodContent(GroovyCode groovyCode, String entry, boolean bodyOnly) {
        def method = groovyCode.findMethod(entry)

        return bodyOnly ?
                method.bodyOnly :
                method.fullBody
    }
}
