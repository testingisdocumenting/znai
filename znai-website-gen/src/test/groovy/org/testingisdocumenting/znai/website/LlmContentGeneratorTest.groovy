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

package org.testingisdocumenting.znai.website

import org.junit.Test
import org.testingisdocumenting.znai.core.DocMeta
import org.testingisdocumenting.znai.parser.MarkupParserResult
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.testingisdocumenting.znai.structure.Page
import org.testingisdocumenting.znai.structure.PageMeta
import org.testingisdocumenting.znai.structure.TableOfContents

import java.nio.file.Paths
import java.time.Instant

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class LlmContentGeneratorTest {
    static final parser = new MarkdownParser(TEST_COMPONENTS_REGISTRY)

    @Test
    void "wraps long paragraphs at 120 characters"() {
        def markdown = """# Section Title

`Znai` combines human written text with artifacts such as `code`, `graphs`, `REST API`, `Java Docs`, `Doxygen`, etc. to create up-to-date, maintainable, beautiful **User Guides** and **Tutorials**."""

        def content = generateContent(markdown)

        content.should == """[//]: # (this is an auto generated file)
"Test Guide" full guide:

# Chapter :: Page One :: Section Title
answer-link: https://example.com/test/chapter/page-one#section-title

`Znai` combines human written text with artifacts such as `code`, `graphs`, `REST API`, `Java Docs`, `Doxygen`, etc. to
create up-to-date, maintainable, beautiful **User Guides** and **Tutorials**.
"""
    }

    @Test
    void "does not wrap code blocks"() {
        def markdown = """# Code Example

Some text before.

```java
public class VeryLongClassNameThatShouldNotBeWrappedBecauseItIsInsideACodeBlockAndWrappingWouldBreakTheCode {
    // code here
}
```

Some text after."""

        def content = generateContent(markdown)

        content.should == """[//]: # (this is an auto generated file)
"Test Guide" full guide:

# Chapter :: Page One :: Code Example
answer-link: https://example.com/test/chapter/page-one#code-example

Some text before.

```java
public class VeryLongClassNameThatShouldNotBeWrappedBecauseItIsInsideACodeBlockAndWrappingWouldBreakTheCode {
    // code here
}
```

Some text after.
"""
    }

    @Test
    void "wraps bullet list items"() {
        def markdown = """# List Section

- This is a very long bullet point that contains a lot of text and should wrap to the next line when it exceeds the maximum width
- Short bullet
1. Numbered item that is also very long and should wrap properly when the text exceeds the maximum allowed width for a single line
2. Short numbered item"""

        def content = generateContent(markdown)

        content.should == """[//]: # (this is an auto generated file)
"Test Guide" full guide:

# Chapter :: Page One :: List Section
answer-link: https://example.com/test/chapter/page-one#list-section

- This is a very long bullet point that contains a lot of text and should wrap to the next line when it exceeds the
maximum width
- Short bullet

1. Numbered item that is also very long and should wrap properly when the text exceeds the maximum allowed width for a
single line
2. Short numbered item
"""
    }

    @Test
    void "handles nested bullet lists"() {
        def markdown = """# Features

* Markdown with custom extensions and dozens of plugins:
  * Content from external files with markers and filters support
  * Simplified extraction of a function body content (working with examples)
  * Embedding of JavaDoc/PyDoc documentation text, preserving styles
  * Beautiful API documentation capabilities
  * Two Sides Page Layout with convenient examples language switch
  * Rich visuals like flow diagrams and charts
  * etc
* Dev server mode with changes highlight and auto-jump to a change
* Local search (with full preview)
* Dark/light mode runtime switch
* Presentation Mode to automatically turn your documentation into slides, using the same content"""

        def content = generateContent(markdown)

        content.should == """[//]: # (this is an auto generated file)
"Test Guide" full guide:

# Chapter :: Page One :: Features
answer-link: https://example.com/test/chapter/page-one#features

* Markdown with custom extensions and dozens of plugins:
  * Content from external files with markers and filters support
  * Simplified extraction of a function body content (working with examples)
  * Embedding of JavaDoc/PyDoc documentation text, preserving styles
  * Beautiful API documentation capabilities
  * Two Sides Page Layout with convenient examples language switch
  * Rich visuals like flow diagrams and charts
  * etc
* Dev server mode with changes highlight and auto-jump to a change
* Local search (with full preview)
* Dark/light mode runtime switch
* Presentation Mode to automatically turn your documentation into slides, using the same content
"""
    }

    @Test
    void "handles second and third level headers with formatting"() {
        def markdown = """# Main Section

Introduction text.

## Second Level **Bold** Header

Content under second level.

### Third Level *Italic* Header

Content under third level.

## Another **Second** Level

More content here."""

        def content = generateContent(markdown)

        content.should == """[//]: # (this is an auto generated file)
"Test Guide" full guide:

# Chapter :: Page One :: Main Section
answer-link: https://example.com/test/chapter/page-one#main-section

Introduction text.

## Second Level **Bold** Header

Content under second level.

### Third Level *Italic* Header

Content under third level.

## Another **Second** Level

More content here.
"""
    }

    @Test
    void "does not include heading props in output"() {
        def markdown = """# Main Section

## API Header {style: "api"}

Content here.

## Custom Anchor {#my-anchor}

More content."""

        def content = generateContent(markdown)

        content.should == """[//]: # (this is an auto generated file)
"Test Guide" full guide:

# Chapter :: Page One :: Main Section
answer-link: https://example.com/test/chapter/page-one#main-section

## API Header

Content here.

## Custom Anchor

More content.
"""
    }

    @Test
    void "handles list items with code blocks"() {
        def markdown = """# List With Code

- first item
- ```java
  code inside list
  ```
- last item"""

        def content = generateContent(markdown)

        content.should == """[//]: # (this is an auto generated file)
"Test Guide" full guide:

# Chapter :: Page One :: List With Code
answer-link: https://example.com/test/chapter/page-one#list-with-code

- first item
- ```java
code inside list
```

- last item
"""
    }

    @Test
    void "handles comprehensive markdown with all formats and include plugin"() {
        def markdown = """# Documentation

This paragraph has **bold**, *italic*, ~~strikethrough~~, and `inline code` formatting.

Check out [*formatted* link](https://example.com) for more info.

> This is a block quote with **bold** text inside.

---

## Code Example

```python
def hello():
    print("world")
```

## Lists

- Dash item one
- Dash item two

* Asterisk item one
* Asterisk item two

1. Numbered item
2. Another numbered

:include-dummy: plugin-content
"""

        def content = generateContent(markdown)

        content.should == """[//]: # (this is an auto generated file)
"Test Guide" full guide:

# Chapter :: Page One :: Documentation
answer-link: https://example.com/test/chapter/page-one#documentation

This paragraph has **bold**, *italic*, ~~strikethrough~~, and `inline code` formatting.

Check out [*formatted* link](https://example.com) for more info.

> This is a block quote with **bold** text inside.

---

## Code Example

```python
def hello():
    print("world")
```

## Lists

- Dash item one
- Dash item two

* Asterisk item one
* Asterisk item two

1. Numbered item
2. Another numbered

**Dummy plugin content**: plugin-content
"""
    }

    private static String generateContent(String markdown) {
        def docMeta = new DocMeta([id: "test", title: "Test Guide"])

        def toc = new TableOfContents()
        def tocItem = toc.addTocItem("chapter", "page-one")

        def parserResult = parser.parse(Paths.get("test.md"), markdown)

        def page = new Page(parserResult.docElement(), Instant.now(), new PageMeta())

        def generator = new LlmContentGenerator(
            docMeta,
            "https://example.com",
            [(tocItem): page],
            [(tocItem): parserResult]
        )

        return generator.generateContent()
    }
}
