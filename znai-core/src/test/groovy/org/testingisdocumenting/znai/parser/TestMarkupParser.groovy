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

package org.testingisdocumenting.znai.parser

import org.testingisdocumenting.znai.parser.docelement.DocElement
import org.testingisdocumenting.znai.search.PageSearchEntry
import org.testingisdocumenting.znai.search.SearchScore
import org.testingisdocumenting.znai.structure.PageMeta

import java.nio.file.Path

class TestMarkupParser implements MarkupParser {
    @Override
    MarkupParserResult parse(Path path, String markup) {
        def page = new DocElement("Page")
        def element = new DocElement("TestMarkup", "markup", markup)
        page.addChild(element)

        def searchEntry = new PageSearchEntry(new PageSectionIdTitle('dummy page section title', [:]), [SearchScore.STANDARD.text(markup)])

        return new MarkupParserResult(page, [], [searchEntry], [], new PageMeta())
    }

    @Override
    PageMeta parsePageMetaOnly(String markup) {
        return new PageMeta()
    }
}
