package com.twosigma.documentation.parser

import com.twosigma.documentation.parser.docelement.DocElement

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

        return new MarkupParserResult(page, [])
    }
}
