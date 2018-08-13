package com.twosigma.documentation.groovy.extensions

import com.twosigma.documentation.codesnippets.CodeSnippetsProps
import com.twosigma.documentation.core.AuxiliaryFile
import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.extensions.PluginParams
import com.twosigma.documentation.extensions.PluginResult
import com.twosigma.documentation.extensions.include.IncludePlugin
import com.twosigma.documentation.groovy.parser.GroovyCode
import com.twosigma.documentation.parser.ParserHandler
import com.twosigma.documentation.parser.docelement.DocElementType

import java.nio.file.Path
import java.util.stream.Stream

/**
 * @author mykola
 */
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

        Map<String, Object> props = CodeSnippetsProps.create(componentsRegistry.codeTokenizer(), "groovy",
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
