package com.twosigma.documentation.web.extensions;

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

    public static Stream<WebResource> cssResources() {
        return providers.stream().flatMap(WebSiteResourcesProvider::cssResources);
    }

    public static Stream<WebResource> htmlResources() {
        return providers.stream().flatMap(WebSiteResourcesProvider::htmlResources);
    }

    public static Stream<WebResource> jsResources() {
        return providers.stream().flatMap(WebSiteResourcesProvider::jsResources);
    }

    public static Stream<WebResource> jsClientOnlyResources() {
        return providers.stream().flatMap(WebSiteResourcesProvider::jsClientOnlyResources);
    }

    public static Stream<WebResource> additionalFilesToDeploy() {
        return providers.stream().flatMap(WebSiteResourcesProvider::additionalFilesToDeploy);
    }
}
