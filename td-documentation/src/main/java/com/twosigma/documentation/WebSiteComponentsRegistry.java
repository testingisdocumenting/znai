package com.twosigma.documentation;

import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.include.IncludeResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;

/**
 * @author mykola
 */
public class WebSiteComponentsRegistry implements ComponentsRegistry {
    private MarkupParser parser;
    private CodeTokenizer codeTokenizer;
    private IncludeResourcesResolver includeResourcesResolver;

    @Override
    public MarkupParser parser() {
        return parser;
    }

    @Override
    public CodeTokenizer codeTokenizer() {
        return codeTokenizer;
    }

    @Override
    public IncludeResourcesResolver includeResourceResolver() {
        return includeResourcesResolver;
    }

    public void setParser(MarkupParser parser) {
        this.parser = parser;
    }

    public void setCodeTokenizer(CodeTokenizer codeTokenizer) {
        this.codeTokenizer = codeTokenizer;
    }

    public void setIncludeResourcesResolver(IncludeResourcesResolver includeResourcesResolver) {
        this.includeResourcesResolver = includeResourcesResolver;
    }
}
