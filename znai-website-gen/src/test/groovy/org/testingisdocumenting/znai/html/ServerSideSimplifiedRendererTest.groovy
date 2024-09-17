/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.html

import org.testingisdocumenting.znai.parser.PageSectionIdTitle
import org.testingisdocumenting.znai.search.PageLocalSearchEntries
import org.testingisdocumenting.znai.search.PageSearchEntry
import org.testingisdocumenting.znai.search.SearchScore
import org.testingisdocumenting.znai.structure.TableOfContents
import org.junit.Test

class ServerSideSimplifiedRendererTest {
    TableOfContents toc = createToc()

    @Test
    void "should render TOC with page sections"() {
        ServerSideSimplifiedRenderer.renderToc(toc).should ==
                '<section style="max-width: 640px; margin-left: auto; margin-right: auto;">\n' +
                '<article>\n' +
                '<a href="chapter-a/page-one/">Page One</a>\n' +
                '<a href="chapter-a/page-one/#ps0">Page One PS0</a>\n' +
                '</article>\n' +
                '\n' +
                '<article>\n' +
                '<a href="chapter-a/page-two/">Page Two</a>\n' +
                '<a href="chapter-a/page-two/#ps1">Page Two PS1</a>\n' +
                '<a href="chapter-a/page-two/#ps2">Page Two PS2</a>\n' +
                '</article>\n' +
                '\n' +
                '<article>\n' +
                '<a href="chapter-b/page-one/">Page One</a>\n' +
                '<a href="chapter-b/page-one/#ps3">Page One PS3</a>\n' +
                '<a href="chapter-b/page-one/#ps4">Page One PS4</a>\n' +
                '</article>\n' +
                '</section>\n'
    }

    @Test
    void "should render simple page for crawl indexing"() {
        def searchEntries = new PageLocalSearchEntries(
                toc.tocItems[0], [
                new PageSearchEntry('PS0', SearchScore.STANDARD.text('hello \' " <> [] & world')),
                new PageSearchEntry('PS1', SearchScore.STANDARD.text('of search'))])

        ServerSideSimplifiedRenderer.renderPageTextContent(searchEntries).should ==
                ServerSideSimplifiedRenderer.LOADING_INDICATOR +
                '<section style="max-width: 640px; margin-left: auto; margin-right: auto;">\n' +
                '<article>\n' +
                '<header><h1>PS0</h1></header>\n' +
                '<p>hello \' &quot; &lt;&gt; [] &amp; world</p>\n' +
                '</article>\n' +
                '\n' +
                '<article>\n' +
                '<header><h1>PS1</h1></header>\n' +
                '<p>of search</p>\n' +
                '</article>\n' +
                '</section>\n'
    }

    private static TableOfContents createToc() {
        def toc = new TableOfContents("md")
        toc.addTocItem("chapter-a", 'page-one')
        toc.addTocItem("chapter-a", 'page-two')
        toc.addTocItem("chapter-b", 'page-one')

        toc.tocItems[0].pageSectionIdTitles = [new PageSectionIdTitle('PS0', [:])]
        toc.tocItems[1].pageSectionIdTitles = [new PageSectionIdTitle('PS1', [:]), new PageSectionIdTitle('PS2', [:])]
        toc.tocItems[2].pageSectionIdTitles = [new PageSectionIdTitle('PS3', [:]), new PageSectionIdTitle('PS4', [:])]

        return toc
    }
}
