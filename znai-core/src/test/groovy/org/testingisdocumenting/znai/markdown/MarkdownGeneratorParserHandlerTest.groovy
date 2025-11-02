/*
 * Copyright 2025 znai maintainers
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

package org.testingisdocumenting.znai.markdown

import org.testingisdocumenting.znai.extensions.include.DummyIncludePlugin
import org.testingisdocumenting.znai.extensions.PluginResult
import org.testingisdocumenting.znai.parser.MarkupParser
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.junit.Test

import java.nio.file.Paths
import java.util.stream.Collectors

import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class MarkdownGeneratorParserHandlerTest {
    static final MarkupParser parser = new MarkdownParser(TEST_COMPONENTS_REGISTRY)

    @Test
    void "should generate markdown for basic text"() {
        def result = pageMarkdownAsText("Hello world")
        result.should == "Hello world\n\n"
    }

    @Test
    void "should generate markdown with text formatting"() {
        def result = pageMarkdownAsText("This is **bold** and *italic* and ~~strikethrough~~")
        result.should == "This is **bold** and *italic* and ~~strikethrough~~\n\n"
    }

    @Test
    void "should generate inline code"() {
        def result = pageMarkdownAsText("Code: `hello()`")
        result.should == "Code: `hello()`\n\n"
    }

    @Test
    void "should generate code blocks"() {
        def result = pageMarkdownAsText("```java\nSystem.out.println();\n```")
        result.should == "```java\nSystem.out.println();\n```\n\n"
    }

    @Test
    void "should generate lists"() {
        def result = pageMarkdownAsText("- Item one\n- Item two")
        result.should == "- Item one\n- Item two\n\n"
    }

    @Test
    void "should generate block quotes"() {
        def result = pageMarkdownAsText("> Quote")
        result.should == "> Quote\n\n"
    }

    @Test
    void "should generate thematic breaks"() {
        def result = pageMarkdownAsText("Text\n\n---\n\nMore text")
        result.should == "Text\n\n---\n\nMore text\n\n"
    }

    @Test
    void "should handle multi-paragraph content"() {
        def result = pageMarkdownAsText("First paragraph\n\nSecond paragraph")
        result.should == "First paragraph\n\nSecond paragraph\n\n"
    }

    @Test
    void "should preserve markdown formatting in complex content"() {
        def result = pageMarkdown("# Documentation\n\nThis has **bold**, *italic*, and `code`.\n\n```python\nprint('hello')\n```\n\n- List item\n- Another item")

        result.sections().size().should == 1
        result.sections().get(0).title().should == "Documentation"
        result.sections().get(0).markdown().should ==
                "This has **bold**, *italic*, and `code`.\n" +
                "\n" +
                "```python\n" +
                "print('hello')\n" +
                "```\n" +
                "\n" +
                "- List item\n" +
                "- Another item\n\n"
    }

    @Test
    void "should handle empty input"() {
        def result = pageMarkdownAsText("")
        result.should == ""
    }

    @Test
    void "should use plugin markdownRepresentation method for include plugins"() {
        def handler = new MarkdownGeneratorParserHandler()
        def plugin = new DummyIncludePlugin()

        plugin.process(TEST_COMPONENTS_REGISTRY, handler, Paths.get("test.md"),
                      TEST_COMPONENTS_REGISTRY.pluginParamsFactory().create("dummy", "test-param", [:]))

        handler.onIncludePlugin(plugin, PluginResult.empty())
        handler.onParsingEnd()

        def result = joinSections(handler.getMarkdown())
        result.should == "**Dummy plugin content**: test-param\n\n"
    }

    @Test
    void "should separate content into sections by first-level headers"() {
        def markdown = """# Section One

Content in section one

## Subsection

More content

# Section Two

Content in section two"""

        def pageMarkdown =pageMarkdown(markdown)

        pageMarkdown.sections().size().should == 2

        def section1 = pageMarkdown.sections()[0]
        section1.title().should == "Section One"
        section1.id().should == "section-one"

        def section2 = pageMarkdown.sections()[1]
        section2.title().should == "Section Two"
        section2.id().should == "section-two"
    }

    @Test
    void "should capture content before first header in section with empty title"() {
        def markdown = """Content before any header

More content

# First Header

Content after header"""

        def pageMarkdown = pageMarkdown(markdown)

        pageMarkdown.sections().size().should == 2

        def section1 = pageMarkdown.sections()[0]
        section1.title().should == ""
        section1.id().should == ""
        section1.markdown().trim().should == "Content before any header\n\nMore content"
    }

    @Test
    void "should include markdown representation from include plugin in section"() {
        def markdown = """# Global

Content before include

:include-dummy: included-content
"""

        def pageMarkdown = pageMarkdown(markdown)

        pageMarkdown.sections().size().should == 1

        def section1 = pageMarkdown.sections()[0]
        section1.title().should == "Global"
        section1.id().should == "global"

        def sectionMd = section1.markdown()

        sectionMd.should contain("Content before include")
        sectionMd.should contain("**Dummy plugin content**: included-content")
    }

    private static String pageMarkdownAsText(String markdown) {
        return joinSections(pageMarkdown(markdown))
    }

    private static PageMarkdown pageMarkdown(String markdown) {
        def handler = new MarkdownGeneratorParserHandler()
        parser.parse(Paths.get("test.md"), handler, markdown)
        handler.onParsingEnd()

        return handler.getMarkdown()
    }

    private static String joinSections(PageMarkdown pageMarkdown) {
        if (pageMarkdown.sections().isEmpty()) {
            return ""
        }

        return pageMarkdown.sections().stream()
                .map(section -> section.markdown())
                .collect(Collectors.joining())
    }
}