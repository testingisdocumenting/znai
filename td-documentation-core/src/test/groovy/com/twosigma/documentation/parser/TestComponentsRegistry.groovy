package com.twosigma.documentation.parser

import com.twosigma.documentation.core.ComponentsRegistry
import com.twosigma.documentation.core.GlobalAssetsRegistry
import com.twosigma.documentation.core.ResourcesResolver
import com.twosigma.documentation.parser.commonmark.MarkdownParser
import com.twosigma.documentation.structure.DocStructure

import java.nio.file.Paths

class TestComponentsRegistry implements ComponentsRegistry {
    public static final TestComponentsRegistry INSTANCE = new TestComponentsRegistry()

    private TestDocStructure docStructure = new TestDocStructure()
    MarkupParser defaultParser = new TestMarkupParser()
    MarkupParser markdownParser = new TestMarkdownParser()

    private GlobalAssetsRegistry assetsRegistry = new GlobalAssetsRegistry()

    private TestComponentsRegistry() {
    }

    @Override
    MarkupParser defaultParser() {
        return defaultParser
    }

    @Override
    MarkdownParser markdownParser() {
        return markdownParser
    }

    @Override
    ResourcesResolver resourceResolver() {
        return new TestResourceResolver(Paths.get(""))
    }

    TestDocStructure getValidator() {
        return docStructure
    }

    @Override
    DocStructure docStructure() {
        return docStructure
    }

    @Override
    GlobalAssetsRegistry globalAssetsRegistry() {
        return assetsRegistry
    }
}