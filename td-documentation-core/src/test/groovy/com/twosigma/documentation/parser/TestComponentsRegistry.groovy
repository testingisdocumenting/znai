package com.twosigma.documentation.parser

import com.twosigma.documentation.codesnippets.CodeTokenizer
import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.core.ResourcesResolver
import com.twosigma.documentation.parser.commonmark.MarkdownParser
import com.twosigma.documentation.structure.DocStructure

/**
 * @author mykola
 */
class TestComponentsRegistry implements ComponentsRegistry {
    public static final TestComponentsRegistry INSTANCE = new TestComponentsRegistry()
    
    private TestDocStructure docStructure = new TestDocStructure()
    MarkupParser defaultParser = new TestMarkupParser()
    MarkupParser markdownParser = new TestMarkdownParser()

    @Override
    MarkupParser defaultParser() {
        return defaultParser
    }

    @Override
    MarkdownParser markdownParser() {
        return markdownParser
    }

    @Override
    CodeTokenizer codeTokenizer() {
        return new TestCodeTokenizer()
    }

    @Override
    ResourcesResolver resourceResolver() {
        return new TestResourceResolver()
    }

    TestDocStructure getValidator() {
        return docStructure
    }

    @Override
    DocStructure docStructure() {
        return docStructure
    }
}