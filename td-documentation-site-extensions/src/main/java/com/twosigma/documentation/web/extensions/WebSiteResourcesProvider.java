package com.twosigma.documentation.web.extensions;

import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.web.WebResource;

import java.util.stream.Stream;

/**
 * Provides files to deploy and/or to include to a generated website
 */
public interface WebSiteResourcesProvider {
    Stream<WebResource> cssResources(ResourcesResolver resourcesResolver);
    Stream<WebResource> htmlResources(ResourcesResolver resourcesResolver);
    Stream<WebResource> jsResources(ResourcesResolver resourcesResolver);
    Stream<WebResource> jsClientOnlyResources(ResourcesResolver resourcesResolver);
    Stream<WebResource> additionalFilesToDeploy(ResourcesResolver resourcesResolver);
}
