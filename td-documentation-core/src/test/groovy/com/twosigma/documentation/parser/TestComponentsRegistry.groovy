package com.twosigma.documentation.parser

import com.twosigma.documentation.codesnippets.CodeTokenizer
import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.extensions.PluginResourcesResolver
import com.twosigma.documentation.validation.DocStructure

/**
 * @author mykola
 */
class TestComponentsRegistry implements ComponentsRegistry {
    private TestDocStructure docStructure = new TestDocStructure()
    MarkupParser parser = new TestMarkupParser()

    @Override
    MarkupParser parser() {
        return parser
    }

    @Override
    CodeTokenizer codeTokenizer() {
        return new TestCodeTokenizer()
    }

    @Override
    PluginResourcesResolver includeResourceResolver() {
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