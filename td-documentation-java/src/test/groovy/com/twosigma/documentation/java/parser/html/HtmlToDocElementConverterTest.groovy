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
hello world 
<p>
another paragraph with 
<b>bold</b>
<i>italic</i>

<P>
second paragraph
""")
        def elements = docElements.collect { it.toMap() }
        assert elements == [[type: 'Paragraph', content:[[text:' hello world ', type: 'SimpleText']]],
                            [type: 'Paragraph', content:[[text:' another paragraph with ', type: 'SimpleText'],
                                                         [type: 'StrongEmphasis', content:[[text: 'bold', type: 'SimpleText']]],
                                                         [type: 'Emphasis', content:[[text: 'italic', type: 'SimpleText']]]]],
                            [type: 'Paragraph', content:[[text:' second paragraph ', type: 'SimpleText']]]]
    }
}
