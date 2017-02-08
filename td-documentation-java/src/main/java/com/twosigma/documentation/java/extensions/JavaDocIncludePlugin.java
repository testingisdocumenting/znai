package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.ReactComponent;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.java.parser.JavaDocExtractor;
import com.twosigma.documentation.parser.docelement.DocElementType;

import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class JavaDocIncludePlugin implements IncludePlugin {
    @Override
    public String id() {
        return "java-doc";
    }

    @Override
    public ReactComponent process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        String textContent = componentsRegistry.includeResourceResolver().textContent(includeParams.getFreeParam());
        String javaDoc = JavaDocExtractor.extractTopLevel(textContent);

        return new ReactComponent(DocElementType.SIMPLE_TEXT, Collections.singletonMap("text", javaDoc));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry, IncludeParams includeParams) {
        Path path = componentsRegistry.includeResourceResolver().fullPath(includeParams.getFreeParam());
        return Stream.of(AuxiliaryFile.builtTime(path));
    }
}
