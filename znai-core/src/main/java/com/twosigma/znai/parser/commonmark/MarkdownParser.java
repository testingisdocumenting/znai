package com.twosigma.znai.parser.commonmark;

import java.nio.file.Path;
import java.util.Arrays;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.MarkupParserResult;
import com.twosigma.znai.parser.ParserHandler;
import com.twosigma.znai.parser.ParserHandlersList;
import com.twosigma.znai.search.SearchCrawlerParserHandler;
import com.twosigma.znai.structure.PageMeta;
import com.twosigma.znai.parser.docelement.DocElementCreationParserHandler;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

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
