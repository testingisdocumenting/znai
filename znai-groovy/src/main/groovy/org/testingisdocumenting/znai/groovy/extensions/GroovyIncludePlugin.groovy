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

package org.testingisdocumenting.znai.groovy.extensions

import org.testingisdocumenting.znai.codesnippets.CodeSnippetsProps
import org.testingisdocumenting.znai.core.AuxiliaryFile
import org.testingisdocumenting.znai.core.ComponentsRegistry
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.extensions.PluginResult
import org.testingisdocumenting.znai.extensions.features.PluginFeatureList
import org.testingisdocumenting.znai.extensions.file.CodeReferencesFeature
import org.testingisdocumenting.znai.extensions.file.ManipulatedSnippetContentProvider
import org.testingisdocumenting.znai.extensions.file.SnippetAutoTitleFeature
import org.testingisdocumenting.znai.extensions.file.SnippetHighlightFeature
import org.testingisdocumenting.znai.extensions.file.SnippetRevealLineStopFeature
import org.testingisdocumenting.znai.extensions.include.IncludePlugin
import org.testingisdocumenting.znai.groovy.parser.GroovyCode
import org.testingisdocumenting.znai.parser.ParserHandler
import org.testingisdocumenting.znai.parser.docelement.DocElementType
import org.testingisdocumenting.znai.search.SearchScore
import org.testingisdocumenting.znai.search.SearchText

import java.nio.file.Path
import java.util.stream.Stream

class GroovyIncludePlugin implements IncludePlugin {
    private String path
    private Path fullPath
    private PluginFeatureList features

    private ManipulatedSnippetContentProvider contentProvider

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
        path = pluginParams.getFreeParam()
        fullPath = componentsRegistry.resourceResolver().fullPath(path)
        String fileContent = componentsRegistry.resourceResolver().textContent(fullPath)
        List<String> entries = pluginParams.getOpts().getList("entry")

        GroovyCode groovyCode = new GroovyCode(componentsRegistry, fullPath, fileContent)

        Boolean bodyOnly = pluginParams.getOpts().has("bodyOnly") ? pluginParams.getOpts().get("bodyOnly") : false

        String content = extractContent(groovyCode, entries, bodyOnly)
        contentProvider = new ManipulatedSnippetContentProvider(path, content, pluginParams)

        features = new PluginFeatureList(
                new SnippetAutoTitleFeature(contentProvider),
                new SnippetHighlightFeature(componentsRegistry, pluginParams, contentProvider),
                new SnippetRevealLineStopFeature(pluginParams, contentProvider),
                new CodeReferencesFeature(componentsRegistry, markupPath, pluginParams)
        )

        Map<String, Object> props = CodeSnippetsProps.create("groovy", contentProvider.snippetContent())
        props.putAll(pluginParams.getOpts().toMap())

        features.updateProps(props)

        return PluginResult.docElement(DocElementType.SNIPPET, props)
    }

    @Override
    SearchText textForSearch() {
        return SearchScore.STANDARD.text(contentProvider.snippetContent())
    }

    @Override
    Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return features.combineAuxiliaryFilesWith(Stream.of(AuxiliaryFile.builtTime(fullPath)))
    }

    private static String extractContent(GroovyCode groovyCode, List<String> entries, Boolean bodyOnly) {
        if (entries.isEmpty()) {
            return groovyCode.getFileContent()
        }

        String firstEntry = entries.get(0)

        return groovyCode.hasTypeDetails(firstEntry) ?
                extractTypeContent(groovyCode, firstEntry, bodyOnly):
                extractMethodsContent(groovyCode, entries, bodyOnly)
    }

    private static String extractTypeContent(GroovyCode groovyCode, String entry, boolean bodyOnly) {
        def type = groovyCode.findType(entry)

        return bodyOnly ?
                type.bodyOnly :
                type.fullBody
    }

    private static String extractMethodsContent(GroovyCode groovyCode, List<String> entries, boolean bodyOnly) {
        return entries.collect { extractSingleMethodContent(groovyCode, it, bodyOnly) }.join("\n\n")
    }

    private static String extractSingleMethodContent(GroovyCode groovyCode, String entry, boolean bodyOnly) {
        def method = groovyCode.findMethod(entry)

        return bodyOnly ?
                method.bodyOnly :
                method.fullBody
    }
}
