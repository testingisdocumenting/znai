package com.twosigma.documentation;

import com.twosigma.documentation.extensions.include.IncludeResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;

/**
 * simple components registry to avoid introducing DI frameworks.
 * One place where we don't control dependencies passing is {@link com.twosigma.documentation.extensions.include.IncludePlugin}
 *
 * @author mykola
 * @see com.twosigma.documentation.extensions.include.IncludePlugin
 */
public interface ComponentsRegistry {
    MarkupParser parser();
    IncludeResourcesResolver includeResourceResolver();
}
