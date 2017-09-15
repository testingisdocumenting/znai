package com.twosigma.documentation.website;

import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.html.WebResource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private Path root;
    private Map<String, ?> definition;
    private ResourcesResolver resourcesResolver;

    /**
     * @param resourcesResolver resource resolving component
     * @param definition extensions information in a tree like format.
     */
    public WebSiteExtensions(ResourcesResolver resourcesResolver, Map<String, ?> definition) {
        this.resourcesResolver = resourcesResolver;
        this.root = root;
        this.definition = definition;

        initCssPaths();
    }

    public List<WebResource> getCssResources() {
        return cssResources;
    }

    @SuppressWarnings("unchecked")
    private void initCssPaths() {
        cssResources = new ArrayList<>();
        Object cssResources = definition.get("cssResources");

        this.cssResources = cssResources == null ?
                Collections.emptyList():
                ((List<String>) cssResources).stream().map(p -> WebResource.withPath(resourcesResolver.fullPath(p), p))
                        .collect(toList());
    }
}
