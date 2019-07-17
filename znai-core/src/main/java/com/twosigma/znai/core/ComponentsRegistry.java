package com.twosigma.znai.core;

import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.commonmark.MarkdownParser;
import com.twosigma.znai.structure.DocStructure;
import com.twosigma.znai.extensions.include.IncludePlugin;

/**
 * simple components registry to avoid introduction of DI frameworks.
 * One place where we don't control dependencies passing is {@link IncludePlugin}
 *
 * @see IncludePlugin
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

    ResourcesResolver resourceResolver();
    DocStructure docStructure();

    GlobalAssetsRegistry globalAssetsRegistry();
}
