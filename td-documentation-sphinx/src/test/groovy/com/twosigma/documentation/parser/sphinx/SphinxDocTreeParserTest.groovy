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

    private void parse(String xml) {
        def parseResult = parser.parse(Paths.get("test.xml"), xml)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
