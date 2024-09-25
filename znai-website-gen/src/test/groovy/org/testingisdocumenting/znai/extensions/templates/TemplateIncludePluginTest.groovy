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

package org.testingisdocumenting.znai.extensions.templates

import org.testingisdocumenting.znai.parser.MarkupParser
import org.testingisdocumenting.znai.parser.MarkupParserResult
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

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

        content.should == [[title  : 'header one', id: 'header-one', additionalIds: [], type: 'Section',
                            content: [[type   : 'Paragraph',
                                       content: [[text: 'text', type: 'SimpleText']]],
                                      [type: 'Paragraph', content: [[text: 'text before section', type: 'SimpleText']]]]],
                           [title  : 'Template Header One', id: 'template-header-one', additionalIds: [], type: 'Section',
                            content: [[type: 'Paragraph', content: [[text: 'template body one', type: 'SimpleText']]]]],
                           [title  : 'Template Header Two', id: 'template-header-two', additionalIds: [], type: 'Section',
                            content: [[type: 'Paragraph', content: [[text: 'template body two', type: 'SimpleText']]]]],
                           [title: 'header two', id: 'header-two', additionalIds: [], type: 'Section']]
    }

    private void parse(String markdown) {
        parseResult = parser.parse(Paths.get("test.md"), markdown)
        content = parseResult.docElement().getContent().collect { it.toMap() }
    }
}
