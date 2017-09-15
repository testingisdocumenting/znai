package com.twosigma.documentation.website;

import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.structure.DocStructure;

/**
 * @author mykola
 */
public class WebSiteComponentsRegistry implements ComponentsRegistry {
    private MarkupParser parser;
    private CodeTokenizer codeTokenizer;
    private ResourcesResolver resourcesResolver;
    private DocStructure docStructure;

    @Override
    public MarkupParser parser() {
        return parser;
    }

    @Override
    public CodeTokenizer codeTokenizer() {
        return codeTokenizer;
    }

    @Override
    public ResourcesResolver resourceResolver() {
        return resourcesResolver;
    }

    @Override
    public DocStructure docStructure() {
        return docStructure;
    }

    public void setParser(MarkupParser parser) {
        this.parser = parser;
    }

    public void setCodeTokenizer(CodeTokenizer codeTokenizer) {
        this.codeTokenizer = codeTokenizer;
    }

    public void setResourcesResolver(ResourcesResolver resourcesResolver) {
        this.resourcesResolver = resourcesResolver;
    }

    public void setDocStructure(DocStructure docStructure) {
        this.docStructure = docStructure;
    }
}
