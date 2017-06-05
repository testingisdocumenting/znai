package com.twosigma.documentation.java.parser.html

import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class HtmlToDocElementConverterTest {
    @Test
    void "should replace standard tags with doc elements"() {
        def docElements = HtmlToDocElementConverter.convert(null, Paths.get(""), """
hello <code>world</code> 
<p>
another paragraph with 
<b>bold</b>
<i>italic</i>
<a href="url">text inside</a>
<P>
second paragraph
""")
        def elements = docElements.collect { it.toMap() }
        elements.should == [[type: 'Paragraph', content:[[text:' hello ', type: 'SimpleText'], [code:'world', type: 'InlinedCode']]],
                            [type: 'Paragraph', content:[[text:' another paragraph with ', type: 'SimpleText'],
                                                         [type: 'StrongEmphasis', content:[[text: 'bold', type: 'SimpleText']]],
                                                         [type: 'Emphasis', content:[[text: 'italic', type: 'SimpleText']]],
                                                         [type: 'Link', url: 'url', content: [[text: 'text inside', type: 'SimpleText']]]]],
                            [type: 'Paragraph', content:[[text:' second paragraph ', type: 'SimpleText']]]]
    }
}
