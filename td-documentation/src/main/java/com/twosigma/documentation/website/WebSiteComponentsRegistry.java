package com.twosigma.documentation.website;

import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.core.GlobalAssetsRegistry;
import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.parser.commonmark.MarkdownParser;
import com.twosigma.documentation.structure.DocStructure;

/**
 * @author mykola
 */
public class WebSiteComponentsRegistry implements ComponentsRegistry {
    private MarkupParser defaultParser;
    private MarkdownParser markdownParser;
    private ResourcesResolver resourcesResolver;
    private DocStructure docStructure;

    private GlobalAssetsRegistry assetsRegistry;

    public WebSiteComponentsRegistry() {
        assetsRegistry = new GlobalAssetsRegistry();
    }

    @Override
    public MarkupParser defaultParser() {
        return defaultParser;
    }

    @Override
    public MarkdownParser markdownParser() {
        return markdownParser;
    }

    @Override
    public ResourcesResolver resourceResolver() {
        return resourcesResolver;
    }

    @Override
    public DocStructure docStructure() {
        return docStructure;
    }

    @Override
    public GlobalAssetsRegistry globalAssetsRegistry() {
        return assetsRegistry;
    }

    public void setDefaultParser(MarkupParser parser) {
        this.defaultParser = parser;
    }

    public void setMarkdownParser(MarkdownParser parser) {
        this.markdownParser = parser;
    }

    public void setResourcesResolver(ResourcesResolver resourcesResolver) {
        this.resourcesResolver = resourcesResolver;
    }

    public void setDocStructure(DocStructure docStructure) {
        this.docStructure = docStructure;
    }
}
