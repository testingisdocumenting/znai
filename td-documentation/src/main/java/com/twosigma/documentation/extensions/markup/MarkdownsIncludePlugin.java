package com.twosigma.documentation.extensions.markup;

import com.twosigma.documentation.core.AuxiliaryFile;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeParams;
import com.twosigma.documentation.extensions.include.IncludePlugin;
import com.twosigma.documentation.extensions.include.IncludePluginResult;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
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
    public IncludePluginResult process(ComponentsRegistry componentsRegistry, Path markupPath, IncludeParams includeParams) {
        Path dir = componentsRegistry.includeResourceResolver().fullPath(includeParams.getFreeParam());
        MarkupParser parser = componentsRegistry.parser();

        markdowns = markdowns(dir).collect(Collectors.toList());
        Stream<DocElement> elements = markdowns.stream().flatMap(p -> {
            MarkupParserResult parserResult = parser.parse(p, FileUtils.fileTextContent(p));
            return parserResult.getDocElement().getContent().stream();
        });

        return IncludePluginResult.docElements(elements);
    }

    private Stream<Path> markdowns(Path dir) {
        try {
            return Files.list(dir).filter(f -> f.toString().endsWith(".md")).sorted(Comparator.comparing(Path::toString).reversed());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<AuxiliaryFile> auxiliaryFiles(ComponentsRegistry componentsRegistry, IncludeParams includeParams) {
        return markdowns.stream().map(AuxiliaryFile::builtTime);
    }
}
