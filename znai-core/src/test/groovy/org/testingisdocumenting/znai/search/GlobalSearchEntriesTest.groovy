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

package org.testingisdocumenting.znai.search

import org.junit.Test
import org.testingisdocumenting.znai.core.DocMeta
import org.testingisdocumenting.znai.markdown.PageMarkdownSection
import org.testingisdocumenting.znai.structure.TocItem

class GlobalSearchEntriesTest {
    @Test
    void "should generate XML document"() {
        def docMeta = new DocMeta([title: 'Test Doc'])
        def tocItem1 = new TocItem('chapter1', 'page1', 'md')
        tocItem1.setPageTitleIfNoTocOverridePresent('page 1')
        tocItem1.setChapterTitle('chapter 1')
        def section1 = new PageMarkdownSection('section1', 'section 1', 'text 1')

        def tocItem2 = new TocItem('chapter2', 'page2', 'md')
        tocItem2.setPageTitleIfNoTocOverridePresent('page 2')
        tocItem2.setChapterTitle('chapter 2')
        def section2 = new PageMarkdownSection('section2', 'section 2', 'text 2')

        def tocItemIndex = TocItem.createIndex()
        def sectionIndex = new PageMarkdownSection('', '', 'index text')

        def entries = new GlobalSearchEntries()
        entries.addAll([
                new GlobalSearchEntry(docMeta, tocItem1, section1, '/doc-id/title1'),
                new GlobalSearchEntry(docMeta, tocItem2, section2, '/doc-id/title2'),
                new GlobalSearchEntry(docMeta, tocItemIndex, sectionIndex, '/doc-id')])

        println entries.toXml()
        entries.toXml().should == '<znai>\n' +
                '  <entry>\n' +
                '    <url>/doc-id/title1</url>\n' +
                '    <fullTitle>Test Doc: page 1, section 1 [chapter 1]</fullTitle>\n' +
                '    <pageTitle>page 1</pageTitle>\n' +
                '    <chapterTitle>chapter 1</chapterTitle>\n' +
                '    <pageSectionTitle>section 1</pageSectionTitle>\n' +
                '    <text>\n' +
                '      <score>STANDARD</score>\n' +
                '      <text>text 1</text>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '  <entry>\n' +
                '    <url>/doc-id/title2</url>\n' +
                '    <fullTitle>Test Doc: page 2, section 2 [chapter 2]</fullTitle>\n' +
                '    <pageTitle>page 2</pageTitle>\n' +
                '    <chapterTitle>chapter 2</chapterTitle>\n' +
                '    <pageSectionTitle>section 2</pageSectionTitle>\n' +
                '    <text>\n' +
                '      <score>STANDARD</score>\n' +
                '      <text>text 2</text>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '  <entry>\n' +
                '    <url>/doc-id</url>\n' +
                '    <fullTitle>Test Doc</fullTitle>\n' +
                '    <pageTitle></pageTitle>\n' +
                '    <chapterTitle></chapterTitle>\n' +
                '    <pageSectionTitle></pageSectionTitle>\n' +
                '    <text>\n' +
                '      <score>STANDARD</score>\n' +
                '      <text>index text</text>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '</znai>\n'
    }

    @Test
    void "should handle ansi sequences"() {
        def docMeta = new DocMeta([title: 'title', type: 'Guide'])
        def tocItem = new TocItem('chapter', 'page', 'md')
        tocItem.setPageTitleIfNoTocOverridePresent('page')
        tocItem.setChapterTitle('chapter')
        def section = new PageMarkdownSection('section', 'section',
                "\u001B[1mwebtau:\u001B[m000\u001B" +
                        "[1m>\u001B[m http.get(\"https://jsonplaceholder.typicode.com/todos/1\")" +
                        " \u001B[33m> (\u001B[32m342ms\u001B[33m)\u001B[0m")

        def entries = new GlobalSearchEntries()
        entries.addAll([
                new GlobalSearchEntry(docMeta, tocItem, section, '/doc-id/title')])

        entries.toXml().should == '<znai>\n' +
                '  <entry>\n' +
                '    <url>/doc-id/title</url>\n' +
                '    <fullTitle>title: page, section [chapter]</fullTitle>\n' +
                '    <pageTitle>page</pageTitle>\n' +
                '    <chapterTitle>chapter</chapterTitle>\n' +
                '    <pageSectionTitle>section</pageSectionTitle>\n' +
                '    <text>\n' +
                '      <score>STANDARD</score>\n' +
                '      <text>webtau:000> http.get("https://jsonplaceholder.typicode.com/todos/1") > (342ms)</text>\n' +
                '    </text>\n' +
                '  </entry>\n' +
                '</znai>\n'
    }
}
