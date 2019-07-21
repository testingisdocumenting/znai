/*
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

package com.twosigma.znai.structure

import com.twosigma.znai.parser.PageSectionIdTitle
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
}
