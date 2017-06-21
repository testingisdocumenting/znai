package com.twosigma.documentation.parser;

import java.nio.file.Path;
import java.util.Arrays;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.docelement.DocElementVisitor;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import com.twosigma.documentation.parser.commonmark.CommonMarkExtension;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;

/**
 * @author mykola
 */
public class MarkdownParser implements MarkupParser {
    private final Parser parser;
    private final ComponentsRegistry componentsRegistry;

    public MarkdownParser(ComponentsRegistry componentsRegistry) {
        this.componentsRegistry = componentsRegistry;
        CommonMarkExtension extension = new CommonMarkExtension();
        parser = Parser.builder().extensions(Arrays.asList(extension,
                TablesExtension.create(),
                YamlFrontMatterExtension.create())).build();
    }

    public MarkupParserResult parse(Path path, String markdown) {
        Node node = parser.parse(markdown);

        DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(componentsRegistry, path);
        DocElementVisitor visitor = new DocElementVisitor(componentsRegistry, path, parserHandler);
        node.accept(visitor);

        YamlFrontMatterVisitor frontMatterVisitor = new YamlFrontMatterVisitor();
        node.accept(frontMatterVisitor);

        if (visitor.isSectionStarted()) {
            parserHandler.onSectionEnd();
        }

        parserHandler.onParsingEnd();

        return new MarkupParserResult(parserHandler.getDocElement(), parserHandler.getAuxiliaryFiles(),
                frontMatterVisitor.getData());
    }
}
