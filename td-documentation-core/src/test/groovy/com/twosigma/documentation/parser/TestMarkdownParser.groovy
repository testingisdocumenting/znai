package com.twosigma.documentation.parser

import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.parser.commonmark.MarkdownParser
import com.twosigma.documentation.parser.docelement.DocElement
import com.twosigma.documentation.search.PageSearchEntry
import com.twosigma.documentation.search.SearchScore
import com.twosigma.documentation.structure.PageMeta

import java.nio.file.Path

/**
 * @author mykola
 */
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
