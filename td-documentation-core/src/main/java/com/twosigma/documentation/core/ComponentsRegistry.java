package com.twosigma.documentation.core;

import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.structure.DocStructure;

/**
 * simple components registry to avoid introduction of DI frameworks.
 * One place where we don't control dependencies passing is {@link com.twosigma.documentation.extensions.include.IncludePlugin}
 *
 * @author mykola
 * @see com.twosigma.documentation.extensions.include.IncludePlugin
 */
public interface ComponentsRegistry {
    /**
     * documentation wide default parser, can be markdown or any other parser that is used to build a documentation.
     * @return instance of a default parser
     */
    MarkupParser defaultParser();

    /**
     * markdown specific parser
     * @return markdown parser
     */
    MarkdownParser markdownParser();

    CodeTokenizer codeTokenizer();
    ResourcesResolver resourceResolver();
    DocStructure docStructure();
}
