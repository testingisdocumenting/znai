package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.codesnippets.CodeSnippetsProps;
import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class JavaIncludePlugin implements IncludePlugin {
    private Path fullPath;

    @Override
    public String id() {
        return "java";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        fullPath = componentsRegistry.includeResourceResolver().fullPath(includeParams.getFreeParam());
        String fileContent = componentsRegistry.includeResourceResolver().textContent(fullPath);
        String methodName = includeParams.getOpts().get("entry");

        JavaCode javaCode = new JavaCode(componentsRegistry, fullPath, fileContent);

        Boolean bodyOnly = includeParams.getOpts().has("bodyOnly") ? includeParams.getOpts().get("bodyOnly") : false;

        Map<String, Object> props = CodeSnippetsProps.create(componentsRegistry.codeTokenizer(), "java",
                extractContent(javaCode, methodName, bodyOnly));

        return PluginResult.docElement(DocElementType.SNIPPET, props);
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }

    private String extractContent(JavaCode javaCode, String methodName, Boolean bodyOnly) {
        if (methodName == null) {
            return javaCode.getFileContent();
        }

        return bodyOnly ?
                javaCode.methodBodyOnly(methodName) :
                javaCode.methodBody(methodName);
    }
}
