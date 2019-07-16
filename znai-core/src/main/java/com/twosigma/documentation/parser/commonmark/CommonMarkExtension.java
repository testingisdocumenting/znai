package com.twosigma.documentation.parser.commonmark;

import com.twosigma.documentation.parser.commonmark.include.IncludeBlockParser;
import org.commonmark.parser.Parser.Builder;
import org.commonmark.parser.Parser.ParserExtension;

public class CommonMarkExtension implements ParserExtension {
    @Override
    public void extend(final Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new IncludeBlockParser.Factory());
    }
}
