package com.twosigma.documentation.search

import com.twosigma.documentation.extensions.PluginParams
import org.junit.Before
import org.junit.Test

/**
 * @author mykola
 */
class SearchCrawlerParserHandlerTest {
    SearchCrawlerParserHandler parserHandler

    @Before
    void init() {
        parserHandler = new SearchCrawlerParserHandler()
    }

    @Test
    void "should create search entry per section"() {
        parserHandler.onSectionStart('section one')
        parserHandler.onSimpleText('hello')
        parserHandler.onSnippet(PluginParams.EMPTY, '', '', 'source code')
        parserHandler.onInlinedCode('inlined term')
        parserHandler.onSectionEnd()

        parserHandler.onSectionStart('section two')
        parserHandler.onSimpleText('world')
        parserHandler.onSnippet(PluginParams.EMPTY, '', '', 'code')
        parserHandler.onInlinedCode('broker')
        parserHandler.onSectionEnd()

        parserHandler.getSearchEntries().should == [ 'pageSectionTitle' | 'searchText'] {
                                                     _______________________________
                                                     'section one'      | [text: 'hello source code inlined term', score: SearchScore.STANDARD]
                                                     'section two'      | [text: 'world code broker', score: SearchScore.STANDARD] }
    }

    @Test
    void "should connect mixed styles characters within a word without extra spaces"() {
        parserHandler.onSectionStart('section')
        parserHandler.onSimpleText('H')
        parserHandler.onEmphasisStart()
        parserHandler.onSimpleText('el')
        parserHandler.onEmphasisEnd()
        parserHandler.onStrongEmphasisStart()
        parserHandler.onSimpleText('lo')
        parserHandler.onStrongEmphasisEnd()
        parserHandler.onSectionEnd()

        parserHandler.getSearchEntries().searchText.text.should == ['Hello']
    }
}
