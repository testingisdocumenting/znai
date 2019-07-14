package com.twosigma.documentation.website;

import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.web.WebResource;
import com.twosigma.documentation.web.extensions.WebSiteResourcesProvider;

import java.util.Collections;
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
    private final List<WebResource> htmlResources;
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

        this.cssResources = extractWebResources("cssResources");
        this.jsResources = extractWebResources("jsResources");
        this.jsClientOnlyResources = extractWebResources("jsClientOnlyResources");
        this.htmlResources = extractWebResources("htmlResources");
        this.additionalFilesToDeploy = extractWebResources("additionalFilesToDeploy");
    }

    @SuppressWarnings("unchecked")
    private List<WebResource> extractWebResources(String key) {
        List<String> resources = (List<String>) definition.get(key);

        return resources == null ?
                Collections.emptyList():
                resources.stream()
                        .map(p -> WebResource.withPath(resourcesResolver.fullPath(p), p))
                        .collect(toList());
    }

    @Override
    public Stream<WebResource> cssResources() {
        return cssResources.stream();
    }

    @Override
    public Stream<WebResource> htmlResources() {
        return htmlResources.stream();
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
}
