package com.twosigma.documentation.search

import com.twosigma.documentation.html.PageProps
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine
import com.twosigma.documentation.parser.MarkdownParser
import com.twosigma.documentation.parser.Page
import com.twosigma.documentation.parser.TestComponentsRegistry
import com.twosigma.documentation.structure.TocItem
import org.junit.Ignore
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class LunrIndexerTest {
    @Test
    @Ignore
    // TODO finish test
    void "should build json index based on pages content"() {
        def engine = new ReactJsNashornEngine()
        def indexer = new LunrIndexer(engine)

        def markupParser = new MarkdownParser(new TestComponentsRegistry())
        def page = new Page(markupParser.parse(Paths.get(""), """# section 1
hello world
""").docElement)

        def item = new TocItem("dir-name", "file-name")
        item.setPageSectionIdTitles(Collections.emptyList())

        def pageProps = new PageProps(item, page)
        def indexJson = indexer.createJsonIndex([pageProps])

        println indexJson
    }
}
