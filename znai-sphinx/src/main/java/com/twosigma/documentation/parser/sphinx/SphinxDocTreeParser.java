package com.twosigma.documentation.parser.sphinx;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.MarkupParserResult;
import com.twosigma.documentation.parser.ParserHandlersList;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;
import com.twosigma.documentation.search.SearchCrawlerParserHandler;
import com.twosigma.documentation.structure.PageMeta;

import java.nio.file.Path;
import java.util.Collections;

public class SphinxDocTreeParser implements MarkupParser {
    private ComponentsRegistry componentsRegistry;

    public SphinxDocTreeParser(ComponentsRegistry componentsRegistry) {
        this.componentsRegistry = componentsRegistry;
    }

    @Override
    public MarkupParserResult parse(Path path, String docXml) {
        SearchCrawlerParserHandler searchCrawler = new SearchCrawlerParserHandler();
        DocElementCreationParserHandler elementCreationHandler = new DocElementCreationParserHandler(componentsRegistry, path);

        ParserHandlersList parserHandler = new ParserHandlersList(elementCreationHandler, searchCrawler);

        DocTreeDomXmlParser xmlParser = new DocTreeDomXmlParser(componentsRegistry, path, parserHandler);
        xmlParser.parse(docXml);

        return new MarkupParserResult(elementCreationHandler.getDocElement(),
                Collections.emptyList(),
                searchCrawler.getSearchEntries(),
                elementCreationHandler.getAuxiliaryFiles(),
                new PageMeta());
    }
}
