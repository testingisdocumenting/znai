package com.twosigma.documentation.extensions.markup;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginParams;
import com.twosigma.documentation.extensions.PluginResult;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.docelement.DocElement;
import com.twosigma.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class MarkdownsIncludePlugin implements IncludePlugin {
    private List<Path> markdowns;

    @Override
    public String id() {
        return "markdowns";
    }

    @Override
    public IncludePlugin create() {
        return new MarkdownsIncludePlugin();
    }

    @Override
    public PluginResult process(ComponentsRegistry componentsRegistry,
                                ParserHandler parserHandler,
                                Path markupPath,
                                PluginParams pluginParams) {
        Path dir = componentsRegistry.resourceResolver().fullPath(pluginParams.getFreeParam());
        MarkupParser parser = componentsRegistry.defaultParser();

        markdowns = markdowns(dir).collect(Collectors.toList());
        Stream<DocElement> elements = markdowns.stream().flatMap(p -> {
            MarkupParserResult parserResult = parser.parse(markupPath, FileUtils.fileTextContent(p));
            return parserResult.getDocElement().getContent().stream();
        });

        return PluginResult.docElements(elements);
    }

    private Stream<Path> markdowns(Path dir) {
        try {
            return Files.list(dir).filter(f -> f.toString().endsWith(".md")).sorted(Comparator.comparing(Path::toString).reversed());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry) {
        return markdowns.stream().map(AuxiliaryFile::builtTime);
    }
}
