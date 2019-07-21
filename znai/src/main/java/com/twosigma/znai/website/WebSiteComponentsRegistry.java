/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.znai.website;

import com.twosigma.znai.core.ComponentsRegistry;
import com.twosigma.znai.core.GlobalAssetsRegistry;
import com.twosigma.znai.core.ResourcesResolver;
import com.twosigma.znai.parser.MarkupParser;
import com.twosigma.znai.parser.commonmark.MarkdownParser;
import com.twosigma.znai.structure.DocStructure;

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
