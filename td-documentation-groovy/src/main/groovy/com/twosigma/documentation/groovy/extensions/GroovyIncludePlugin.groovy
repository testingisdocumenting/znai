package com.twosigma.documentation.groovy.extensions

import com.twosigma.documentation.codesnippets.CodeSnippetsProps
import com.twosigma.documentation.core.AuxiliaryFile
import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.extensions.PluginParams
import com.twosigma.documentation.extensions.PluginResult
import com.twosigma.documentation.extensions.include.IncludePlugin
import com.twosigma.documentation.groovy.parser.GroovyCode
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
    PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        fullPath = componentsRegistry.includeResourceResolver().fullPath(pluginParams.getFreeParam())
        String fileContent = componentsRegistry.includeResourceResolver().textContent(fullPath)
        String methodName = pluginParams.getOpts().get("entry")

        GroovyCode groovyCode = new GroovyCode(componentsRegistry, fullPath, fileContent)

        Boolean bodyOnly = pluginParams.getOpts().has("bodyOnly") ? pluginParams.getOpts().get("bodyOnly") : false;

        Map<String, Object> props = CodeSnippetsProps.create(componentsRegistry.codeTokenizer(), "groovy",
                extractContent(groovyCode, methodName, bodyOnly))

        return PluginResult.docElement(DocElementType.SNIPPET, props)
    }

    @Override
    Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath))
    }

    private static String extractContent(GroovyCode groovyCode, String methodName, Boolean bodyOnly) {
        if (methodName == null) {
            return groovyCode.getFileContent();
        }

        return bodyOnly ?
                groovyCode.methodBodyOnly(methodName) :
                groovyCode.methodBody(methodName);
    }
}
