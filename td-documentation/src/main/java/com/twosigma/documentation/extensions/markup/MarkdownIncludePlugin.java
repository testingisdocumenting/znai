package com.twosigma.documentation.extensions.markup;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.ParserHandler;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class MarkdownIncludePlugin implements IncludePlugin {
    private static final String FIRST_AVAILABLE_PARAM = "firstAvailable";
    private static final String USAGE_MESSAGE = "use either <" + FIRST_AVAILABLE_PARAM + "> or free " +
            "form param to specify file to include";

    private MarkupParserResult parserResult;
    private Path markdownPathUsed;

    @Override
    public String id() {
        return "markdown";
    }

    @Override
    public IncludePlugin create() {
        return new MarkdownIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        ResourcesResolver resourcesResolver = componentsRegistry.resourceResolver();
        MarkupParser parser = componentsRegistry.defaultParser();

        markdownPathUsed = selectMarkdown(componentsRegistry.resourceResolver(), pluginParams);
        parserResult = parser.parse(markupPath, resourcesResolver.textContent(markdownPathUsed));

        return PluginResult.docElements(parserResult.getDocElement().getContent().stream());
    }

    private Path selectMarkdown(ResourcesResolver resourcesResolver, PluginParams pluginParams) {
        if (pluginParams.getOpts().has(FIRST_AVAILABLE_PARAM) && !pluginParams.getFreeParam().isEmpty()) {
            throw new IllegalArgumentException(USAGE_MESSAGE + ", but not both");
        }

        List<Object> optionalPaths = pluginParams.getOpts().getList(FIRST_AVAILABLE_PARAM);

        if (pluginParams.getFreeParam().isEmpty() && optionalPaths.isEmpty()) {
            throw new IllegalArgumentException(USAGE_MESSAGE + ", but none was specified");
        }

        return optionalPaths.stream()
                .filter(p -> resourcesResolver.canResolve(p.toString()))
                .findFirst()
                .map(p -> resourcesResolver.fullPath(p.toString()))
                .orElseGet(() -> resourcesResolver.fullPath(pluginParams.getFreeParam()));
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return Stream.concat(
                Stream.of(AuxiliaryFile.builtTime(markdownPathUsed)),
                parserResult.getAuxiliaryFiles().stream());
    }
}
