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
import com.twosigma.znai.utils.ServiceLoaderUtils;

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

    public static Stream<WebResource> htmlHeadResources() {
        return providers.stream().flatMap(WebSiteResourcesProvider::htmlHeadResources);
    }

    public static Stream<WebResource> htmlBodyResources() {
        return providers.stream().flatMap(WebSiteResourcesProvider::htmlBodyResources);
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
