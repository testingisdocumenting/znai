package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class JavaDocIncludePlugin implements IncludePlugin {
    private Path fullPath;

    @Override
    public String id() {
        return "java-doc";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        String fileName = pluginParams.getFreeParam();
        fullPath = componentsRegistry.includeResourceResolver().fullPath(fileName);
        String textContent = componentsRegistry.includeResourceResolver().textContent(fullPath);
        String entry = pluginParams.getOpts().get("entry");

        JavaCode javaCode = new JavaCode(componentsRegistry, fullPath, textContent);
        List<DocElement> docElements = entry == null ?
                javaCode.getClassJavaDocAsDocElements() :
                javaCode.methodJavaDocTextAsDocElements(entry);

        return PluginResult.docElements(docElements.stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
