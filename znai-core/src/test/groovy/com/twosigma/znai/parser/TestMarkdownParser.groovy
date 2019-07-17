package com.twosigma.znai.parser

import com.twosigma.znai.core.ComponentsRegistry
import com.twosigma.znai.parser.commonmark.MarkdownParser
import com.twosigma.znai.parser.docelement.DocElement
import com.twosigma.znai.search.PageSearchEntry
import com.twosigma.znai.search.SearchScore
import com.twosigma.znai.structure.PageMeta

import java.nio.file.Path

class TestMarkdownParser extends MarkdownParser {
    TestMarkdownParser(ComponentsRegistry componentsRegistry) {
        super(componentsRegistry)
    }

    @Override
    MarkupParserResult parse(Path path, String markdown) {
        def page = new DocElement('Page')
        def element = new DocElement('TestMarkdown', 'markdown', markdown)
        page.addChild(element)

        def searchEntry = new PageSearchEntry('dummy page section title', SearchScore.STANDARD.text(markdown))

        return new MarkupParserResult(page, [], [searchEntry], [], new PageMeta())
    }

    @Override
    void parse(Path path, String markdown, ParserHandler handler) {
        handler.onCustomNode('TestMarkdown', [markdown: markdown])
    }
}
