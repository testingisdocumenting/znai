package com.twosigma.documentation.java.extensions;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.java.parser.JavaCode;
import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mykola
 */
abstract public class JavaIncludePluginBase implements IncludePlugin {
    protected Path fullPath;
    protected ComponentsRegistry componentsRegistry;
    protected Path markupPath;
    protected PluginParams pluginParams;
    protected String entry;
    protected List<String> entries;

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, PluginParams pluginParams) {
        this.componentsRegistry = componentsRegistry;
        this.markupPath = markupPath;
        this.pluginParams = pluginParams;
        fullPath = componentsRegistry.includeResourceResolver().fullPath(pluginParams.getFreeParam());
        entry = pluginParams.getOpts().get("entry");
        entries = pluginParams.getOpts().get("entries");

        if (entry != null && entries != null) {
            throw new IllegalArgumentException("specify either entry or entries");
        }

        JavaCode javaCode = new JavaCode(componentsRegistry.includeResourceResolver().textContent(pluginParams.getFreeParam()));
        List<DocElement> docElements = process(javaCode);

        return PluginResult.docElements(docElements.stream());
    }

    abstract public List<DocElement> process(JavaCode javaCode);

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.of(AuxiliaryFile.builtTime(fullPath));
    }
}
