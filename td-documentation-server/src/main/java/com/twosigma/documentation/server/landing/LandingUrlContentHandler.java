package com.twosigma.documentation.server.landing;

import com.twosigma.documentation.html.HtmlPage;
import com.twosigma.documentation.html.reactjs.HtmlReactJsPage;
import com.twosigma.documentation.html.reactjs.ReactJsNashornEngine;
import com.twosigma.documentation.server.urlhandlers.UrlContentHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LandingUrlContentHandler implements UrlContentHandler {
    @Override
    public String url() {
        return "/";
    }

    @Override
    public String buildContent(ReactJsNashornEngine nashornEngine) {
        List<Map<String, Object>> documentations = LandingDocEntriesProviders.provide()
                .map(LandingDocEntry::toMap)
                .collect(Collectors.toList());

        HtmlReactJsPage htmlReactJsPage = new HtmlReactJsPage(nashornEngine);
        HtmlPage htmlPage = htmlReactJsPage.createWithClientSideOnly("Documentations",
                "Landing", Collections.singletonMap("documentations", documentations));

        return htmlPage.render("");
    }
}
