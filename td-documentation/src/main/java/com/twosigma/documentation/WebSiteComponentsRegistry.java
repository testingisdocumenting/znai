package com.twosigma.documentation;

import com.twosigma.documentation.codesnippets.CodeTokenizer;
import com.twosigma.documentation.core.ComponentsRegistry;
import com.twosigma.documentation.extensions.PluginResourcesResolver;
import com.twosigma.documentation.parser.MarkupParser;
import com.twosigma.documentation.structure.DocStructure;

/**
 * @author mykola
 */
public class WebSiteComponentsRegistry implements ComponentsRegistry {
    private MarkupParser parser;
    private CodeTokenizer codeTokenizer;
    private PluginResourcesResolver pluginResourcesResolver;
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
    public PluginResourcesResolver includeResourceResolver() {
        return pluginResourcesResolver;
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

    public void setPluginResourcesResolver(PluginResourcesResolver pluginResourcesResolver) {
        this.pluginResourcesResolver = pluginResourcesResolver;
    }

    public void setDocStructure(DocStructure docStructure) {
        this.docStructure = docStructure;
    }
}
