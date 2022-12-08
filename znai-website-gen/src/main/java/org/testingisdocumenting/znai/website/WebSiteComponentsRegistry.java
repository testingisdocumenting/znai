/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.website;

import org.testingisdocumenting.znai.core.ComponentsRegistry;
import org.testingisdocumenting.znai.core.DocConfig;
import org.testingisdocumenting.znai.core.GlobalAssetsRegistry;
import org.testingisdocumenting.znai.extensions.PluginParamsFactory;
import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.parser.MarkupParser;
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser;
import org.testingisdocumenting.znai.structure.DocStructure;
import org.testingisdocumenting.znai.time.SystemTimeService;
import org.testingisdocumenting.znai.time.TimeService;

import java.nio.file.Path;

public class WebSiteComponentsRegistry implements ComponentsRegistry {
    private MarkupParser defaultParser;
    private MarkdownParser markdownParser;
    private PluginParamsFactory pluginParamsFactory;
    private ResourcesResolver resourcesResolver;
    private DocStructure docStructure;

    private final GlobalAssetsRegistry assetsRegistry;

    private final TimeService timeService;

    private final DocConfig docConfig;

    public WebSiteComponentsRegistry(Path docRootPath, boolean validateExternalLinks) {
        assetsRegistry = new GlobalAssetsRegistry();
        timeService = new SystemTimeService();
        docConfig = new DocConfig(docRootPath, validateExternalLinks);
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
    public PluginParamsFactory pluginParamsFactory() {
        return pluginParamsFactory;
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
    public DocConfig docConfig() {
        return docConfig;
    }

    @Override
    public GlobalAssetsRegistry globalAssetsRegistry() {
        return assetsRegistry;
    }

    @Override
    public TimeService timeService() {
        return timeService;
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

    public void setPluginParamsFactory(PluginParamsFactory pluginParamsFactory) {
        this.pluginParamsFactory = pluginParamsFactory;
    }

    public void setDocStructure(DocStructure docStructure) {
        this.docStructure = docStructure;
    }
}
