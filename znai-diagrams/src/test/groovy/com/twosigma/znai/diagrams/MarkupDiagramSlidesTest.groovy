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

package com.twosigma.znai.diagrams

import com.twosigma.znai.diagrams.slides.DiagramSlides
import com.twosigma.znai.diagrams.slides.MarkupDiagramSlides
import com.twosigma.znai.parser.TestComponentsRegistry
import com.twosigma.znai.parser.commonmark.MarkdownParser
import org.junit.Test

import java.nio.file.Paths

class MarkupDiagramSlidesTest {
    private DiagramSlides slides

    static twoSimpleSections = """
# server
server text

# client
client text
"""

    @Test
    void "level 1 header should be treated as id"() {
        parse(twoSimpleSections)

        assert slides.slides.collect { it.ids }.flatten() == ['server', 'client']
    }

    @Test
    void "empty section should be used to define multiple ids"() {
        parse("""
# sub_system_a
# sub_system_b

context information
""")

        assert slides.slides.size() == 1
        assert slides.slides[0].ids == ['sub_system_a', 'sub_system_b']
    }

    @Test
    void "slide content should be doc elements based on markdown"() {
        parse(twoSimpleSections)

        assert slides.slides[0].content*.toMap() == [[type: 'Paragraph', content: [[text: 'server text', type: 'SimpleText']]]]
        assert slides.slides[1].content*.toMap() == [[type: 'Paragraph', content: [[text: 'client text', type: 'SimpleText']]]]
    }

    @Test
    void "slides should be represented as list of maps for client side"() {
        parse(twoSimpleSections)

        assert slides.toListOfMaps() == [[ids: ['server'],
                                          content:[[type: 'Paragraph', content:[[text: 'server text', type: 'SimpleText']]]]],
                                         [ids: ['client'],
                                          content:[[type: 'Paragraph', content:[[text: 'client text', type: 'SimpleText']]]]]]

    }

    private void parse(markdown) {
        def diagramSlides = new MarkupDiagramSlides(new MarkdownParser(TestComponentsRegistry.INSTANCE))
        slides = diagramSlides.create(Paths.get(""), markdown)
    }
}
