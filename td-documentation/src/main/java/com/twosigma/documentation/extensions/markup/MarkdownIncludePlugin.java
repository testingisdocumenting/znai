package com.twosigma.documentation.extensions.markup;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.PluginResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class MarkdownIncludePlugin implements IncludePlugin {
    private MarkupParserResult parserResult;

    @Override
    public String id() {
        return "markdown";
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        PluginResourcesResolver resourcesResolver = componentsRegistry.includeResourceResolver();
        MarkupParser parser = componentsRegistry.parser();

        Path markdown = resourcesResolver.fullPath(includeParams.getFreeParam());
        parserResult = parser.parse(markdown, resourcesResolver.textContent(markdown));

        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return parserResult.getAuxiliaryFiles().stream();
    }
}
