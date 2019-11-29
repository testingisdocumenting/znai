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

    default Stream<WebResource> htmlHeadResources() {
        return Stream.empty();
    }

    default Stream<WebResource> htmlBodyResources() {
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
