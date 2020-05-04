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

package org.testingisdocumenting.znai.website;

import org.testingisdocumenting.znai.resources.ResourcesResolver;
import org.testingisdocumenting.znai.web.WebResource;
import org.testingisdocumenting.znai.web.extensions.WebSiteResourcesProvider;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Represents web site extensions provided by a user
 */
public class WebSiteUserExtensions implements WebSiteResourcesProvider {
    private final List<WebResource> cssResources;
    private final List<WebResource> jsResources;
    private final List<WebResource> jsClientOnlyResources;
    private final List<WebResource> htmlHeadResources;
    private final List<WebResource> htmlBodyResources;
    private final List<WebResource> additionalFilesToDeploy;

    private final Map<String, ?> definition;
    private final ResourcesResolver resourcesResolver;

    /**
     * @param resourcesResolver resource resolving component
     * @param definition extensions information in a tree like format.
     */
    public WebSiteUserExtensions(ResourcesResolver resourcesResolver, Map<String, ?> definition) {
        this.resourcesResolver = resourcesResolver;
        this.definition = definition;

        this.cssResources = extract("cssResources", "style.css");
        this.jsResources = extract("jsResources");
        this.jsClientOnlyResources = extract("jsClientOnlyResources");
        this.htmlBodyResources = extract("htmlResources"); // name is without "head" part for compatibility with existing extensions out there
        this.htmlHeadResources = extract("htmlHeadResources", "tracking.html");
        this.additionalFilesToDeploy = extract("additionalFilesToDeploy");
    }

    @Override
    public Stream<WebResource> cssResources() {
        return cssResources.stream();
    }

    @Override
    public Stream<WebResource> htmlHeadResources() {
        return htmlHeadResources.stream();
    }

    @Override
    public Stream<WebResource> htmlBodyResources() {
        return htmlBodyResources.stream();
    }

    @Override
    public Stream<WebResource> jsResources() {
        return jsResources.stream();
    }

    @Override
    public Stream<WebResource> jsClientOnlyResources() {
        return jsClientOnlyResources.stream();
    }

    @Override
    public Stream<WebResource> additionalFilesToDeploy() {
        return additionalFilesToDeploy.stream();
    }

    private List<WebResource> extract(String extensionsKey) {
        return extract(extensionsKey, "");
    }

    private List<WebResource> extract(String extensionsKey, String fileName) {
        Stream<WebResource> fromExtensions = extractFromExtensions(extensionsKey);
        Stream<WebResource> fromFile = fileName.isEmpty() ?
                Stream.empty() :
                extractFromFile(fileName);

        return Stream.concat(fromExtensions, fromFile).collect(toList());
    }

    @SuppressWarnings("unchecked")
    private Stream<WebResource> extractFromExtensions(String key) {
        List<String> resources = (List<String>) definition.get(key);

        return resources == null ?
                Stream.empty():
                resources.stream()
                        .map(p -> WebResource.withPath(resourcesResolver.fullPath(p), p));
    }

    private Stream<WebResource> extractFromFile(String name) {
        if (!resourcesResolver.canResolve(name)) {
            return Stream.empty();
        }

        Path stylePath = resourcesResolver.fullPath(name);
        return Stream.of(WebResource.withPath(stylePath, name));
    }
}
