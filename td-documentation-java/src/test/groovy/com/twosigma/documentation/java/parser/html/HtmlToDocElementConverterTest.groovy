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
        process('<a href="url">text inside</a>')

        elements.should == [[type: 'Paragraph', content:[[type: 'Link', url: 'url', content:
                [[text: 'text inside', type: 'SimpleText']]]]]]
    }

    @Test
    void "should replace pre with Snippet element"() {
        process('<pre>line of code</pre>')

        elements.should == [[type: 'Snippet', lang: '', lineNumber: '', maxLineLength: 12,
                             tokens:[[type: 'text', content: 'line of code']]]] // test tokenizer treats whole block as a single text element
    }

    private void process(String html) {
        def docElements = HtmlToDocElementConverter.convert(testComponentsRegistry, Paths.get(""), html)
        elements = docElements.collect { it.toMap() }
    }
}
