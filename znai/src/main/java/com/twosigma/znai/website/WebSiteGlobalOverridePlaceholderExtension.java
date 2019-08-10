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

package com.twosigma.znai.website;

import com.twosigma.znai.web.WebResource;
import com.twosigma.znai.web.extensions.WebSiteResourcesProvider;

import java.util.stream.Stream;

/**
 * znai can be used to not just generate, but also host multiple documentations.
 * a company may choose to override colors/logos/etc.
 * Instead of forcing each documentation to be rebuild for new values to take effect, global overrides
 * will be loaded from static/global-overrides.css.
 *
 * when znai is used to serve it will attempt to load static resources from classpath instead of a documentation files.
 * this provider inserts an empty placeholder to be deployed alongside documentation files
 */
public class WebSiteGlobalOverridePlaceholderExtension implements WebSiteResourcesProvider {
    @Override
    public Stream<WebResource> cssResources() {
        return Stream.of(WebResource.withTextContent("static/css/global-overrides.css", ""));
    }
}
