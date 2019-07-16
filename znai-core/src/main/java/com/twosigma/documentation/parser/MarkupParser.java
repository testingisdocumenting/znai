package com.twosigma.documentation.parser;

import com.twosigma.documentation.parser.docelement.DocElement;

import java.nio.file.Path;
import java.util.Map;

/**
 * Parser for markup languages. Markup in {@link DocElement} out. Markup here stands for generic human readable language.
 */
public interface MarkupParser {
    /**
     * parses markup
     *
     * @param path   path of the content, will be used to resolve resources location (as one of the options)
     * @param markup markup to parse
     * @return markup result
     * @see MarkupPathsResolution
     */
    MarkupParserResult parse(Path path, String markup);
}