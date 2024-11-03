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
    List<String> searchText

    @Before
    void init() {
        parserHandler = new SearchCrawlerParserHandler()
    }

    @Test
    void "should create search entry per section"() {
        parserHandler.onSectionStart("section one", HeadingProps.EMPTY,)
        parserHandler.onSimpleText("hello")
        parserHandler.onSnippet(PluginParams.EMPTY, "", "", "source code")
        parserHandler.onInlinedCode("inlined term", DocReferences.EMPTY)
        parserHandler.onSectionEnd()

        parserHandler.onSectionStart("section two", HeadingProps.EMPTY,)
        parserHandler.onSimpleText("world")
        parserHandler.onSnippet(PluginParams.EMPTY, "", "", "code")
        parserHandler.onInlinedCode("broker", DocReferences.EMPTY)
        parserHandler.onSectionEnd()

        parserHandler.getSearchEntries().should == [ "pageSectionTitle" | "searchTextList"] {
                                                     ______________________________________________________________________________
                                                     "section one"      | [[text: "hello", score: SearchScore.STANDARD], [text: "source code inlined term", score: SearchScore.HIGH]]
                                                     "section two"      | [[text: "world", score: SearchScore.STANDARD], [text: "code broker", score: SearchScore.HIGH]] }
    }

    @Test
    void "should connect mixed styles characters within a word without extra spaces"() {
        withinSection {
            parserHandler.onSimpleText("H")
            parserHandler.onEmphasisStart()
            parserHandler.onSimpleText("el")
            parserHandler.onEmphasisEnd()
            parserHandler.onStrongEmphasisStart()
            parserHandler.onSimpleText("lo")
            parserHandler.onStrongEmphasisEnd()
        }

        searchText.should == ["Hello"]
    }

    @Test
    void "should separate entries based on soft line break"() {
        withinSection {
            parserHandler.onSimpleText("entry one.")
            parserHandler.onSoftLineBreak()
            parserHandler.onSimpleText("entry two.")
        }

        searchText.should == ["entry one entry two"]
    }

    @Test
    void "should separate entries based on hard line break"() {
        withinSection {
            parserHandler.onSimpleText("entry one.")
            parserHandler.onHardLineBreak()
            parserHandler.onSimpleText("entry two.")
        }

        searchText.should == ["entry one entry two"]
    }

    @Test
    void "should split on separators in code snippets"() {
        withinSection {
            parserHandler.onInlinedCode("record.access", DocReferences.EMPTY)
        }

        searchText.should == ["record access"]
    }

    @Test
    void "should remove delimiters"() {
        withinSection {
            parserHandler.onSimpleText("\"hello\" world of 'quotes'. and separators,like!and?maybe/backward\\and[inside]different{brackets}and(other)" +
                    " --key=value")
        }

        searchText.should == ["hello world of quotes and separators like and maybe backward and " +
                                                         "inside different brackets and other key value"]
    }

    @Test
    void "should index text from inlined html"() {
        withinSection {
            parserHandler.onHtml("<ul><li>why use</li><li>it though</li></ul>", true)
        }

        searchText.should == ["why use it though"]
    }

    private withinSection(Closure setupCode) {
        parserHandler.onSectionStart("section", HeadingProps.EMPTY,)
        setupCode()
        parserHandler.onSectionEnd()

        def searchEntries = parserHandler.getSearchEntries()
        searchText = searchEntries.collect { it.extractText() }
    }
}
