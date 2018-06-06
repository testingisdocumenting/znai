package com.twosigma.documentation.web.extensions;

import com.twosigma.documentation.core.ResourcesResolver;
import com.twosigma.documentation.web.WebResource;

import java.util.stream.Stream;

/**
 * Provides files to deploy and/or to include to a generated website
 */
public interface WebSiteResourcesProvider {
    default Stream<WebResource> cssResources(ResourcesResolver resourcesResolver) {
        return Stream.empty();
    }

    default Stream<WebResource> htmlResources(ResourcesResolver resourcesResolver) {
        return Stream.empty();
    }

    default Stream<WebResource> jsResources(ResourcesResolver resourcesResolver) {
        return Stream.empty();
    }

    default Stream<WebResource> jsClientOnlyResources(ResourcesResolver resourcesResolver) {
        return Stream.empty();
    }

    default Stream<WebResource> additionalFilesToDeploy(ResourcesResolver resourcesResolver) {
        return Stream.empty();
    }
}
