package com.twosigma.documentation.parser;

import com.twosigma.documentation.parser.docelement.DocElement;

/**
 * Parser for markup languages. Markup in {@link DocElement} out. Markup here stands for generic human readable language.
 * @author mykola
 */
public interface MarkupParser {
    DocElement parse(final String markup);
}
