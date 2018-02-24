package com.twosigma.documentation.extensions.markup;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;
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
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        MarkupParser parser = componentsRegistry.defaultParser();

        Path markdown = resourcesResolver.fullPath(pluginParams.getFreeParam());
        parserResult = parser.parse(markdown, resourcesResolver.textContent(markdown));

        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return parserResult.getAuxiliaryFiles().stream();
    }
}
