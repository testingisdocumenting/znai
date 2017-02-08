package com.twosigma.documentation.parser.commonmark;

import org.commonmark.parser.Parser.Builder;
import org.commonmark.parser.Parser.ParserExtension;

import com.twosigma.documentation.parser.commonmark.include.IncludePostProcessor;

/**
 * @author mykola
 */
public class CommonMarkExtension implements ParserExtension {
    @Override
    public void extend(final Builder parserBuilder) {
        parserBuilder.postProcessor(new IncludePostProcessor());
    }
}
