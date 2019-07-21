/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
