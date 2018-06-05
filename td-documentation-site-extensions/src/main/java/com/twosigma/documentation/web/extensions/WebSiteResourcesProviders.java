package com.twosigma.documentation.web.extensions;

import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.web.WebResource;
import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Set;
import java.util.stream.Stream;

public class WebSiteResourcesProviders {
    private static final Set<WebSiteResourcesProvider> providers =
            ServiceLoaderUtils.load(WebSiteResourcesProvider.class);

    public static void add(WebSiteResourcesProvider provider) {
        providers.add(provider);
    }

    public static Stream<WebResource> cssResources(ResourcesResolver resourcesResolver) {
        return providers.stream().flatMap(p -> p.cssResources(resourcesResolver));
    }

    public static Stream<WebResource> htmlResources(ResourcesResolver resourcesResolver) {
        return providers.stream().flatMap(p -> p.htmlResources(resourcesResolver));
    }

    public static Stream<WebResource> jsResources(ResourcesResolver resourcesResolver) {
        return providers.stream().flatMap(p -> p.jsResources(resourcesResolver));
    }

    public static Stream<WebResource> jsClientOnlyResources(ResourcesResolver resourcesResolver) {
        return providers.stream().flatMap(p -> p.jsClientOnlyResources(resourcesResolver));
    }

    public static Stream<WebResource> additionalFilesToDeploy(ResourcesResolver resourcesResolver) {
        return providers.stream().flatMap(p -> p.additionalFilesToDeploy(resourcesResolver));
    }
}
