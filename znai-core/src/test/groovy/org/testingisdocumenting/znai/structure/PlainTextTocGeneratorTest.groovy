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

package org.testingisdocumenting.znai.structure

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class PlainTextTocGeneratorTest {
    @Test
    void "should create top level TOC from nested text structure"() {
        def toc = new PlainTextTocGenerator("md").generate("""
chapter1
    page-a
    page-b
chapter2
    page-c""")

        toc.toListOfMaps().should == [[chapterTitle: 'Chapter1', dirName: 'chapter1',
                                       items       : [[chapterTitle       : 'Chapter1', pageTitle: 'Page A', fileName: 'page-a', dirName: 'chapter1',
                                                       fileExtension: "md",
                                                       pageSectionIdTitles: [], pageMeta: [:], viewOnRelativePath: null],
                                                      [chapterTitle       : 'Chapter1', pageTitle: 'Page B', fileName: 'page-b', dirName: 'chapter1',
                                                       fileExtension: "md",
                                                       pageSectionIdTitles: [], pageMeta: [:],  viewOnRelativePath: null]]],
                                      [chapterTitle: 'Chapter2', dirName: 'chapter2',
                                       items       :
                                               [[chapterTitle       : 'Chapter2', pageTitle: 'Page C', fileName: 'page-c', dirName: 'chapter2',
                                                 fileExtension: "md",
                                                 pageSectionIdTitles: [], pageMeta: [:],  viewOnRelativePath: null]]]]
    }

    @Test
    void "should override chapter title"() {
        def toc = new PlainTextTocGenerator("md").generate("""
chapter1 
    page-a
    page-b
chapter2 {title: "chapter TWO"}
    page-c""")

        def tocItem = toc.findTocItem("chapter2", "page-c")
        tocItem.getChapterTitle().should == "chapter TWO"
    }

    @Test
    void "should support standalone pages with file extension"() {
        def toc = new PlainTextTocGenerator("md").generate("""
overview.md
chapter1
    page-a
    page-b
api-reference.md
changelog.md""")

        def overview = toc.findTocItem("", "overview")
        overview.should != null
        overview.getFilePath().should == "overview.md"
        overview.getChapterTitle().should == ""
        overview.getPageTitle().should == "Overview"

        def apiRef = toc.findTocItem("", "api-reference")
        apiRef.should != null
        apiRef.getFilePath().should == "api-reference.md"
        apiRef.getPageTitle().should == "Api Reference"

        def pageA = toc.findTocItem("chapter1", "page-a")
        pageA.should != null
        pageA.getFilePath().should == "chapter1/page-a.md"
    }

    @Test
    void "should support all standalone pages without chapters"() {
        def toc = new PlainTextTocGenerator("md").generate("""
getting-started.md
installation.md
configuration.md
troubleshooting.md""")

        toc.getTocItems().size().should == 4

        def gettingStarted = toc.findTocItem("", "getting-started")
        gettingStarted.getFilePath().should == "getting-started.md"
        gettingStarted.getPageTitle().should == "Getting Started"
    }

    @Test  
    void "should handle different file extensions for standalone pages"() {
        def toc = new PlainTextTocGenerator("md").generate("""
readme.mdx
config.md
chapter1
    page-a
api.mmx""")

        def readme = toc.findTocItem("", "readme")
        readme.getFileExtension().should == "mdx"
        readme.getFilePath().should == "readme.mdx"

        def api = toc.findTocItem("", "api")
        api.getFileExtension().should == "mmx"
        api.getFilePath().should == "api.mmx"
    }

    @Test
    void "should support mixed format with standalone pages and chapters"() {
        def toc = new PlainTextTocGenerator("md").generate("""
introduction.md
getting-started.md
fundamentals
    concepts
    terminology
    examples
advanced
    performance
    troubleshooting
api-reference.md
changelog.md""")

        def items = toc.getTocItems()
        items[0].getPageTitle().should == "Introduction"
        
        items[1].getPageTitle().should == "Getting Started"
        
        items[2].getDirName().should == "fundamentals"
        items[2].getChapterTitle().should == "Fundamentals"
        
        items[5].getDirName().should == "advanced"
        
        items[7].getPageTitle().should == "Api Reference"
    }

    @Test
    void "should handle empty toc"() {
        def toc = new PlainTextTocGenerator("md").generate("")
        toc.getTocItems().size().should == 0
    }

    @Test
    void "should handle whitespace and empty lines"() {
        def toc = new PlainTextTocGenerator("md").generate("""

overview.md

chapter1
    page-a
    
    page-b

api.md

""")

        toc.getTocItems().size().should == 4
        toc.findTocItem("", "overview").should != null
        toc.findTocItem("chapter1", "page-a").should != null
        toc.findTocItem("chapter1", "page-b").should != null
        toc.findTocItem("", "api").should != null
    }

    @Test
    void "should handle standalone pages with JSON options"() {
        def toc = new PlainTextTocGenerator("md").generate("""
overview.md {title: "Project Overview"}
chapter1
    page-a
api-reference.md {title: "API Docs"}""")

        def overview = toc.findTocItem("", "overview")
        overview.getPageTitle().should == "Project Overview"
        
        def api = toc.findTocItem("", "api-reference")
        api.getPageTitle().should == "API Docs"
    }

    @Test
    void "should throw error for indented page without chapter"() {
        code {
            new PlainTextTocGenerator("md").generate("""
    page-without-chapter
""")
        } should throwException(IllegalArgumentException, 
                "chapter is not specified, use a line without indentation to specify a chapter")
    }
}
