package com.twosigma.documentation.extensions.templates

import com.twosigma.documentation.parser.MarkdownParser
import com.twosigma.documentation.parser.MarkupParser
import com.twosigma.documentation.parser.MarkupParserResult
import com.twosigma.documentation.parser.TestComponentsRegistry
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.testing.Ddjt.equal

/**
 * @author mykola
 */
class TemplateIncludePluginTest {
    private static TestComponentsRegistry componentsRegistry
    private static MarkupParser parser

    private List<Map> content
    private MarkupParserResult parseResult

    @BeforeClass
    static void init() {
        componentsRegistry = new TestComponentsRegistry()
        parser = new MarkdownParser(componentsRegistry)

        componentsRegistry.defaultParser = parser
    }

    @Test
    void "level one headings should merge with the rest of the document"() {
        parse("""# header one

text

:include-template: test-template.md

# header two
""")

        content.should equal([[title  : 'header one', id: 'header-one', type: 'Section',
                               content: [[type   : 'Paragraph',
                                          content: [[text: 'text', type: 'SimpleText']]],
                                         [type: 'Paragraph', content: [[text: 'text before section', type: 'SimpleText']]]]],
                              [title  : 'Template Header One', id: 'template-header-one', type: 'Section',
                               content: [[type: 'Paragraph', content: [[text: 'template body one', type: 'SimpleText']]]]],
                              [title  : 'Template Header Two', id: 'template-header-two', type: 'Section',
                               content: [[type: 'Paragraph', content: [[text: 'template body two', type: 'SimpleText']]]]],
                              [title: 'header two', id: 'header-two', type: 'Section']])
    }

    private void parse(String markdown) {
        parseResult = parser.parse(Paths.get("test.md"), markdown)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
