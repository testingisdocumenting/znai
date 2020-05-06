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

package org.testingisdocumenting.znai.structure

import org.junit.Test

class PlainTextTocGeneratorTest {
    @Test
    void "should create top level TOC from nested text structure"() {
        def toc = new PlainTextTocGenerator().generate("""
chapter1
    page-a
    page-b
chapter2
    page-c""")

        toc.toListOfMaps().should == [[sectionTitle: 'Chapter1', dirName: 'chapter1',
                                       items       : [[sectionTitle       : 'Chapter1', pageTitle: 'Page A', fileName: 'page-a', dirName: 'chapter1',
                                                       pageSectionIdTitles: [], pageMeta: [:], viewOnRelativePath: null],
                                                      [sectionTitle       : 'Chapter1', pageTitle: 'Page B', fileName: 'page-b', dirName: 'chapter1',
                                                       pageSectionIdTitles: [], pageMeta: [:],  viewOnRelativePath: null]]],
                                      [sectionTitle: 'Chapter2', dirName: 'chapter2',
                                       items       :
                                               [[sectionTitle       : 'Chapter2', pageTitle: 'Page C', fileName: 'page-c', dirName: 'chapter2',
                                                 pageSectionIdTitles: [], pageMeta: [:],  viewOnRelativePath: null]]]]
    }
}
