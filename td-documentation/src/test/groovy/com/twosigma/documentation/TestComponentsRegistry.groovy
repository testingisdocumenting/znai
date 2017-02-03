package com.twosigma.documentation

import com.twosigma.documentation.extensions.include.IncludeResourcesResolver
import com.twosigma.documentation.extensions.include.TestResourceResolver
import com.twosigma.documentation.parser.MarkupParser

/**
 * @author mykola
 */
class TestComponentsRegistry implements ComponentsRegistry {
    @Override
    public MarkupParser parser() {
        return null;
    }

    @Override
    public IncludeResourcesResolver includeResourceResolver() {
        return new TestResourceResolver();
    }
}