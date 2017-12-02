package com.twosigma.documentation.parser.sphinx

import com.twosigma.documentation.parser.MarkupParser
import com.twosigma.documentation.parser.TestComponentsRegistry
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class SphinxDocTreeParserTest {
    static final TestComponentsRegistry componentsRegistry = new TestComponentsRegistry()
    static final MarkupParser parser = new SphinxDocTreeParser(componentsRegistry)
    List<Map<String,Object>> content

    @Test
    void "section"() {
        parse("""
<document source="path">
    <section ids="indices-and-tables" names="indices\\ and\\ tables">
        <title>Indices and tables</title>
        <paragraph>Hello World for real</paragraph>
    </section>
</document>
""")

        content.should == [[title: 'Indices and tables', id: 'indices-and-tables', type: 'Section',
                            content:[[type: 'Paragraph', content: [[text: 'Hello World for real', type: 'SimpleText']]]]]]
    }

    @Test
    void "snippet"() {
        parse("""<literal_block highlight_args="{}" language="java" 
linenos="False" xml:space="preserve">System.out.println("hello world");</literal_block>""")

        content.should == [[lang: 'java', maxLineLength: 34,
                            tokens: [[type: 'text',
                                      content: 'System.out.println("hello world");']],
                            lineNumber: '', type: 'Snippet']]
    }

    @Test
    void "emphasis and strong text"() {
        parse('<paragraph><emphasis>mdoc</emphasis> <strong>table of contents</strong></paragraph>')

        content.should == [[type: 'Paragraph', content: [[type: 'Emphasis', content:[[text: 'mdoc', type: 'SimpleText']]], [text: ' ', type: 'SimpleText'],
                                                         [type: 'StrongEmphasis', content:[[text: 'table of contents', type: 'SimpleText']]]]]]
    }

    private void parse(String xml) {
        def parseResult = parser.parse(Paths.get("test.xml"), xml)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
