/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.search

import org.testingisdocumenting.znai.extensions.PluginParams

import org.testingisdocumenting.znai.parser.HeadingProps
import org.testingisdocumenting.znai.reference.DocReferences
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*

class SearchCrawlerParserHandlerTest {
    SearchCrawlerParserHandler parserHandler

    @Before
    void init() {
        parserHandler = new SearchCrawlerParserHandler()
    }

    @Test
    void "should create search entry per section"() {
        parserHandler.onSectionStart("section one", HeadingProps.EMPTY)
        parserHandler.onSimpleText("hello")
        parserHandler.onSnippet(PluginParams.EMPTY, "", "", "source code")
        parserHandler.onInlinedCode("inlined term", DocReferences.EMPTY)
        parserHandler.onSectionEnd()

        parserHandler.onSectionStart("section two", HeadingProps.EMPTY)
        parserHandler.onSimpleText("world")
        parserHandler.onSnippet(PluginParams.EMPTY, "", "", "code")
        parserHandler.onInlinedCode("broker", DocReferences.EMPTY)
        parserHandler.onSectionEnd()

        parserHandler.getSearchEntries().should == [ "pageSectionTitle" | "searchText"] {
                                                     _______________________________
                                                     "section one"      | [text: "hello source code inlined term", score: SearchScore.STANDARD]
                                                     "section two"      | [text: "world code broker", score: SearchScore.STANDARD] }
    }

    @Test
    void "should connect mixed styles characters within a word without extra spaces"() {
        def searchEntries = withinSection {
            parserHandler.onSimpleText("H")
            parserHandler.onEmphasisStart()
            parserHandler.onSimpleText("el")
            parserHandler.onEmphasisEnd()
            parserHandler.onStrongEmphasisStart()
            parserHandler.onSimpleText("lo")
            parserHandler.onStrongEmphasisEnd()
        }

        searchEntries.searchText.text.should == ["Hello"]
    }

    @Test
    void "should separate entries based on soft line break"() {
        def searchEntries = withinSection {
            parserHandler.onSimpleText("entry one.")
            parserHandler.onSoftLineBreak()
            parserHandler.onSimpleText("entry two.")
        }

        searchEntries.searchText.text.should == ["entry one entry two"]
    }

    @Test
    void "should separate entries based on hard line break"() {
        def searchEntries = withinSection {
            parserHandler.onSimpleText("entry one.")
            parserHandler.onHardLineBreak()
            parserHandler.onSimpleText("entry two.")
        }

        searchEntries.searchText.text.should == ["entry one entry two"]
    }

    @Test
    void "should split on separators in code snippets"() {
        def searchEntries = withinSection {
            parserHandler.onInlinedCode("record.access", DocReferences.EMPTY)
            parserHandler.onHardLineBreak()
            parserHandler.onSimpleText("entry two ")
        }

        searchEntries.searchText.text.should == ["record access entry two"]
    }

    private withinSection(Closure setupCode) {
        parserHandler.onSectionStart("section", HeadingProps.EMPTY)
        setupCode()
        parserHandler.onSectionEnd()

        return parserHandler.getSearchEntries()
    }
}
