package com.twosigma.documentation.java.parser.html

import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.parser.TestComponentsRegistry
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class HtmlToDocElementConverterTest {
    private static ComponentsRegistry testComponentsRegistry = new TestComponentsRegistry()
    private List<Map<String,Object>> elements

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
        elements.should == [[type: 'Paragraph', content:[[text:' hello ', type: 'SimpleText'], [code:'world', type: 'InlinedCode']]],
                            [type: 'Paragraph', content:[[text:' another paragraph with ', type: 'SimpleText'],
                                                         [type: 'StrongEmphasis', content:[[text: 'bold', type: 'SimpleText']]],
                                                         [type: 'Emphasis', content:[[text: 'italic', type: 'SimpleText']]]]],
                            [type: 'Paragraph', content:[[text:' second paragraph ', type: 'SimpleText']]]]
    }

    @Test
    void "should replace ahref with Link element"() {
        process('<a href="http://url">text inside</a>')

        elements.should == [[type: 'Paragraph', content:[[type: 'Link', url: 'http://url', content:
                [[text: 'text inside', type: 'SimpleText']]]]]]
    }

    @Test
    void "should replace pre with Snippet element"() {
        process('<pre>line of code</pre>')

        elements.should == [[type: 'Snippet', lang: '', lineNumber: '', maxLineLength: 12,
                             tokens:[[type: 'text', content: 'line of code']]]] // test tokenizer treats whole block as a single text element
    }

    @Test
    void "should replace ul and li with BulletList and ListItem elements"() {
        process('<ul><li>item 1</li><li>item 2</li></ul>')

        elements.should == [[type: 'BulletList', bulletMarker: '*', tight: false,
                             content: [
                                     [type: 'ListItem', content: [[text: 'item 1', type: 'SimpleText']]],
                                     [type: 'ListItem', content: [[text: 'item 2', type: 'SimpleText']]]]]]
    }

    @Test
    void "should replace ol and li with BulletList and ListItem elements"() {
        process('<ol><li>item 1</li><li>item 2</li></ol>')

        elements.should == [[type: 'OrderedList', startNumber: 1, delimiter: '.',
                             content: [
                                     [type: 'ListItem', content: [[text: 'item 1', type: 'SimpleText']]],
                                     [type: 'ListItem', content: [[text: 'item 2', type: 'SimpleText']]]]]]
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

        elements.should == [[type: 'Paragraph',
                             content: [[text: 'Hello ', type: 'SimpleText']]],
                            [bulletMarker: '*', tight: false, type: 'BulletList',
                                content:[[type: 'ListItem', content: [[text: 'item 1', type: 'SimpleText']]],
                                         [type: 'ListItem', content: [[text: 'item 2', type: 'SimpleText']]]]],
                            [type: 'Paragraph', content:[[text: ' some text ', type: 'SimpleText']]],
                            [type: 'Paragraph', content:[[text: ' World paragraph ', type: 'SimpleText']]]]
    }

    private void process(String html) {
        def docElements = HtmlToDocElementConverter.convert(testComponentsRegistry, Paths.get(""), html)
        elements = docElements.collect { it.toMap() }
    }
}
