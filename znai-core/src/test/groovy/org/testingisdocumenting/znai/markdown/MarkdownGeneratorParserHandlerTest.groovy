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

import org.testingisdocumenting.znai.parser.MarkupParser
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class MarkdownGeneratorParserHandlerTest {
    static final MarkupParser parser = new MarkdownParser(TEST_COMPONENTS_REGISTRY)

    @Test
    void "should generate markdown for basic text"() {
        def result = process("Hello world")
        result.should == "Hello world\n\n"
    }

    @Test
    void "should generate markdown with text formatting"() {
        def result = process("This is **bold** and *italic* and ~~strikethrough~~")
        result.should == "This is **bold** and *italic* and ~~strikethrough~~\n\n"
    }

    @Test
    void "should generate headers with base level adjustment"() {
        def result = process("# Header")
        result.should == "# Header\n\n"

        result = process("# Header", 2)
        result.should == "### Header\n\n"
    }

    @Test
    void "should generate inline code"() {
        def result = process("Code: `hello()`")
        result.should == "Code: `hello()`\n\n"
    }

    @Test
    void "should generate code blocks"() {
        def result = process("```java\nSystem.out.println();\n```")
        result.should == "```java\nSystem.out.println();\n```\n\n"
    }

    @Test
    void "should generate lists"() {
        def result = process("- Item one\n- Item two")
        result.should == "- Item one\n- Item two\n\n"
    }

    @Test
    void "should generate block quotes"() {
        def result = process("> Quote")
        result.should == "> Quote\n\n\n"
    }

    @Test
    void "should generate thematic breaks"() {
        def result = process("Text\n\n---\n\nMore text")
        result.should == "Text\n\n---\n\nMore text\n\n"
    }

    @Test
    void "should handle multi-paragraph content"() {
        def result = process("First paragraph\n\nSecond paragraph")
        result.should == "First paragraph\n\nSecond paragraph\n\n"
    }

    @Test
    void "should handle complex document with proper header level shifting"() {
        def result = process("# Main Title\n\nContent here\n\n## Subtitle\n\nMore content with **bold**", 1)
        result.should == "## Main Title\n\nContent here\n\n### Subtitle\n\nMore content with **bold**\n\n"
    }

    @Test
    void "should preserve markdown formatting in complex content"() {
        def result = process("# Documentation\n\nThis has **bold**, *italic*, and `code`.\n\n```python\nprint('hello')\n```\n\n- List item\n- Another item")
        result.should == "# Documentation\n" +
                "\n" +
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
        def result = process("")
        result.should == ""
    }

    private static String process(String markdown, int baseHeadingLevel = 0) {
        def handler = new MarkdownGeneratorParserHandler(baseHeadingLevel)
        parser.parse(Paths.get("test.md"), handler, markdown)
        return handler.getMarkdown()
    }
}