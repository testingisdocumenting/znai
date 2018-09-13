package com.twosigma.documentation.html

import com.twosigma.documentation.parser.PageSectionIdTitle
import com.twosigma.documentation.search.PageSearchEntries
import com.twosigma.documentation.search.PageSearchEntry
import com.twosigma.documentation.search.SearchScore
import com.twosigma.documentation.structure.TableOfContents
import org.junit.Test

class ServerSideSimplifiedRendererTest {
    TableOfContents toc = createToc()

    @Test
    void "should render TOC with page sections"() {
        ServerSideSimplifiedRenderer.renderToc(toc).should ==
                ServerSideSimplifiedRenderer.LOADING_INDICATOR +
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
        def searchEntries = new PageSearchEntries(
                toc.tocItems[0], [
                new PageSearchEntry('PS0', SearchScore.STANDARD.text('hello world')),
                new PageSearchEntry('PS1', SearchScore.STANDARD.text('of search'))])

        ServerSideSimplifiedRenderer.renderPageTextContent(searchEntries).should ==
                ServerSideSimplifiedRenderer.LOADING_INDICATOR +
                '<section style="max-width: 640px; margin-left: auto; margin-right: auto;">\n' +
                '<article>\n' +
                '<header><h1>PS0</h1></header>\n' +
                '<p>hello world</p>\n' +
                '</article>\n' +
                '\n' +
                '<article>\n' +
                '<header><h1>PS1</h1></header>\n' +
                '<p>of search</p>\n' +
                '</article>\n' +
                '</section>\n'
    }

    private static TableOfContents createToc() {
        def toc = new TableOfContents()
        toc.addTocItem('chapter-a', 'page-one')
        toc.addTocItem('chapter-a', 'page-two')
        toc.addTocItem('chapter-b', 'page-one')

        toc.tocItems[0].pageSectionIdTitles = [new PageSectionIdTitle('PS0')]
        toc.tocItems[1].pageSectionIdTitles = [new PageSectionIdTitle('PS1'), new PageSectionIdTitle('PS2')]
        toc.tocItems[2].pageSectionIdTitles = [new PageSectionIdTitle('PS3'), new PageSectionIdTitle('PS4')]

        return toc
    }
}
