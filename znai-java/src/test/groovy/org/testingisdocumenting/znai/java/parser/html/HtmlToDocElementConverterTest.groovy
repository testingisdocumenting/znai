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

package org.testingisdocumenting.znai.java.parser.html

import org.testingisdocumenting.znai.core.ComponentsRegistry
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.testingisdocumenting.znai.reference.DocReferences
import org.junit.Test

import java.nio.file.Paths

class HtmlToDocElementConverterTest {
    private List<Map<String, Object>> elements
    private String searchText

    @Test
    void "should replace standard style tags with doc elements"() {
        process("""
hello <code>world</code> 
<p>
another paragraph with 
<b>bold</b>
<i>italic</i>
<P>
second paragraph
""")
        elements.should == [[type: 'Paragraph', content: [[text: ' hello ', type: 'SimpleText'], [code: 'world', type: 'InlinedCode']]],
                            [type: 'Paragraph', content: [[text: ' another paragraph with ', type: 'SimpleText'],
                                                          [type: 'StrongEmphasis', content: [[text: 'bold', type: 'SimpleText']]],
                                                          [type: 'Emphasis', content: [[text: 'italic', type: 'SimpleText']]]]],
                            [type: 'Paragraph', content: [[text: ' second paragraph ', type: 'SimpleText']]]]

        searchText.should == 'hello world another paragraph with bolditalic second paragraph'
    }

    @Test
    void "should replace ahref with Link element"() {
        process('<a href="http://url">text inside</a>')

        elements.should == [[type: 'Paragraph', content: [[type: 'Link', isFile: false, url: 'http://url', content:
                [[text: 'text inside', type: 'SimpleText']]]]]]

        searchText.should == 'http url text inside'
    }

    @Test
    void "should replace pre with Snippet element"() {
        process('<pre>line of code</pre>')

        elements.should == [[type   : 'Snippet', lang: '', lineNumber: '',
                             snippet: 'line of code']]

        searchText.should == 'line of code'
    }

    @Test
    void "should replace ul and li with BulletList and ListItem elements"() {
        process('<ul><li>item 1</li><li>item 2</li></ul>')

        elements.should == [[type   : 'BulletList', bulletMarker: '*', tight: false,
                             content: [
                                     [type: 'ListItem', content: [[text: 'item 1', type: 'SimpleText']]],
                                     [type: 'ListItem', content: [[text: 'item 2', type: 'SimpleText']]]]]]

        searchText.should == 'item 1 item 2'
    }

    @Test
    void "should replace ol and li with BulletList and ListItem elements"() {
        process('<ol><li>item 1</li><li>item 2</li></ol>')

        elements.should == [[type   : 'OrderedList', startNumber: 1, delimiter: '.',
                             content: [
                                     [type: 'ListItem', content: [[text: 'item 1', type: 'SimpleText']]],
                                     [type: 'ListItem', content: [[text: 'item 2', type: 'SimpleText']]]]]]

        searchText.should == 'item 1 item 2'
    }

    @Test
    void "should handle unclosed tags"() {
        process("""Hello
<ul>
<li>item 1</li>
<li>item 2</li>
</ul>
some text
<p>
World paragraph
""")

        elements.should == [[type   : 'Paragraph',
                             content: [[text: 'Hello ', type: 'SimpleText']]],
                            [bulletMarker: '*', tight: false, type: 'BulletList',
                             content     : [[type: 'ListItem', content: [[text: 'item 1', type: 'SimpleText']]],
                                            [type: 'ListItem', content: [[text: 'item 2', type: 'SimpleText']]]]],
                            [type: 'Paragraph', content: [[text: ' some text ', type: 'SimpleText']]],
                            [type: 'Paragraph', content: [[text: ' World paragraph ', type: 'SimpleText']]]]

        searchText.should == 'Hello item 1 item 2 some text World paragraph'
    }

    @Test
    void "should add code references to inlined code when specified"() {
        process('<code>MyClass</code>', [MyClass: 'link/toRef'])

        elements.should == [[type   : 'Paragraph',
                             content: [[code: 'MyClass', references: [MyClass: [pageUrl: 'link/toRef']], type: 'InlinedCode']]]]

        searchText.should == 'MyClass'
    }

    @Test
    void "should parse markdown text if specified"() {
        processWithMarkdownParser('hello <code>inline</code>\n```\n' +
                'my snippet\n' +
                '```\n', [:])

        elements.should == [[type: 'Paragraph', content: [[type: 'SimpleText', text: "hello"]]],
                             [type: 'InlinedCode', code: 'inline'],
                             [type: 'Snippet', lineNumber: '', lang: '', snippet: 'my snippet\n']]
        searchText.should == 'hello inline my snippet'

    }

    private void process(String html, codeReferences = [:], isMarkdown = false) {
        processWithComponentsRegistry(TestComponentsRegistry.TEST_COMPONENTS_REGISTRY, html, codeReferences, isMarkdown)
    }

    private void processWithMarkdownParser(String html, codeReferences = [:]) {
        def componentsRegistry = new TestComponentsRegistry()
        componentsRegistry.markdownParser = new MarkdownParser(componentsRegistry)

        processWithComponentsRegistry(componentsRegistry, html, codeReferences, true)
    }

    private void processWithComponentsRegistry(ComponentsRegistry componentsRegistry, String html, codeReferences = [:], isMarkdown = false) {
        def docReferences = new DocReferences()
        codeReferences.each { k, v -> docReferences.add(k, v) }

        def docElementsAndSearchText = HtmlToDocElementConverter.convert(componentsRegistry, Paths.get(""), html,
                docReferences, isMarkdown)

        elements = docElementsAndSearchText.docElements.collect { it.toMap() }
        searchText = docElementsAndSearchText.searchText
    }
}
