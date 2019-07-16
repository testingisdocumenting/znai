package com.twosigma.documentation.search

import com.twosigma.documentation.extensions.PluginParams
import org.junit.Before
import org.junit.Test

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
        def searchEntries = withinSection {
            parserHandler.onSimpleText('H')
            parserHandler.onEmphasisStart()
            parserHandler.onSimpleText('el')
            parserHandler.onEmphasisEnd()
            parserHandler.onStrongEmphasisStart()
            parserHandler.onSimpleText('lo')
            parserHandler.onStrongEmphasisEnd()
        }

        searchEntries.searchText.text.should == ['Hello']
    }

    @Test
    void "should separate entries based on soft line break"() {
        def searchEntries = withinSection {
            parserHandler.onSimpleText('entry one.')
            parserHandler.onSoftLineBreak()
            parserHandler.onSimpleText('entry two.')
        }

        searchEntries.searchText.text.should == ['entry one. entry two.']
    }

    @Test
    void "should separate entries based on hard line break"() {
        def searchEntries = withinSection {
            parserHandler.onSimpleText('entry one.')
            parserHandler.onHardLineBreak()
            parserHandler.onSimpleText('entry two.')
        }

        searchEntries.searchText.text.should == ['entry one. entry two.']
    }

    private withinSection(Closure setupCode) {
        parserHandler.onSectionStart('section')
        setupCode()
        parserHandler.onSectionEnd()

        return parserHandler.getSearchEntries()
    }
}
