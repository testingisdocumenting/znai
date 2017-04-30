package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class JavaDocIncludePlugin implements IncludePlugin {
    private String fileName;

    @Override
    public String id() {
        return "java-doc";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        fileName = includeParams.getFreeParam();
        String textContent = componentsRegistry.includeResourceResolver().textContent(fileName);

        String javaDoc = new JavaCode(componentsRegistry, textContent).getClassJavaDocText();
        return PluginResult.docElement(DocElementType.SIMPLE_TEXT, Collections.singletonMap("text", javaDoc));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        Path path = componentsRegistry.includeResourceResolver().fullPath(fileName);
        return Stream.of(AuxiliaryFile.builtTime(path));
    }
}
