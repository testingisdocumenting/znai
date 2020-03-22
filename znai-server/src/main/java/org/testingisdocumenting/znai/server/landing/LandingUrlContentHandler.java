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

package com.twosigma.znai.server.landing;

import com.twosigma.znai.html.HtmlPage;
import com.twosigma.znai.html.reactjs.HtmlReactJsPage;
import com.twosigma.znai.html.reactjs.ReactJsBundle;
import com.twosigma.znai.server.FavIcons;
import com.twosigma.znai.server.urlhandlers.UrlContentHandler;
import io.vertx.ext.web.RoutingContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LandingUrlContentHandler implements UrlContentHandler {
    private String landingTitle;
    private String landingType;

    public LandingUrlContentHandler(String landingTitle, String landingType) {
        this.landingTitle = landingTitle;
        this.landingType = landingType;
    }

    @Override
    public String url() {
        return "/";
    }

    @Override
    public String buildContent(RoutingContext ctx, ReactJsBundle reactJsBundle) {
        List<Map<String, Object>> documentations = LandingDocEntriesProviders.provide()
                .map(LandingDocEntry::toMap)
                .collect(Collectors.toList());

        HtmlReactJsPage htmlReactJsPage = new HtmlReactJsPage(reactJsBundle);

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("documentations", documentations);
        props.put("type", landingType);
        props.put("title", landingTitle);

        HtmlPage htmlPage = htmlReactJsPage.create(landingTitle + " " + landingType,
                "Landing", props, () -> "", FavIcons.DEFAULT_ICON_PATH);
        return htmlPage.render("");
    }
}
