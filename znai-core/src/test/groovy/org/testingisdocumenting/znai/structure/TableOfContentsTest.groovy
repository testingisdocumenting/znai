/*
 * Copyright 2020 znai maintainers
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

import org.testingisdocumenting.znai.parser.PageSectionIdTitle
import org.junit.Test

class TableOfContentsTest {
    @Test
    void "should know which toc item is defined and which is not"() {
        def toc = new TableOfContents()
        toc.addTocItem("chapter1", "page-a")
        toc.addTocItem("chapter1", "page-b")

        toc.contains("chapter1", "page-a", "").should == true
        toc.contains("chapter1", "page-c", "").should == false

        toc.contains("chapter1", "page-a", "page section title").should == false
        def tocItem = toc.findTocItem("chapter1", "page-a")

        def pageSection1 = new PageSectionIdTitle("page section title")
        def pageSection2 = new PageSectionIdTitle("another title")
        tocItem.setPageSectionIdTitles([pageSection1, pageSection2])

        toc.contains("chapter1", "page-a", pageSection1.id).should == true
        toc.contains("chapter1", "page-a", "another title").should == false
    }

    @Test
    void "index page title should be a doc title"() {
        def toc = new TableOfContents()
        toc.addIndex("My Documentation")

        toc.index.pageTitle.should == "My Documentation"
    }

    @Test
    void "should detect newly added items by comparing with another toc"() {
        def toc = new TableOfContents()
        def updated = new TableOfContents()

        toc.detectNewTocItems(updated).should == []

        toc.addTocItem("chapter1", "page-a")
        toc.addTocItem("chapter1", "page-b")
        toc.addTocItem("chapter2", "page-c")

        updated.addTocItem("chapter1", "page-a")
        updated.addTocItem("chapter1", "page-e")
        updated.addTocItem("chapter2", "page-c")
        updated.addTocItem("chapter2", "page-d")

        def newItems = toc.detectNewTocItems(updated)
        newItems.should == ['dirName'  | 'fileNameWithoutExtension'] {
                           ________________________________________
                            'chapter1' | 'page-e'
                            'chapter2' | 'page-d'  }
    }

    @Test
    void "should detect removed items by comparing with another toc"() {
        def toc = new TableOfContents()
        def updated = new TableOfContents()

        toc.detectRemovedTocItems(updated).should == []

        toc.addTocItem("chapter1", "page-a")
        toc.addTocItem("chapter1", "page-e")
        toc.addTocItem("chapter2", "page-c")
        toc.addTocItem("chapter2", "page-d")

        updated.addTocItem("chapter1", "page-a")
        updated.addTocItem("chapter1", "page-b")
        updated.addTocItem("chapter2", "page-c")

        def removedItems = toc.detectRemovedTocItems(updated)
        removedItems.should == ['dirName'  | 'fileNameWithoutExtension'] {
                               ________________________________________
                                'chapter1' | 'page-e'
                                'chapter2' | 'page-d'  }
    }
}
