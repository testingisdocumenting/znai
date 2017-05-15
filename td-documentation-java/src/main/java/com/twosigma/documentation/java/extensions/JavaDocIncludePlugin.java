package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.java.parser.html.HtmlToDocElementConverter;
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
        String entry = pluginParams.getOpts().get("entry");

        JavaCode javaCode = new JavaCode(componentsRegistry.includeResourceResolver().textContent(fullPath));
        List<DocElement> docElements = HtmlToDocElementConverter.convert(componentsRegistry, markupPath, entry == null ?
                javaCode.getClassJavaDocText() :
                javaCode.methodByName(entry).getJavaDocText());

        return PluginResult.docElements(docElements.stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
