package com.twosigma.documentation.parser.commonmark;

import java.nio.file.Path;
import java.util.Arrays;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.ParserHandler;
import com.twosigma.documentation.parser.ParserHandlersList;
import com.twosigma.documentation.search.SearchCrawlerParserHandler;
import com.twosigma.documentation.structure.PageMeta;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

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
                StrikethroughExtension.create(),
                YamlFrontMatterExtension.create())).build();
    }

    public MarkupParserResult parse(Path path, String markdown) {
        SearchCrawlerParserHandler searchCrawler = new SearchCrawlerParserHandler();
        DocElementCreationParserHandler elementCreationHandler = new DocElementCreationParserHandler(componentsRegistry, path);

        ParserHandlersList parserHandler = new ParserHandlersList(elementCreationHandler, searchCrawler);

        Node node = parser.parse(markdown);
        MarkdownVisitor visitor = parsePartial(node, path, parserHandler);

        YamlFrontMatterVisitor frontMatterVisitor = new YamlFrontMatterVisitor();
        node.accept(frontMatterVisitor);

        if (visitor.isSectionStarted()) {
            parserHandler.onSectionEnd();
        }

        parserHandler.onParsingEnd();

        return new MarkupParserResult(elementCreationHandler.getDocElement(),
                elementCreationHandler.getGlobalAnchorIds(),
                searchCrawler.getSearchEntries(),
                elementCreationHandler.getAuxiliaryFiles(),
                new PageMeta(frontMatterVisitor.getData()));
    }

    public void parse(Path path, String markdown, ParserHandler handler) {
        Node node = parser.parse(markdown);
        parsePartial(node, path, handler);
    }

    private MarkdownVisitor parsePartial(Node node, Path path, ParserHandler handler) {
        MarkdownVisitor visitor = new MarkdownVisitor(componentsRegistry, path, handler);
        node.accept(visitor);

        return visitor;
    }
}
