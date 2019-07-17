package com.twosigma.znai.parser

import com.twosigma.znai.parser.docelement.DocElement
import com.twosigma.znai.search.PageSearchEntry
import com.twosigma.znai.search.SearchScore
import com.twosigma.znai.structure.PageMeta

import java.nio.file.Path

class TestMarkupParser implements MarkupParser {
    @Override
    MarkupParserResult parse(Path path, String markup) {
        def page = new DocElement("Page")
        def element = new DocElement("TestMarkup", "markup", markup)
        page.addChild(element)

        def searchEntry = new PageSearchEntry('dummy page section title', SearchScore.STANDARD.text(markup))

        return new MarkupParserResult(page, [], [searchEntry], [], new PageMeta())
    }
}
