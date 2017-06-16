package com.twosigma.documentation.core;

import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.extensions.PluginResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.validation.DocStructure;

/**
 * simple components registry to avoid introducing DI frameworks.
 * One place where we don't control dependencies passing is {@link com.twosigma.documentation.extensions.include.IncludePlugin}
 *
 * @author mykola
 * @see com.twosigma.documentation.extensions.include.IncludePlugin
 */
public interface ComponentsRegistry {
    MarkupParser parser();
    CodeTokenizer codeTokenizer();
    PluginResourcesResolver includeResourceResolver();
    DocStructure docStructure();
}
