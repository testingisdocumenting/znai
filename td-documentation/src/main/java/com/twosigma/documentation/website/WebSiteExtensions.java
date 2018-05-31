package com.twosigma.documentation.website;

import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.html.WebResource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Represents web site extensions provided by a user
 * @author mykola
 */
public class WebSiteExtensions {
    private List<WebResource> cssResources;
    private List<WebResource> jsResources;
    private List<WebResource> htmlResources;
    private Map<String, ?> definition;
    private ResourcesResolver resourcesResolver;

    /**
     * @param resourcesResolver resource resolving component
     * @param definition extensions information in a tree like format.
     */
    public WebSiteExtensions(ResourcesResolver resourcesResolver, Map<String, ?> definition) {
        this.resourcesResolver = resourcesResolver;
        this.definition = definition;

        this.cssResources = extractWebResources("cssResources");
        this.jsResources = extractWebResources("jsResources");
        this.htmlResources = extractWebResources("htmlResources");
    }

    public List<WebResource> getCssResources() {
        return cssResources;
    }

    public List<WebResource> getJsResources() {
        return jsResources;
    }

    public List<WebResource> getHtmlResources() {
        return htmlResources;
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
}
