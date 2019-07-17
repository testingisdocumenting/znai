package com.twosigma.znai.server.landing;

import com.twosigma.znai.html.HtmlPage;
import com.twosigma.znai.html.reactjs.HtmlReactJsPage;
import com.twosigma.znai.html.reactjs.ReactJsBundle;
import com.twosigma.znai.server.FavIcons;
import com.twosigma.znai.server.urlhandlers.UrlContentHandler;
import com.twosigma.znai.web.WebResource;
import com.twosigma.znai.web.extensions.WebSiteResourcesProviders;

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
    public String buildContent(ReactJsBundle reactJsBundle) {
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

        WebSiteResourcesProviders.jsResources().forEach(htmlPage::addJavaScript);
        WebSiteResourcesProviders.jsClientOnlyResources().forEach(htmlPage::addJavaScript);
        WebSiteResourcesProviders.cssResources().forEach(htmlPage::addCss);
        WebSiteResourcesProviders.htmlResources().map(WebResource::getTextContent)
                .forEach(text -> htmlPage.addToBody(() -> text));

        return htmlPage.render("");
    }
}
