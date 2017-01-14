package com.twosigma.documentation.search

import com.twosigma.documentation.html.HtmlRenderContext
import com.twosigma.documentation.html.PageProps
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine
import com.twosigma.documentation.parser.MarkdownParser
import com.twosigma.documentation.parser.Page
import com.twosigma.documentation.structure.TocItem
import org.junit.Test

/**
 * @author mykola
 */
class LunrIndexerTest {
    @Test
    void "should build json index based on pages content"() {
        def engine = new ReactJsNashornEngine()
        def indexer = new LunrIndexer(engine)

        def markupParser = new MarkdownParser()
        def page = new Page(markupParser.parse("""# section 1
hello world
"""))

        def pageProps = new PageProps(new TocItem("dir-name", "file-name"), page, HtmlRenderContext.nested(1))
        def indexJson = indexer.createJsonIndex([pageProps])

        println indexJson
    }
}
