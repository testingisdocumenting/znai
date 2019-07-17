package com.twosigma.znai.web.extensions;

import com.twosigma.znai.web.WebResource;

import java.util.stream.Stream;

/**
 * Provides files to deploy and/or to include to a generated website
 */
public interface WebSiteResourcesProvider {
    default Stream<WebResource> cssResources() {
        return Stream.empty();
    }

    default Stream<WebResource> htmlResources() {
        return Stream.empty();
    }

    default Stream<WebResource> jsResources() {
        return Stream.empty();
    }

    default Stream<WebResource> jsClientOnlyResources() {
        return Stream.empty();
    }

    default Stream<WebResource> additionalFilesToDeploy() {
        return Stream.empty();
    }
}
