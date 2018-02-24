package com.twosigma.documentation.parser

import com.twosigma.documentation.parser.docelement.DocElement
import com.twosigma.documentation.search.PageSearchEntry
import com.twosigma.documentation.search.SearchScore

import java.nio.file.Path

/**
 * @author mykola
 */
class TestMarkupParser implements MarkupParser {
    @Override
    MarkupParserResult parse(Path path, String markup) {
        def page = new DocElement("Page")
        def element = new DocElement("TestMarkup", "markup", markup)
        page.addChild(element)

        def searchEntry = new PageSearchEntry('dummy page section title', SearchScore.STANDARD.text(markup))

        return new MarkupParserResult(page, [], [searchEntry], [], properties)
    }
}
